package es.tierno.mohamed.aa.mohabeatsiii.ui.view.fragmentos.recyclerView

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import es.tierno.mohamed.aa.mohabeatsiii.R
import es.tierno.mohamed.aa.mohabeatsiii.domain.model.Musica

class ViewHolder(view : View): RecyclerView.ViewHolder(view){
    val imagen = view.findViewById<ImageView>(R.id.imagenCancion)
    val nombre = view.findViewById<TextView>(R.id.nombreCancion)
    val artista = view.findViewById<TextView>(R.id.nombreArtista)

    fun render(cancion : Musica){
        nombre.text = cancion.nombre
        artista.text = cancion.artista
        Glide.with(imagen.context).load(cancion.url).into(imagen)
    }
}