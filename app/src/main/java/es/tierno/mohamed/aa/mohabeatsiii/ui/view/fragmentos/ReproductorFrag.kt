package es.tierno.mohamed.aa.mohabeatsiii.ui.view.fragmentos

import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.os.*
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.target.Target
import es.tierno.mohamed.aa.mohabeatsiii.R
import es.tierno.mohamed.aa.mohabeatsiii.databinding.FragmentReproductorBinding
import es.tierno.mohamed.aa.mohabeatsiii.domain.model.Musica
import es.tierno.mohamed.aa.mohabeatsiii.service.MusicService

class ReproductorFrag : Fragment() {

    private var _binding: FragmentReproductorBinding? = null
    private val binding get() = _binding!!

    private var musicService: MusicService? = null
    private var isBound = false

    private val handler = Handler(Looper.getMainLooper())
    private val updateSeekBarRunnable = object : Runnable {
        override fun run() {
            musicService?.let {
                val duration = it.getDuration()
                val position = it.getCurrentPosition()
                if (duration > 0) {
                    binding.seekBar.max = duration
                    binding.seekBar.progress = position
                }
            }
            handler.postDelayed(this, 1000)
        }
    }

    private val serviceConnection = object : ServiceConnection {
        override fun onServiceConnected(name: ComponentName?, binder: IBinder?) {
            val binderLocal = binder as MusicService.MusicBinder
            musicService = binderLocal.getService()
            isBound = true
            setupObservers()
            handler.post(updateSeekBarRunnable)
        }

        override fun onServiceDisconnected(name: ComponentName?) {
            musicService = null
            isBound = false
            handler.removeCallbacks(updateSeekBarRunnable)
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentReproductorBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.btnSalir.setOnClickListener {
            requireActivity().findViewById<View>(R.id.reproductorContainer)?.visibility = View.GONE
            val miniContainer = requireActivity().findViewById<View>(R.id.reproductorMiniContainer)
            miniContainer?.visibility = View.VISIBLE

            musicService?.currentSongLiveData?.value?.let { currentSong ->
                val miniFragment = ReproductorMinimizadoFrag.newInstance(currentSong)
                requireActivity().supportFragmentManager.beginTransaction()
                    .replace(R.id.reproductorMiniContainer, miniFragment)
                    .commit()
            }
            parentFragmentManager.popBackStack()
        }

        binding.btnPlayPause.setOnClickListener {
            musicService?.let {
                if (it.isPlayingLiveData.value == true) it.pause() else it.play()
            }
        }

        binding.btnSiguiente.setOnClickListener {
            musicService?.next()
        }

        binding.btnAnterior.setOnClickListener {
            musicService?.previous()
        }

        binding.seekBar.setOnSeekBarChangeListener(object : android.widget.SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: android.widget.SeekBar?, progress: Int, fromUser: Boolean) {
                if (fromUser) musicService?.seekTo(progress)
            }

            override fun onStartTrackingTouch(seekBar: android.widget.SeekBar?) {}
            override fun onStopTrackingTouch(seekBar: android.widget.SeekBar?) {}
        })

        Intent(requireContext(), MusicService::class.java).also { intent ->
            requireContext().bindService(intent, serviceConnection, Context.BIND_AUTO_CREATE)
        }

        arguments?.getParcelable<Musica>(ARG_CANCION)?.let { cancion ->
            mostrarDatosCancion(cancion)
        }
    }

    private fun mostrarDatosCancion(cancion: Musica) {
        binding.txtTituloCancion.text = cancion.nombreCancion
        binding.txtArtista.text = cancion.nombreArtista

        Glide.with(this)
            .load(cancion.urlImagen)
            .placeholder(R.drawable.moha_beats_removebg_preview)
            .error(R.drawable.moha_beats_removebg_preview)
            .diskCacheStrategy(DiskCacheStrategy.ALL)
            .override(Target.SIZE_ORIGINAL)
            .fitCenter()
            .into(binding.imgAlbumArt)
    }

    private fun setupObservers() {
        musicService?.currentSongLiveData?.observe(viewLifecycleOwner) { song ->
            song?.let {
                binding.txtTituloCancion.text = it.nombreCancion
                binding.txtArtista.text = it.nombreArtista
                Glide.with(this)
                    .load(it.urlImagen)
                    .placeholder(R.drawable.moha_beats_removebg_preview)
                    .error(R.drawable.moha_beats_removebg_preview)
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .override(Target.SIZE_ORIGINAL)
                    .fitCenter()
                    .into(binding.imgAlbumArt)
            }
        }

        musicService?.isPlayingLiveData?.observe(viewLifecycleOwner) { isPlaying ->
            binding.btnPlayPause.setImageResource(
                if (isPlaying) R.drawable.ic_parar else R.drawable.ic_reproducir
            )
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        if (isBound) {
            requireContext().unbindService(serviceConnection)
            handler.removeCallbacks(updateSeekBarRunnable)
            isBound = false
        }
        _binding = null
        requireActivity().findViewById<View>(R.id.reproductorContainer)?.visibility = View.GONE
    }

    companion object {
        private const val ARG_CANCION = "cancion"

        fun newInstance(cancion: Musica): ReproductorFrag {
            val fragment = ReproductorFrag()
            val args = Bundle()
            args.putParcelable(ARG_CANCION, cancion)
            fragment.arguments = args
            return fragment
        }
    }
}