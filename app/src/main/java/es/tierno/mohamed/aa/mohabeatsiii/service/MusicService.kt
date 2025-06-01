package es.tierno.mohamed.aa.mohabeatsiii.service

import android.app.*
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.media.AudioAttributes
import android.media.MediaPlayer
import android.os.Binder
import android.os.Build
import android.os.IBinder
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.transition.Transition
import es.tierno.mohamed.aa.mohabeatsiii.R
import es.tierno.mohamed.aa.mohabeatsiii.domain.model.Musica
import android.widget.RemoteViews

class MusicService : Service() {

    private val binder = MusicBinder()
    private var mediaPlayer: MediaPlayer? = null

    private var playlist: List<Musica> = emptyList()
    private var currentIndex = 0

    private val _currentSongLiveData = MutableLiveData<Musica?>()
    val currentSongLiveData: LiveData<Musica?> get() = _currentSongLiveData

    private val _isPlayingLiveData = MutableLiveData(false)
    val isPlayingLiveData: LiveData<Boolean> get() = _isPlayingLiveData

    companion object {
        const val CHANNEL_ID = "MusicServiceChannel"
        private const val TAG = "MusicService"

        const val ACTION_PLAY = "es.tierno.mohamed.aa.mohabeatsiii.ACTION_PLAY"
        const val ACTION_PAUSE = "es.tierno.mohamed.aa.mohabeatsiii.ACTION_PAUSE"
        const val ACTION_NEXT = "es.tierno.mohamed.aa.mohabeatsiii.ACTION_NEXT"
        const val ACTION_PREVIOUS = "es.tierno.mohamed.aa.mohabeatsiii.ACTION_PREVIOUS"

        const val EXTRA_URL = "es.tierno.mohamed.aa.mohabeatsiii.EXTRA_URL"
        const val EXTRA_PLAYLIST = "es.tierno.mohamed.aa.mohabeatsiii.EXTRA_PLAYLIST"
    }

    inner class MusicBinder : Binder() {
        fun getService(): MusicService = this@MusicService
    }

    override fun onBind(intent: Intent?): IBinder? {
        Log.d(TAG, "onBind called")
        return binder
    }

    override fun onCreate() {
        super.onCreate()
        Log.d(TAG, "Service created")
        createNotificationChannel()
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        intent?.action?.let { action ->
            when (action) {
                ACTION_PLAY -> {
                    val list = intent.getParcelableArrayListExtra<Musica>(EXTRA_PLAYLIST)
                    val url = intent.getStringExtra(EXTRA_URL)
                    val startIndex = intent.getIntExtra("EXTRA_START_INDEX", 0) // aquí sacamos el índice

                    if (list != null) {
                        setPlaylist(list, startIndex.coerceIn(0, list.size - 1))
                    } else if (url != null) {
                        setPlaylist(listOf(
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
                        ), 0)
                    } else {
                        play()
                    }
                }
                ACTION_PAUSE -> pause()
                ACTION_NEXT -> next()
                ACTION_PREVIOUS -> previous()
            }
        }
        return START_STICKY
    }

    fun setPlaylist(list: List<Musica>, startIndex: Int) {
        playlist = list
        currentIndex = startIndex.coerceIn(0, playlist.size - 1)
        playCurrent()
    }

    private fun playCurrent() {
        val song = playlist.getOrNull(currentIndex)
        if (song == null || song.urlPreview.isNullOrEmpty()) {
            stop()
            return
        }
        _currentSongLiveData.postValue(song)
        playUrl(song.urlPreview)
    }

    private fun playUrl(url: String) {
        mediaPlayer?.release()
        mediaPlayer = MediaPlayer().apply {
            try {
                setAudioAttributes(
                    AudioAttributes.Builder()
                        .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
                        .build()
                )
                setDataSource(url)
                setOnPreparedListener {
                    start()
                    _isPlayingLiveData.postValue(true)
                    startForegroundNotification()
                }
                setOnCompletionListener {
                    next()
                }
                setOnErrorListener { _, what, extra ->
                    Log.e(TAG, "MediaPlayer error what=$what extra=$extra")
                    true
                }
                prepareAsync()
            } catch (e: Exception) {
                Log.e(TAG, "Error preparing MediaPlayer: ${e.message}")
                e.printStackTrace()
            }
        }
    }

    fun play() {
        if (mediaPlayer == null && playlist.isNotEmpty()) {
            playCurrent()
        } else if (mediaPlayer?.isPlaying == false) {
            mediaPlayer?.start()
            _isPlayingLiveData.postValue(true)
            startForegroundNotification()
        }
    }

    fun pause() {
        if (mediaPlayer?.isPlaying == true) {
            mediaPlayer?.pause()
            _isPlayingLiveData.postValue(false)
            startForegroundNotification()
        }
    }

    fun stop() {
        mediaPlayer?.stop()
        mediaPlayer?.release()
        mediaPlayer = null
        _isPlayingLiveData.postValue(false)
        stopForeground(true)
        stopSelf()
    }

    fun next() {
        if (playlist.isEmpty()) return
        currentIndex = (currentIndex + 1) % playlist.size
        playCurrent()
    }

    fun previous() {
        if (playlist.isEmpty()) return
        currentIndex = if (currentIndex - 1 < 0) playlist.size - 1 else currentIndex - 1
        playCurrent()
    }

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                CHANNEL_ID,
                "Music Playback",
                NotificationManager.IMPORTANCE_LOW
            )
            val manager = getSystemService(NotificationManager::class.java)
            manager.createNotificationChannel(channel)
        }
    }

    private fun createPendingIntent(action: String): PendingIntent {
        val intent = Intent(this, MusicService::class.java).apply {
            this.action = action
        }
        return PendingIntent.getService(
            this,
            action.hashCode(),
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )
    }

    private fun startForegroundNotification() {
        val currentSong = playlist.getOrNull(currentIndex)
        val isPlaying = _isPlayingLiveData.value == true

        val remoteViews = RemoteViews(packageName, R.layout.item_notificacion)

        // Set text
        remoteViews.setTextViewText(R.id.notification_song_title, currentSong?.nombreCancion ?: "Desconocida")
        remoteViews.setTextViewText(R.id.notification_artist, currentSong?.nombreArtista ?: "Desconocido")

        // Set play/pause button
        remoteViews.setImageViewResource(
            R.id.notification_play_pause,
            if (isPlaying) R.drawable.ic_parar else R.drawable.ic_reproducir
        )

        // Set actions
        remoteViews.setOnClickPendingIntent(R.id.notification_previous, createPendingIntent(ACTION_PREVIOUS))
        remoteViews.setOnClickPendingIntent(
            R.id.notification_play_pause,
            createPendingIntent(if (isPlaying) ACTION_PAUSE else ACTION_PLAY)
        )
        remoteViews.setOnClickPendingIntent(R.id.notification_next, createPendingIntent(ACTION_NEXT))

        // Build the base notification (placeholder image first)
        val builder = NotificationCompat.Builder(this, CHANNEL_ID)
            .setSmallIcon(R.drawable.moha_beats_removebg_preview)
            .setCustomContentView(remoteViews)
            .setOnlyAlertOnce(true)
            .setOngoing(isPlaying)

        startForeground(1, builder.build())

        // Load the image asynchronously using Glide
        Glide.with(this)
            .asBitmap()
            .load(currentSong?.urlImagen)
            .into(object : CustomTarget<Bitmap>() {
                override fun onResourceReady(resource: Bitmap, transition: Transition<in Bitmap>?) {
                    remoteViews.setImageViewBitmap(R.id.notification_album_art, resource)

                    // Update notification with the new image
                    val updatedNotification = builder.build()
                    val notificationManager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager
                    notificationManager.notify(1, updatedNotification)
                }

                override fun onLoadCleared(placeholder: Drawable?) {}
            })
    }



    override fun onDestroy() {
        mediaPlayer?.release()
        mediaPlayer = null
        super.onDestroy()
    }
}



