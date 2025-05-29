package es.tierno.mohamed.aa.mohabeatsiii.ui.view.fragmentos.rv_playlist

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import es.tierno.mohamed.aa.mohabeatsiii.R
import es.tierno.mohamed.aa.mohabeatsiii.data.model.PlaylistModel
import es.tierno.mohamed.aa.mohabeatsiii.ui.view.fragmentos.rv_playlist.ViewHolderPlaylist
class AdapterPlaylist(
    private val playlists: List<PlaylistModel>,
) : RecyclerView.Adapter<ViewHolderPlaylist>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolderPlaylist {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_playlist, parent, false)
        return ViewHolderPlaylist(view)
    }

    override fun onBindViewHolder(holder: ViewHolderPlaylist, position: Int) {
        val item = playlists[position]
        holder.render(item)
    }

    override fun getItemCount(): Int = playlists.size
}
