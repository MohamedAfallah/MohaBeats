package es.tierno.mohamed.aa.mohabeatsiii.ui.view.fragmentos.rv_playlist

import android.view.View
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.airbnb.lottie.LottieAnimationView
import com.bumptech.glide.Glide
import es.tierno.mohamed.aa.mohabeatsiii.R
import es.tierno.mohamed.aa.mohabeatsiii.domain.model.Playlist

class ViewHolderPlaylist(view: View) : RecyclerView.ViewHolder(view) {
    private val imagen = view.findViewById<ImageView>(R.id.imgPlaylist)
    private val nombre = view.findViewById<TextView>(R.id.txtNombrePlaylist)
    private val cantidad = view.findViewById<TextView>(R.id.txtCantidadCanciones)
    private val btnEliminar = view.findViewById<LottieAnimationView>(R.id.btnEliminar)
    private val btnAgregarPost = view.findViewById<ImageButton>(R.id.btnAgregarPost)

    fun render(
        playlist: Playlist,
        onItemClick: (Playlist) -> Unit,
        onEliminarClick: ((Playlist) -> Unit)? = null,
        onAgregarPostClick: ((Playlist) -> Unit)? = null,
        showEliminarButton: Boolean,
        showAgregarPostButton: Boolean
    ) {
        nombre.text = playlist.nombre
        cantidad.text = "${playlist.canciones.size} canciones"

        itemView.setOnClickListener {
            onItemClick(playlist)
        }

        btnEliminar.visibility = if (showEliminarButton) View.VISIBLE else View.GONE
        onEliminarClick?.let { click ->
            btnEliminar.setOnClickListener {
                click(playlist)
            }
        } ?: run {
            btnEliminar.setOnClickListener(null) // Clear listener if no callback
        }

        btnAgregarPost.visibility = if (showAgregarPostButton) View.VISIBLE else View.GONE
        onAgregarPostClick?.let { click ->
            btnAgregarPost.setOnClickListener {
                click(playlist)
            }
        } ?: run {
            btnAgregarPost.setOnClickListener(null) // Clear listener if no callback
        }
    }
}
