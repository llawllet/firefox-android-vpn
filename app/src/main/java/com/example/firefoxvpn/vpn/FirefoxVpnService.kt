package com.example.firefoxvpn.vpn

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.Service
import android.content.Intent
import android.net.VpnService
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat

class FirefoxVpnService : VpnService() {

    companion object {
        private const val SERVICE_NOTIFICATION_ID = 1
        private const val CHANNEL_ID = "firefox_vpn_foreground"
    }

    override fun onCreate() {
        super.onCreate()
        createNotificationChannel()
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        startForeground()
        // TODO: Start VPN connection
        return START_NOT_STICKY
    }

    override fun onDestroy() {
        stopForeground()
        super.onDestroy()
    }

    override fun onBind(intent: Intent?): android.os.IBinder? = null

    private fun startForeground() {
        val stopIntent = Intent(this, FirefoxVpnService::class.java)
        val pendingIntent = PendingIntent.getService(
            this,
            0,
            stopIntent,
            PendingIntent.FLAG_IMMUTABLE
        )

        val notification = NotificationCompat.Builder(this, CHANNEL_ID)
            .setContentTitle("Firefox VPN")
            .setContentText("VPN is active")
            .setSmallIcon(android.R.drawable.ic_lock_idle_lock)
            .addAction(
                android.R.drawable.ic_lock_idle_lock,
                "Disconnect",
                pendingIntent
            )
            .setOngoing(true)
            .build()

        startForeground(SERVICE_NOTIFICATION_ID, notification)
    }

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name = "Firefox VPN Service"
            val descriptionText = "Shows when VPN is active"
            val importance = NotificationManager.IMPORTANCE_LOW
            val channel = NotificationChannel(CHANNEL_ID, name, importance).apply {
                description = descriptionText
            }
            val notificationManager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }
}