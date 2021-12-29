package be.condictum.move_up.util.notification

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build

class NotificationUtil(private var context: Context?) {
    companion object {
        private const val CHANNEL_ID = "ID"
        private const val CHANNEL_NAME = "NAME"

        fun with(context: Context?): NotificationUtil {
            return NotificationUtil(context)
        }
    }

    private fun getNotificationManager(): NotificationManager {
        val manager = context?.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel =
                NotificationChannel(CHANNEL_ID, CHANNEL_NAME, NotificationManager.IMPORTANCE_HIGH)
            channel.setShowBadge(true)
            channel.enableVibration(true)
            channel.enableLights(true)
            manager.createNotificationChannel(channel)
        }
        return manager
    }

    fun showNotification(title: String?, text: String?, icon: Int, resultIntent: Intent) {
        resultIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NO_ANIMATION)
        val pendingIntent =
            PendingIntent.getBroadcast(context, 0, resultIntent, PendingIntent.FLAG_ONE_SHOT)
        val builder: Notification.Builder = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            Notification.Builder(context, CHANNEL_ID)
        } else {
            Notification.Builder(context)
        }
        builder.setAutoCancel(true)
        builder.setSmallIcon(icon)
        builder.setContentTitle(title)
        builder.setContentText(text)
        builder.setContentIntent(pendingIntent)
        getNotificationManager().notify(
            (System.currentTimeMillis() + 1000).toInt(),
            builder.build()
        )
    }
}