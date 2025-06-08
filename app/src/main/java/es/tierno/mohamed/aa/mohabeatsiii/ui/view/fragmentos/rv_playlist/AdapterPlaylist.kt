package es.tierno.mohamed.aa.mohabeatsiii.ui.view.fragmentos.rv_playlist

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import es.tierno.mohamed.aa.mohabeatsiii.R
import es.tierno.mohamed.aa.mohabeatsiii.domain.model.Playlist

class AdapterPlaylist(
    private var playlists: List<Playlist>,
    private val onItemClick: (Playlist) -> Unit,
    private val onEliminarClick: ((Playlist) -> Unit)? = null,
    private val onAgregarPostClick: ((Playlist) -> Unit)? = null,
    private val showEliminarButton: Boolean = false,
    private val showAgregarPostButton: Boolean = false
) : RecyclerView.Adapter<ViewHolderPlaylist>() {

    constructor(onItemClick: (Playlist) -> Unit) : this(
        emptyList(),
        onItemClick,
        null,
        null,
        false,
        false
    )

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolderPlaylist {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_playlist, parent, false)
        return ViewHolderPlaylist(view)
    }

    override fun onBindViewHolder(holder: ViewHolderPlaylist, position: Int) {
        val item = playlists[position]
        holder.render(
            item,
            onItemClick,
            onEliminarClick,
            onAgregarPostClick,
            showEliminarButton,
            showAgregarPostButton
        )
    }

    override fun getItemCount(): Int = playlists.size

    fun actualizarListas(nuevasListas: List<Playlist>) {
        this.playlists = nuevasListas
        notifyDataSetChanged()
    }
}