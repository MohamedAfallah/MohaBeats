package es.tierno.mohamed.aa.mohabeatsiii.ui.view.fragmentos.rv_canciones_descargadas

import android.animation.Animator
import android.net.Uri
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import es.tierno.mohamed.aa.mohabeatsiii.R
import es.tierno.mohamed.aa.mohabeatsiii.databinding.ItemCancionDescargadaBinding
import es.tierno.mohamed.aa.mohabeatsiii.domain.model.Musica
import java.io.File

class CancionesDescargadasViewHolder(private val binding: ItemCancionDescargadaBinding) :
    RecyclerView.ViewHolder(binding.root) {

    fun bind(musica: Musica, onItemClick: (Musica) -> Unit, onDeleteClick: (Musica) -> Unit) {
        binding.txtNombreCancion.text = musica.nombreCancion
        binding.txtNombreArtista.text = musica.nombreArtista

        val imageSource = if (!musica.rutaLocalImg.isNullOrEmpty() && File(musica.rutaLocalImg!!).exists()) {
            Uri.parse(musica.rutaLocalImg)
        } else {
            musica.urlImagen
        }

        Glide.with(binding.imgCancion.context)
            .load(imageSource)
            .placeholder(R.drawable.ic_launcher_background)
            .error(R.drawable.ic_launcher_foreground)
            .into(binding.imgCancion)

        binding.root.setOnClickListener {
            onItemClick(musica)
        }

        binding.btnEliminar.setOnClickListener {
            binding.btnEliminar.cancelAnimation()
            binding.btnEliminar.playAnimation()

            binding.btnEliminar.addAnimatorListener(object : Animator.AnimatorListener {
                override fun onAnimationStart(animation: Animator) {}

                override fun onAnimationEnd(animation: Animator) {
                    onDeleteClick(musica)
                    binding.btnEliminar.removeAnimatorListener(this)
                }

                override fun onAnimationCancel(animation: Animator) {
                    binding.btnEliminar.removeAnimatorListener(this)
                }

                override fun onAnimationRepeat(animation: Animator) {}
            })
        }
    }
}