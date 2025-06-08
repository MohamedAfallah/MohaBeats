package es.tierno.mohamed.aa.mohabeatsiii.ui.view.fragmentos.rv_canciones

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.view.View
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.airbnb.lottie.LottieAnimationView
import com.bumptech.glide.Glide
import es.tierno.mohamed.aa.mohabeatsiii.R
import es.tierno.mohamed.aa.mohabeatsiii.domain.model.Musica

class ViewHolderCanciones(
    view: View,
    private val onReproducirClick: (Musica) -> Unit,
    private val onFavoritoClick: (Musica, Boolean) -> Unit,
    private val onDescargarClick: (Musica) -> Unit,
    private val onAnadirClick: (Musica) -> Unit,
    private val onEliminarClick: (Musica) -> Unit
) : RecyclerView.ViewHolder(view) {

    private val imagen = view.findViewById<ImageView>(R.id.imagenCancion)
    private val nombre = view.findViewById<TextView>(R.id.nombreCancion)
    private val artista = view.findViewById<TextView>(R.id.nombreArtista)
    private val favoritoIcon = view.findViewById<ImageView>(R.id.iconFavorito)
    private val lottieFavorito = view.findViewById<LottieAnimationView>(R.id.lottieFavorito)
    private val icDescargar = view.findViewById<LottieAnimationView>(R.id.ic_descargas)
    private val btnAnadir = view.findViewById<ImageButton>(R.id.btnAnadir)
    private val btnEliminar = view.findViewById<ImageButton>(R.id.btnEliminar)


    private var currentCancion: Musica? = null

    fun render(
        cancion: Musica,
        esFavoritoInicial: Boolean,
        mostrarFavorito: Boolean = true,
        mostrarDescargar: Boolean = true,
        mostrarAnadir: Boolean = true,
        mostrarEliminar: Boolean = true
    ) {
        currentCancion = cancion
        nombre.text = cancion.nombreCancion
        artista.text = cancion.nombreArtista
        Glide.with(imagen.context).load(cancion.urlImagen).into(imagen)

        actualizarIcono(esFavoritoInicial)

        favoritoIcon.visibility = if (mostrarFavorito) View.VISIBLE else View.GONE
        icDescargar.visibility = if (mostrarDescargar) View.VISIBLE else View.GONE
        btnAnadir.visibility = if (mostrarAnadir) View.VISIBLE else View.GONE
        btnEliminar.visibility = if (mostrarEliminar) View.VISIBLE else View.GONE


        itemView.setOnClickListener {
            onReproducirClick(cancion)
        }

        icDescargar.setOnClickListener {
            currentCancion?.let {
                icDescargar.playAnimation()
                onDescargarClick(it)
            }
        }

        favoritoIcon.setOnClickListener {
            currentCancion?.let {
                val esFavoritoActual = getEstadoFavoritoDesdeIcono()
                onFavoritoClick(it, esFavoritoActual)

                if (!esFavoritoActual) {
                    lottieFavorito.visibility = View.VISIBLE
                    lottieFavorito.playAnimation()
                    lottieFavorito.addAnimatorListener(object : AnimatorListenerAdapter() {
                        override fun onAnimationEnd(animation: Animator) {
                            lottieFavorito.visibility = View.GONE
                            lottieFavorito.removeAnimatorListener(this)
                        }
                    })
                }
            }
        }

        btnAnadir.setOnClickListener {
            currentCancion?.let {
                onAnadirClick(it)
            }
        }

        btnEliminar.setOnClickListener {
            currentCancion?.let {
                onEliminarClick(it)
            }
        }
    }

    private fun getEstadoFavoritoDesdeIcono(): Boolean {
        val currentDrawable = favoritoIcon.drawable
        val favoriteDrawable = ContextCompat.getDrawable(favoritoIcon.context, R.drawable.ic_favorito_true)
        return currentDrawable?.constantState == favoriteDrawable?.constantState
    }

    private fun actualizarIcono(esFavorito: Boolean) {
        val icono = if (esFavorito) {
            R.drawable.ic_favorito_true
        } else {
            R.drawable.ic_favorito
        }
        favoritoIcon.setImageResource(icono)
    }
}