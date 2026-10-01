package sbs.apoorv.clipsy

import android.app.Application
import android.app.NotificationChannel
import android.app.NotificationManager
import sbs.apoorv.clipsy.data.ClipDatabase

class ClipsyApp : Application() {

    val database: ClipDatabase by lazy { ClipDatabase.getInstance(this) }

    override fun onCreate() {
        super.onCreate()
        val channel = NotificationChannel(
            CHANNEL_ID,
            "Clipboard Monitor",
            NotificationManager.IMPORTANCE_LOW
        ).apply {
            description = "Keeps Clipsy running to capture copies"
        }
        val manager = getSystemService(NotificationManager::class.java)
        manager.createNotificationChannel(channel)
    }

    companion object {
        const val CHANNEL_ID = "clipsy_service"
    }
}
