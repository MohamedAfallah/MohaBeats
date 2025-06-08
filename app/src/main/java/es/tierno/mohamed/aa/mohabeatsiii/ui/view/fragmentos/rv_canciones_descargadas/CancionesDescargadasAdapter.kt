package es.tierno.mohamed.aa.mohabeatsiii.ui.view.fragmentos.rv_canciones_descargadas

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import es.tierno.mohamed.aa.mohabeatsiii.databinding.ItemCancionDescargadaBinding
import es.tierno.mohamed.aa.mohabeatsiii.domain.model.Musica

class CancionesDescargadasAdapter(
    private val onItemClick: (Musica) -> Unit,
    private val onDeleteClick: (Musica) -> Unit
) : ListAdapter<Musica, CancionesDescargadasViewHolder>(DiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CancionesDescargadasViewHolder {
        val binding = ItemCancionDescargadaBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return CancionesDescargadasViewHolder(binding)
    }

    override fun onBindViewHolder(holder: CancionesDescargadasViewHolder, position: Int) {
        val currentItem = getItem(position)
        holder.bind(currentItem, onItemClick, onDeleteClick)
    }

    private class DiffCallback : DiffUtil.ItemCallback<Musica>() {
        override fun areItemsTheSame(oldItem: Musica, newItem: Musica): Boolean {
            return oldItem.idCancion == newItem.idCancion
        }

        override fun areContentsTheSame(oldItem: Musica, newItem: Musica): Boolean {
            return oldItem == newItem
        }
    }
}