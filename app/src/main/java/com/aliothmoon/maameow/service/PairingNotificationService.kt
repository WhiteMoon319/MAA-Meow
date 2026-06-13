package com.aliothmoon.maameow.service

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.Service
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.IBinder
import androidx.core.app.NotificationCompat
import com.aliothmoon.maameow.MainActivity
import com.aliothmoon.maameow.R
import timber.log.Timber

class PairingNotificationService : Service() {

    companion object {
        const val CHANNEL_ID = "shizuku_pairing"
        const val NOTIFICATION_ID = 2001
        const val ACTION_PAIRING_REQUEST = "com.aliothmoon.maameow.PAIRING_REQUEST"
        const val ACTION_PAIRING_COMPLETE = "com.aliothmoon.maameow.PAIRING_COMPLETE"
        const val EXTRA_PAIRING_CODE = "pairing_code"
        const val EXTRA_PAIRING_PORT = "pairing_port"

        fun startPairingNotification(context: Context, pairingCode: String, pairingPort: Int) {
            val intent = Intent(context, PairingNotificationService::class.java).apply {
                action = ACTION_PAIRING_REQUEST
                putExtra(EXTRA_PAIRING_CODE, pairingCode)
                putExtra(EXTRA_PAIRING_PORT, pairingPort)
            }
            context.startForegroundService(intent)
        }

        fun stopPairingNotification(context: Context) {
            context.stopService(Intent(context, PairingNotificationService::class.java))
        }
    }

    override fun onCreate() {
        super.onCreate()
        createNotificationChannel()
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        when (intent?.action) {
            ACTION_PAIRING_REQUEST -> {
                val pairingCode = intent.getStringExtra(EXTRA_PAIRING_CODE) ?: ""
                val pairingPort = intent.getIntExtra(EXTRA_PAIRING_PORT, 0)
                showPairingNotification(pairingCode, pairingPort)
            }
            ACTION_PAIRING_COMPLETE -> {
                stopForeground(STOP_FOREGROUND_REMOVE)
                stopSelf()
            }
        }
        return START_NOT_STICKY
    }

    override fun onBind(intent: Intent?): IBinder? = null

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                CHANNEL_ID,
                getString(R.string.notification_channel_pairing),
                NotificationManager.IMPORTANCE_HIGH
            ).apply {
                description = getString(R.string.notification_channel_pairing_desc)
                enableVibration(true)
            }
            val notificationManager = getSystemService(NotificationManager::class.java)
            notificationManager.createNotificationChannel(channel)
        }
    }

    private fun showPairingNotification(pairingCode: String, pairingPort: Int) {
        val intent = Intent(this, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            putExtra(EXTRA_PAIRING_CODE, pairingCode)
            putExtra(EXTRA_PAIRING_PORT, pairingPort)
        }

        val pendingIntent = PendingIntent.getActivity(
            this,
            0,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        val notification = NotificationCompat.Builder(this, CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_notification)
            .setContentTitle(getString(R.string.notification_pairing_title))
            .setContentText(getString(R.string.notification_pairing_message, pairingCode))
            .setStyle(NotificationCompat.BigTextStyle()
                .bigText(getString(R.string.notification_pairing_detail, pairingCode, pairingPort)))
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setContentIntent(pendingIntent)
            .setAutoCancel(true)
            .setOngoing(true)
            .build()

        startForeground(NOTIFICATION_ID, notification)
    }
}
