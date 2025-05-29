package es.tierno.mohamed.aa.mohabeatsiii.ui.view.fragmentos.rv_canciones

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import es.tierno.mohamed.aa.mohabeatsiii.R
import es.tierno.mohamed.aa.mohabeatsiii.domain.model.Musica

class AdapterCanciones(val cancionesList : List<Musica>) : RecyclerView.Adapter<ViewHolderCanciones>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolderCanciones {
        val layoutInflater = LayoutInflater.from(parent.context)
        return ViewHolderCanciones(layoutInflater.inflate(R.layout.item_cancion, parent, false))
    }

    override fun onBindViewHolder(holder: ViewHolderCanciones, position: Int) {
        val item = cancionesList[position]
        holder.render(item)
    }

    override fun getItemCount(): Int =  cancionesList.size
}