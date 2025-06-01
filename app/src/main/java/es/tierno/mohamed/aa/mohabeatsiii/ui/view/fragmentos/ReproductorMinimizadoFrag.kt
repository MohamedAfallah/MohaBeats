package es.tierno.mohamed.aa.mohabeatsiii.ui.view.fragmentos

import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.os.Bundle
import android.os.IBinder
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
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
        // Bind to MusicService
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
            .load(song.urlImagen)
            .placeholder(R.drawable.moha_beats_removebg_preview)
            .into(binding.imgMusicaMini)
    }

    private fun updatePlayPauseIcon(isPlaying: Boolean) {
        binding.btnPlayPauseMini.setImageResource(
            if (isPlaying) R.drawable.ic_parar else R.drawable.ic_reproducir
        )
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Mostrar canción inicial pasada por argumentos (si hay)
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
