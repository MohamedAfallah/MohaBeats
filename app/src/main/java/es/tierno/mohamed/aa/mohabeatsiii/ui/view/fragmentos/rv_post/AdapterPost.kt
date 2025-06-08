package es.tierno.mohamed.aa.mohabeatsiii.ui.view.fragmentos.rv_post

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import es.tierno.mohamed.aa.mohabeatsiii.databinding.ItemPostBinding
import es.tierno.mohamed.aa.mohabeatsiii.domain.model.PostConPlaylist

class AdapterPost(
    private val onPlaylistClick: (playlistId: String) -> Unit
) : ListAdapter<PostConPlaylist, ViewHolderPost>(PostDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolderPost {
        val binding = ItemPostBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolderPost(binding, onPlaylistClick)
    }

    override fun onBindViewHolder(holder: ViewHolderPost, position: Int) {
        val post = getItem(position)
        holder.bind(post)
    }

    private class PostDiffCallback : DiffUtil.ItemCallback<PostConPlaylist>() {
        override fun areItemsTheSame(oldItem: PostConPlaylist, newItem: PostConPlaylist): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: PostConPlaylist, newItem: PostConPlaylist): Boolean {
            return oldItem == newItem
        }
    }
}
