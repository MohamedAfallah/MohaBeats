package es.tierno.mohamed.aa.mohabeatsiii.ui.view.fragmentos.rv_canciones

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.airbnb.lottie.LottieAnimationView
import com.bumptech.glide.Glide
import es.tierno.mohamed.aa.mohabeatsiii.R
import es.tierno.mohamed.aa.mohabeatsiii.domain.model.Musica

class ViewHolderCanciones(
    view: View,
    private val onReproducirClick: (Musica) -> Unit // callback para botón de reproducción
) : RecyclerView.ViewHolder(view) {

    private val imagen = view.findViewById<ImageView>(R.id.imagenCancion)
    private val nombre = view.findViewById<TextView>(R.id.nombreCancion)
    private val artista = view.findViewById<TextView>(R.id.nombreArtista)
    private val favorito = view.findViewById<ImageView>(R.id.iconFavorito)
    private val lottieFavorito = view.findViewById<LottieAnimationView>(R.id.lottieFavorito)
    private val iconReproducir = view.findViewById<ImageView>(R.id.iconReproducir)

    private var esFavorito = false

    fun render(cancion: Musica) {
        nombre.text = cancion.nombreCancion
        artista.text = cancion.nombreArtista
        Glide.with(imagen.context).load(cancion.urlImagen).into(imagen)

        actualizarIcono()

        // Clic en itemView (excepto en los botones específicos)
        itemView.setOnClickListener {
            onReproducirClick(cancion)
        }

        // Clic en botón reproducir
        iconReproducir.setOnClickListener {
            // solo se reproduce, no se lanza itemView click
            onReproducirClick(cancion)
        }

        // Clic en favorito
        favorito.setOnClickListener {
            val eraFavorito = esFavorito
            esFavorito = !esFavorito
            actualizarIcono()

            if (!eraFavorito) {
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

    private fun actualizarIcono() {
        val icono = if (esFavorito) {
            R.drawable.ic_favorito_true
        } else {
            R.drawable.ic_favorito
        }
        favorito.setImageResource(icono)
    }
}

