package es.tierno.mohamed.aa.mohabeatsiii.ui.view.fragmentos.rv_categorias

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import es.tierno.mohamed.aa.mohabeatsiii.R
import es.tierno.mohamed.aa.mohabeatsiii.data.model.CategoriasModel

class AdapterCategorias(
    private val listaCategorias: List<CategoriasModel>,
    private val onItemClick: (CategoriasModel) -> Unit
) : RecyclerView.Adapter<ViewHolderCategorias>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolderCategorias {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_categorias, parent, false)
        return ViewHolderCategorias(view, onItemClick)
    }

    override fun onBindViewHolder(holder: ViewHolderCategorias, position: Int) {
        holder.render(listaCategorias[position])
    }

    override fun getItemCount(): Int = listaCategorias.size
}