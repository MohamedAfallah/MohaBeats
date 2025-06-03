package es.tierno.mohamed.aa.mohabeatsiii.service

import android.app.Service
import android.content.Context
import android.content.Intent
import android.media.AudioAttributes
import android.media.AudioManager
import android.media.MediaPlayer
import android.os.Binder
import android.os.IBinder
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import es.tierno.mohamed.aa.mohabeatsiii.domain.model.Musica
import es.tierno.mohamed.aa.mohabeatsiii.ui.view.helper_views.NotificacionHelper

class MusicService : Service() {

    private val binder = MusicBinder()
    private var mediaPlayer: MediaPlayer? = null
    private var audioManager: AudioManager? = null

    private var playlist: List<Musica> = emptyList()
    private var currentIndex = 0

    private var isPrepared = false
    private var isPaused = false

    private val _currentSongLiveData = MutableLiveData<Musica?>()
    val currentSongLiveData: LiveData<Musica?> get() = _currentSongLiveData

    private val _isPlayingLiveData = MutableLiveData(false)
    val isPlayingLiveData: LiveData<Boolean> get() = _isPlayingLiveData

    private lateinit var notificacionHelper: NotificacionHelper

    companion object {
        const val CHANNEL_ID = "MusicServiceChannel"
        private const val TAG = "MusicService"

        const val ACTION_PLAY = "es.tierno.mohamed.aa.mohabeatsiii.ACTION_PLAY"
        const val ACTION_PAUSE = "es.tierno.mohamed.aa.mohabeatsiii.ACTION_PAUSE"
        const val ACTION_NEXT = "es.tierno.mohamed.aa.mohabeatsiii.ACTION_NEXT"
        const val ACTION_PREVIOUS = "es.tierno.mohamed.aa.mohabeatsiii.ACTION_PREVIOUS"

        const val EXTRA_URL = "es.tierno.mohamed.aa.mohabeatsiii.EXTRA_URL"
        const val EXTRA_PLAYLIST = "es.tierno.mohamed.aa.mohabeatsiii.EXTRA_PLAYLIST"
        const val EXTRA_START_INDEX = "EXTRA_START_INDEX"
    }

    inner class MusicBinder : Binder() {
        fun getService(): MusicService = this@MusicService
    }

    override fun onBind(intent: Intent?): IBinder = binder

    override fun onCreate() {
        super.onCreate()
        Log.d(TAG, "Service created")
        notificacionHelper = NotificacionHelper(this, this)
        notificacionHelper.createNotificationChannel()

        audioManager = getSystemService(Context.AUDIO_SERVICE) as AudioManager
        requestBluetoothAudio()
    }

    private fun requestBluetoothAudio() {
        audioManager?.let { am ->
            @Suppress("DEPRECATION")
            am.mode = AudioManager.MODE_NORMAL
            am.isSpeakerphoneOn = false
            am.startBluetoothSco()
            am.isBluetoothScoOn = true
        }
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        intent?.action?.let { action ->
            Log.d(TAG, "Received action: $action")
            when (action) {
                ACTION_PLAY -> {
                    val list = intent.getParcelableArrayListExtra<Musica>(EXTRA_PLAYLIST)
                    val url = intent.getStringExtra(EXTRA_URL)
                    val startIndex = intent.getIntExtra(EXTRA_START_INDEX, 0)

                    when {
                        list != null -> setPlaylist(list, startIndex.coerceIn(0, list.size - 1))
                        url != null -> setPlaylist(
                            listOf(
                                Musica(
                                    idCancion = 0,
                                    nombreCancion = "Desconocida",
                                    idArtista = 0,
                                    nombreArtista = "Desconocido",
                                    idAlbum = null,
                                    nombreAlbum = null,
                                    urlImagen = "",
                                    urlPreview = url,
                                    genero = "",
                                    duracionMillis = 0,
                                    fechaLanzamiento = ""
                                )
                            ), 0
                        )
                        else -> play()
                    }
                }
                ACTION_PAUSE -> pause()
                ACTION_NEXT -> next()
                ACTION_PREVIOUS -> previous()
                else -> Log.w(TAG, "Unknown action received: $action")
            }
        }
        return START_STICKY
    }

    fun setPlaylist(list: List<Musica>, startIndex: Int) {
        playlist = list
        currentIndex = startIndex
        playCurrent()
    }

    private fun playCurrent() {
        val song = playlist.getOrNull(currentIndex)
        if (song == null || song.urlPreview.isNullOrEmpty()) {
            Log.w(TAG, "Invalid song or missing URL. Stopping playback.")
            stop()
            return
        }

        _currentSongLiveData.postValue(song)

        mediaPlayer?.apply {
            stop()
            reset()
            release()
        }
        mediaPlayer = null
        isPrepared = false
        isPaused = false

        mediaPlayer = MediaPlayer().apply {
            try {
                setAudioAttributes(
                    AudioAttributes.Builder()
                        .setUsage(AudioAttributes.USAGE_MEDIA)
                        .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
                        .build()
                )
                setDataSource(song.urlPreview)
                setOnPreparedListener {
                    isPrepared = true
                    start()
                    isPaused = false
                    _isPlayingLiveData.postValue(true)
                    notificacionHelper.startForegroundNotification(song, true)
                }
                setOnCompletionListener { next() }
                setOnErrorListener { _, what, extra ->
                    Log.e(TAG, "MediaPlayer error what=$what extra=$extra")
                    true
                }
                prepareAsync()
            } catch (e: Exception) {
                Log.e(TAG, "Error preparing MediaPlayer: ${e.message}")
            }
        }
    }

    fun play() {
        when {
            mediaPlayer == null -> playCurrent()
            isPrepared && isPaused -> {
                mediaPlayer?.start()
                isPaused = false
                _isPlayingLiveData.postValue(true)
                playlist.getOrNull(currentIndex)?.let {
                    notificacionHelper.startForegroundNotification(it, true)
                }
            }
            !isPrepared -> playCurrent()
        }
    }

    fun pause() {
        if (mediaPlayer?.isPlaying == true) {
            mediaPlayer?.pause()
            isPaused = true
            _isPlayingLiveData.postValue(false)
            playlist.getOrNull(currentIndex)?.let {
                notificacionHelper.startForegroundNotification(it, false)
            }
        }
    }

    fun stop() {
        mediaPlayer?.apply {
            if (isPlaying) stop()
            reset()
            release()
        }
        mediaPlayer = null
        isPrepared = false
        isPaused = false
        _isPlayingLiveData.postValue(false)

        audioManager?.let {
            it.stopBluetoothSco()
            it.isBluetoothScoOn = false
        }

        notificacionHelper.cancelNotification()
        stopSelf()
    }

    fun next() {
        if (playlist.isNotEmpty()) {
            currentIndex = (currentIndex + 1) % playlist.size
            playCurrent()
        }
    }

    fun previous() {
        if (playlist.isNotEmpty()) {
            currentIndex = if (currentIndex - 1 < 0) playlist.size - 1 else currentIndex - 1
            playCurrent()
        }
    }

    fun getDuration(): Int = mediaPlayer?.duration ?: 0

    fun getCurrentPosition(): Int = mediaPlayer?.currentPosition ?: 0

    fun seekTo(position: Int) {
        mediaPlayer?.seekTo(position)
    }

    override fun onDestroy() {
        stop()
        super.onDestroy()
    }

    override fun onTaskRemoved(rootIntent: Intent?) {
        stop()
        super.onTaskRemoved(rootIntent)
    }
}