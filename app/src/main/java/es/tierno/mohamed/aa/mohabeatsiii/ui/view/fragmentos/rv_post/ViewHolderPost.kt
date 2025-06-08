package es.tierno.mohamed.aa.mohabeatsiii.ui.view.fragmentos.rv_post

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import es.tierno.mohamed.aa.mohabeatsiii.databinding.ItemPostBinding
import es.tierno.mohamed.aa.mohabeatsiii.databinding.ItemPlaylistBinding
import es.tierno.mohamed.aa.mohabeatsiii.domain.model.PostConPlaylist

class ViewHolderPost(
    private val binding: ItemPostBinding,
    private val onPlaylistClick: (playlistId: String) -> Unit
) : RecyclerView.ViewHolder(binding.root) {
    fun bind(post: PostConPlaylist) {
        binding.txtNombreUsuarioPost.text = post.usuario.nombreCompleto
        binding.txtComentarioPost.text = post.comentario

        val itemPlaylistBinding = ItemPlaylistBinding.bind(binding.itemPlaylistIncluded.root)
        itemPlaylistBinding.txtNombrePlaylist.text = post.playlist.nombre
        itemPlaylistBinding.txtCantidadCanciones.text = "${post.playlist.canciones.size} canciones"

        itemPlaylistBinding.btnEliminar.visibility = View.GONE
        itemPlaylistBinding.btnAgregarPost.visibility = View.GONE

        itemPlaylistBinding.root.setOnClickListener {
            onPlaylistClick(post.playlist.id)
        }
    }
}