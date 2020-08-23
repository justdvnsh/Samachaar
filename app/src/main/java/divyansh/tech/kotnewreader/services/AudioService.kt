package divyansh.tech.kotnewreader.services

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.NotificationManager.IMPORTANCE_LOW
import android.app.PendingIntent
import android.app.PendingIntent.FLAG_UPDATE_CURRENT
import android.content.Context
import android.content.Intent
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import androidx.lifecycle.LifecycleService
import divyansh.tech.kotnewreader.R
import divyansh.tech.kotnewreader.ui.activities.AudioActivity
import divyansh.tech.kotnewreader.utils.Constants.Companion.ACTION_PAUSE_SERVICE
import divyansh.tech.kotnewreader.utils.Constants.Companion.ACTION_START_OR_RESTART_SERVICE
import divyansh.tech.kotnewreader.utils.Constants.Companion.ACTION_STOP_SERVICE
import divyansh.tech.kotnewreader.utils.Constants.Companion.NOTIFICATION_CHANNEL_ID
import divyansh.tech.kotnewreader.utils.Constants.Companion.NOTIFICATION_CHANNEL_NAME
import divyansh.tech.kotnewreader.utils.Constants.Companion.NOTIFICATION_ID

class AudioService: LifecycleService() {

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        intent?.let {
            when(it.action) {
                ACTION_START_OR_RESTART_SERVICE -> {
                    startForegroundService()
                }
                ACTION_STOP_SERVICE -> {
                    Log.i("Service", "Started service")
                }
                ACTION_PAUSE_SERVICE -> {
                    Log.i("Service", "Started service")
                }
                else -> {
                    Log.i("Service", "Started service")
                }
            }
        }
        return super.onStartCommand(intent, flags, startId)
    }

    private fun getAudioActivityPendingIntent() = PendingIntent.getActivity(
        this,
        0,
        Intent(this, AudioActivity::class.java),
        FLAG_UPDATE_CURRENT
    )

    private fun startForegroundService() {
        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            createNotification(notificationManager)
        }

        val builder = NotificationCompat.Builder(this, NOTIFICATION_CHANNEL_ID)
            .setAutoCancel(false)
            .setOngoing(true)
            .setSmallIcon(R.drawable.ic_baseline_headset_24)
            .setContentTitle("Samachaar")
            .setContentText("Samachaar")
            .setContentIntent(getAudioActivityPendingIntent())

        startForeground(NOTIFICATION_ID, builder.build())
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun createNotification(notificationManager: NotificationManager) {
        val channel = NotificationChannel(
            NOTIFICATION_CHANNEL_ID,
            NOTIFICATION_CHANNEL_NAME,
            IMPORTANCE_LOW
        )
        notificationManager.createNotificationChannel(channel)
    }
}