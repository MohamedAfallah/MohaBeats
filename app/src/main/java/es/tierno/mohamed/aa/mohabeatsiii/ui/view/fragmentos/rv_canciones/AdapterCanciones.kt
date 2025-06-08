package es.tierno.mohamed.aa.mohabeatsiii.ui.view.fragmentos.rv_canciones

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import es.tierno.mohamed.aa.mohabeatsiii.R
import es.tierno.mohamed.aa.mohabeatsiii.domain.model.Musica

class AdapterCanciones(
    private val onReproducirClick: (Musica) -> Unit,
    private val onFavoritoClick: (Musica, Boolean) -> Unit,
    private val onDescargarClick: (Musica) -> Unit,
    private val onAnadirClick: (Musica) -> Unit,
    private val onEliminarClick: (Musica) -> Unit,
    private val mostrarFavorito: Boolean,
    private val mostrarDescargar: Boolean,
    private val mostrarAnadir: Boolean,
    private val mostrarEliminar: Boolean
) : RecyclerView.Adapter<ViewHolderCanciones>() {

    private val cancionesList = mutableListOf<Musica>()
    private var favoritasIds = emptySet<String>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolderCanciones {
        val layoutInflater = LayoutInflater.from(parent.context)
        val view = layoutInflater.inflate(R.layout.item_cancion, parent, false)
        return ViewHolderCanciones(view, onReproducirClick, onFavoritoClick, onDescargarClick, onAnadirClick, onEliminarClick)
    }

    override fun onBindViewHolder(holder: ViewHolderCanciones, position: Int) {
        val item = cancionesList[position]
        val esFavoritaInicial = favoritasIds.contains(item.idCancion)
        holder.render(item, esFavoritaInicial, mostrarFavorito, mostrarDescargar, mostrarAnadir, mostrarEliminar)
    }

    override fun getItemCount(): Int = cancionesList.size

    fun actualizarDatos(nuevasCanciones: List<Musica>, nuevasFavoritasIds: Set<String>) {
        cancionesList.clear()
        cancionesList.addAll(nuevasCanciones)
        favoritasIds = nuevasFavoritasIds
        notifyDataSetChanged()
    }

    fun obtenerDatos(): List<Musica> {
        return cancionesList
    }
}