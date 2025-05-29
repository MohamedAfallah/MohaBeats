package es.tierno.mohamed.aa.mohabeatsiii.ui.view.fragmentos.rv_playlist

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import es.tierno.mohamed.aa.mohabeatsiii.data.model.PlaylistModel
import es.tierno.mohamed.aa.mohabeatsiii.R

class ViewHolderPlaylist(view: View) : RecyclerView.ViewHolder(view) {
    private val imagen = view.findViewById<ImageView>(R.id.imgPlaylist)
    private val nombre = view.findViewById<TextView>(R.id.txtNombrePlaylist)
    private val cantidad = view.findViewById<TextView>(R.id.txtCantidadCanciones)


    fun render(playlist: PlaylistModel) {
        val url = if (playlist.canciones.isNotEmpty()) playlist.canciones[0].urlImagen else null
        nombre.text = playlist.nombre
        cantidad.text = "${playlist.canciones.size} canciones"

        Glide.with(imagen.context).load(url).into(imagen)
    }
}
