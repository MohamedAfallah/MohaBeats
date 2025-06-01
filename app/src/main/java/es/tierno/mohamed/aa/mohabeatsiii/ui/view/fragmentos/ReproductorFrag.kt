package es.tierno.mohamed.aa.mohabeatsiii.ui.view.fragmentos

import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.os.Bundle
import android.os.IBinder
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bumptech.glide.Glide
import es.tierno.mohamed.aa.mohabeatsiii.R
import es.tierno.mohamed.aa.mohabeatsiii.databinding.FragmentReproductorBinding
import es.tierno.mohamed.aa.mohabeatsiii.domain.model.Musica
import es.tierno.mohamed.aa.mohabeatsiii.service.MusicService

class ReproductorFrag : Fragment() {

    private var _binding: FragmentReproductorBinding? = null
    private val binding get() = _binding!!

    private var musicService: MusicService? = null
    private var isBound = false

    private val serviceConnection = object : ServiceConnection {
        override fun onServiceConnected(name: ComponentName?, binder: IBinder?) {
            val binderLocal = binder as MusicService.MusicBinder
            musicService = binderLocal.getService()
            isBound = true
            setupObservers()
        }

        override fun onServiceDisconnected(name: ComponentName?) {
            musicService = null
            isBound = false
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentReproductorBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.btnPlayPause.setOnClickListener {
            musicService?.let {
                if (it.isPlayingLiveData.value == true) {
                    it.pause()
                } else {
                    it.play()
                }
            }
        }

        binding.btnSiguiente.setOnClickListener {
            musicService?.next()
        }

        binding.btnAnterior.setOnClickListener {
            musicService?.previous()
        }

        val intent = Intent(requireContext(), MusicService::class.java)
        requireContext().bindService(intent, serviceConnection, Context.BIND_AUTO_CREATE)
    }

    private fun setupObservers() {
        musicService?.currentSongLiveData?.observe(viewLifecycleOwner) { song: Musica? ->
            song?.let {
                binding.txtTituloCancion.text = it.nombreCancion
                binding.txtArtista.text = it.nombreArtista
                Glide.with(this)
                    .load(it.urlImagen)
                    .placeholder(R.drawable.moha_beats_removebg_preview)
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
            isBound = false
        }
        _binding = null
    }
}



