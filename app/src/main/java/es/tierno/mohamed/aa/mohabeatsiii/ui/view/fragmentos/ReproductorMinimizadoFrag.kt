package es.tierno.mohamed.aa.mohabeatsiii.ui.view.fragmentos

import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.graphics.drawable.GradientDrawable
import android.os.Bundle
import android.os.IBinder
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import es.tierno.mohamed.aa.mohabeatsiii.R
import es.tierno.mohamed.aa.mohabeatsiii.databinding.FragmentReproductorMinimizadoBinding
import es.tierno.mohamed.aa.mohabeatsiii.domain.model.Musica
import es.tierno.mohamed.aa.mohabeatsiii.service.MusicService

class ReproductorMinimizadoFrag : Fragment() {

    private var _binding: FragmentReproductorMinimizadoBinding? = null
    private val binding get() = _binding!!

    private var musicService: MusicService? = null
    private var bound = false

    private var enReproduccion = true

    private val connection = object : ServiceConnection {
        override fun onServiceConnected(name: ComponentName?, service: IBinder?) {
            val binder = service as MusicService.MusicBinder
            musicService = binder.getService()
            bound = true
            observeCurrentSong()
            updatePlayPauseIcon(musicService?.isPlayingLiveData?.value ?: false)
        }

        override fun onServiceDisconnected(name: ComponentName?) {
            bound = false
            musicService = null
        }
    }

    override fun onStart() {
        super.onStart()
        Intent(requireContext(), MusicService::class.java).also { intent ->
            requireContext().bindService(intent, connection, Context.BIND_AUTO_CREATE)
        }
    }

    override fun onStop() {
        super.onStop()
        if (bound) {
            requireContext().unbindService(connection)
            bound = false
        }
    }

    private fun observeCurrentSong() {
        musicService?.currentSongLiveData?.observe(viewLifecycleOwner) { song ->
            song?.let { updateSongInfo(it) }
        }
        musicService?.isPlayingLiveData?.observe(viewLifecycleOwner) { isPlaying ->
            enReproduccion = isPlaying
            updatePlayPauseIcon(isPlaying)
        }
    }

    private fun updateSongInfo(song: Musica) {
        binding.txtTituloMini.text = song.nombreCancion
        binding.txtArtistaMini.text = song.nombreArtista

        Glide.with(requireContext())
            .asBitmap()
            .load(song.urlImagen)
            .placeholder(R.drawable.moha_beats_removebg_preview)
            .into(object : com.bumptech.glide.request.target.CustomTarget<android.graphics.Bitmap>() {
                override fun onResourceReady(
                    resource: android.graphics.Bitmap,
                    transition: com.bumptech.glide.request.transition.Transition<in android.graphics.Bitmap>?
                ) {
                    binding.imgMusicaMini.setImageBitmap(resource)

                    androidx.palette.graphics.Palette.from(resource).generate { palette ->
                        val swatches = palette?.swatches
                            ?.sortedByDescending { it.population }
                            ?.take(2)

                        if (!swatches.isNullOrEmpty()) {
                            val color1 = swatches[0].rgb
                            val color2 = swatches.getOrNull(1)?.rgb ?: color1

                            // Creamos un GradientDrawable con borde y esquinas redondeadas:
                            val gradientDrawable = GradientDrawable(
                                GradientDrawable.Orientation.TOP_BOTTOM,
                                intArrayOf(color1, color2)
                            )
                            gradientDrawable.cornerRadius = 20f * resources.displayMetrics.density // 20dp a px
                            gradientDrawable.setStroke(
                                (3 * resources.displayMetrics.density).toInt(), // 3dp a px
                                0x80FFA500.toInt() // color #80FFA500 (naranja translúcido) hardcodeado
                            )
                            binding.reproductorMinimizado.background = gradientDrawable

                            val textColor = if (isColorLight(color1)) {
                                ContextCompat.getColor(requireContext(), R.color.colorPrincipal)
                            } else {
                                ContextCompat.getColor(requireContext(), android.R.color.white)
                            }
                            binding.txtTituloMini.setTextColor(textColor)
                            binding.txtArtistaMini.setTextColor(ContextCompat.getColor(requireContext(), R.color.colorDetalles))
                        } else {
                            // Si paleta vacía, fondo por defecto
                            binding.reproductorMinimizado.background = ContextCompat.getDrawable(
                                requireContext(), R.drawable.bordes_repproductor_min
                            )
                            val defaultColor = ContextCompat.getColor(requireContext(), R.color.colorPrincipal)
                            binding.txtTituloMini.setTextColor(defaultColor)
                            binding.txtArtistaMini.setTextColor(ContextCompat.getColor(requireContext(), R.color.colorDetalles))
                        }
                    }
                }

                override fun onLoadCleared(placeholder: android.graphics.drawable.Drawable?) {
                    binding.reproductorMinimizado.background = ContextCompat.getDrawable(
                        requireContext(), R.drawable.bordes_repproductor_min
                    )
                    val defaultColor = ContextCompat.getColor(requireContext(), R.color.colorPrincipal)
                    binding.txtTituloMini.setTextColor(defaultColor)
                    binding.txtArtistaMini.setTextColor(defaultColor)
                }
            })
    }

    private fun isColorLight(color: Int): Boolean {
        val r = (color shr 16 and 0xff) / 255.0
        val g = (color shr 8 and 0xff) / 255.0
        val b = (color and 0xff) / 255.0
        val brightness = 0.299 * r + 0.587 * g + 0.114 * b
        return brightness > 0.7
    }

    private fun updatePlayPauseIcon(isPlaying: Boolean) {
        binding.btnPlayPauseMini.setImageResource(
            if (isPlaying) R.drawable.ic_parar else R.drawable.ic_reproducir
        )
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        arguments?.getParcelable<Musica>(ARG_CANCION)?.let { cancion ->
            updateSongInfo(cancion)
        }

        binding.btnPlayPauseMini.setOnClickListener {
            val action = if (enReproduccion) MusicService.ACTION_PAUSE else MusicService.ACTION_PLAY
            Intent(requireContext(), MusicService::class.java).also { intent ->
                intent.action = action
                requireContext().startService(intent)
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentReproductorMinimizadoBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        private const val ARG_CANCION = "cancion"

        fun newInstance(cancion: Musica): ReproductorMinimizadoFrag {
            val fragment = ReproductorMinimizadoFrag()
            val args = Bundle()
            args.putParcelable(ARG_CANCION, cancion)
            fragment.arguments = args
            return fragment
        }
    }
}