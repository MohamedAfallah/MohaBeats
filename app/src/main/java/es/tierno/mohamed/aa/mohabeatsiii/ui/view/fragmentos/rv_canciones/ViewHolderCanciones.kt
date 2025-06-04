package es.tierno.mohamed.aa.mohabeatsiii.ui.view.fragmentos.rv_canciones

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat // Añadir este import
import androidx.recyclerview.widget.RecyclerView
import com.airbnb.lottie.LottieAnimationView
import com.bumptech.glide.Glide
import es.tierno.mohamed.aa.mohabeatsiii.R
import es.tierno.mohamed.aa.mohabeatsiii.domain.model.Musica

class ViewHolderCanciones(
    view: View,
    private val onReproducirClick: (Musica) -> Unit,
    private val onFavoritoClick: (Musica, Boolean) -> Unit // Nuevo callback: canción y su estado actual (antes del clic)
) : RecyclerView.ViewHolder(view) {

    private val imagen = view.findViewById<ImageView>(R.id.imagenCancion)
    private val nombre = view.findViewById<TextView>(R.id.nombreCancion)
    private val artista = view.findViewById<TextView>(R.id.nombreArtista)
    private val favoritoIcon = view.findViewById<ImageView>(R.id.iconFavorito)
    private val lottieFavorito = view.findViewById<LottieAnimationView>(R.id.lottieFavorito)
    private val iconReproducir = view.findViewById<ImageView>(R.id.iconReproducir)

    private var currentCancion: Musica? = null

    fun render(cancion: Musica, esFavoritoInicial: Boolean) {
        currentCancion = cancion
        nombre.text = cancion.nombreCancion
        artista.text = cancion.nombreArtista
        Glide.with(imagen.context).load(cancion.urlImagen).into(imagen)

        actualizarIcono(esFavoritoInicial)

        itemView.setOnClickListener {
            onReproducirClick(cancion)
        }

        iconReproducir.setOnClickListener {
            onReproducirClick(cancion)
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

