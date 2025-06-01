package es.tierno.mohamed.aa.mohabeatsiii.ui.view.helper_views

import android.app.*
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.os.Build
import android.widget.RemoteViews
import androidx.core.app.NotificationCompat
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.transition.Transition
import es.tierno.mohamed.aa.mohabeatsiii.R
import es.tierno.mohamed.aa.mohabeatsiii.domain.model.Musica
import es.tierno.mohamed.aa.mohabeatsiii.service.MusicService

class NotificacionHelper(
    private val context: Context,
    private val service: MusicService
) {

    companion object {
        const val CHANNEL_ID = "MusicServiceChannel"
        const val NOTIFICATION_ID = 1
    }

    fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                CHANNEL_ID,
                "Music Playback",
                NotificationManager.IMPORTANCE_LOW
            )
            val manager = context.getSystemService(NotificationManager::class.java)
            manager.createNotificationChannel(channel)
        }
    }

    private fun createPendingIntent(action: String): PendingIntent {
        val intent = Intent(context, MusicService::class.java).apply { this.action = action }
        return PendingIntent.getService(
            context,
            action.hashCode(),
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )
    }

    fun startForegroundNotification(song: Musica, isPlaying: Boolean) {
        val notification = buildNotification(song, isPlaying)
        service.startForeground(NOTIFICATION_ID, notification)
    }

    private fun buildNotification(song: Musica?, isPlaying: Boolean): Notification {
        val remoteViews = RemoteViews(context.packageName, R.layout.item_notificacion)

        remoteViews.setTextViewText(R.id.notification_song_title, song?.nombreCancion ?: "Desconocida")
        remoteViews.setTextViewText(R.id.notification_artist, song?.nombreArtista ?: "Desconocido")
        remoteViews.setImageViewResource(
            R.id.notification_play_pause,
            if (isPlaying) R.drawable.ic_parar else R.drawable.ic_reproducir
        )

        remoteViews.setOnClickPendingIntent(R.id.notification_previous, createPendingIntent(MusicService.ACTION_PREVIOUS))
        remoteViews.setOnClickPendingIntent(
            R.id.notification_play_pause,
            createPendingIntent(if (isPlaying) MusicService.ACTION_PAUSE else MusicService.ACTION_PLAY)
        )
        remoteViews.setOnClickPendingIntent(R.id.notification_next, createPendingIntent(MusicService.ACTION_NEXT))

        val builder = NotificationCompat.Builder(context, CHANNEL_ID)
            .setSmallIcon(R.drawable.moha_beats_removebg_preview)
            .setCustomContentView(remoteViews)
            .setOnlyAlertOnce(true)
            .setOngoing(isPlaying)
            .setPriority(NotificationCompat.PRIORITY_LOW)

        Glide.with(context)
            .asBitmap()
            .load(song?.urlImagen)
            .into(object : CustomTarget<Bitmap>() {
                override fun onResourceReady(resource: Bitmap, transition: Transition<in Bitmap>?) {
                    remoteViews.setImageViewBitmap(R.id.notification_album_art, resource)
                    val manager = context.getSystemService(NotificationManager::class.java)
                    manager.notify(NOTIFICATION_ID, builder.build())
                }

                override fun onLoadCleared(placeholder: Drawable?) {}
            })

        return builder.build()
    }

    fun cancelNotification() {
        val manager = context.getSystemService(NotificationManager::class.java)
        manager.cancel(NOTIFICATION_ID)
    }
}