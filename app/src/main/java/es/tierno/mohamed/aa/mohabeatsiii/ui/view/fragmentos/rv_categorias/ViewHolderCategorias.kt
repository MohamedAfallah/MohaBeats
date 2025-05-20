package es.tierno.mohamed.aa.mohabeatsiii.ui.view.fragmentos.rv_categorias

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import es.tierno.mohamed.aa.mohabeatsiii.R
import es.tierno.mohamed.aa.mohabeatsiii.data.model.CategoriasModel

class ViewHolderCategorias(
    view: View,
    private val onItemClick: (CategoriasModel) -> Unit
) : RecyclerView.ViewHolder(view) {

    private val imagenCategoria = view.findViewById<ImageView>(R.id.imgCarta)
    private val nombreCategoria = view.findViewById<TextView>(R.id.nombreCategoria)
    private val color = view.findViewById<View>(R.id.capaColor)
    private val cardView = view.findViewById<CardView>(R.id.itemCategoia)

    fun render(categoria: CategoriasModel) {
        nombreCategoria.text = categoria.nombre
        Glide.with(imagenCategoria.context).load(categoria.urlImagen).into(imagenCategoria)

        color.setBackgroundColor(
            categoria.colorSuperposicion
        )

        cardView.radius = 30f

        itemView.setOnClickListener {
            onItemClick(categoria)
        }
    }
}
