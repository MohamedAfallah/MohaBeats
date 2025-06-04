package es.tierno.mohamed.aa.mohabeatsiii.ui.view.fragmentos.rv_canciones

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import es.tierno.mohamed.aa.mohabeatsiii.R
import es.tierno.mohamed.aa.mohabeatsiii.domain.model.Musica

class AdapterCanciones(
    private val idUsuario: String, // Recibe el ID de usuario
    private val onReproducirClick: (Musica) -> Unit,
    private val onFavoritoClick: (Musica, Boolean) -> Unit // Nuevo callback: canción y su estado actual (antes del clic)
) : RecyclerView.Adapter<ViewHolderCanciones>() {

    private val cancionesList = mutableListOf<Musica>()
    private var favoritasIds = emptySet<String>() // Para guardar los IDs de canciones favoritas

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolderCanciones {
        val layoutInflater = LayoutInflater.from(parent.context)
        val view = layoutInflater.inflate(R.layout.item_cancion, parent, false)
        // Pasa el nuevo callback al ViewHolder
        return ViewHolderCanciones(view, onReproducirClick, onFavoritoClick)
    }

    override fun onBindViewHolder(holder: ViewHolderCanciones, position: Int) {
        val item = cancionesList[position]
        val esFavoritaInicial = favoritasIds.contains(item.idCancion)
        holder.render(item, esFavoritaInicial) // Pasa el estado inicial
    }

    override fun getItemCount(): Int = cancionesList.size

    fun actualizarDatos(nuevasCanciones: List<Musica>, nuevasFavoritasIds: Set<String>) {
        cancionesList.clear()
        cancionesList.addAll(nuevasCanciones)
        favoritasIds = nuevasFavoritasIds // Actualiza el conjunto de IDs favoritas
        notifyDataSetChanged()
    }

    fun obtenerDatos(): List<Musica> {
        return cancionesList
    }
}


