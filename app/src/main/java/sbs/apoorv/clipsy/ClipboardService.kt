package sbs.apoorv.clipsy

import android.app.PendingIntent
import android.app.Service
import android.content.ClipboardManager
import android.content.Intent
import android.os.IBinder
import androidx.core.app.NotificationCompat
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch
import sbs.apoorv.clipsy.data.ClipEntity

class ClipboardService : Service() {

    private val scope = CoroutineScope(Dispatchers.IO + SupervisorJob())
    private var clipboardManager: ClipboardManager? = null
    private var lastClip: String? = null

    private val clipListener = ClipboardManager.OnPrimaryClipChangedListener {
        val clip = clipboardManager?.primaryClip
        if (clip != null && clip.itemCount > 0) {
            val text = clip.getItemAt(0).coerceToText(this).toString()
            if (text.isNotBlank() && text != lastClip) {
                lastClip = text
                saveClip(text)
            }
        }
    }

    override fun onCreate() {
        super.onCreate()
        clipboardManager = getSystemService(ClipboardManager::class.java)
        clipboardManager?.addPrimaryClipChangedListener(clipListener)
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        val openIntent = Intent(this, MainActivity::class.java)
        val pendingIntent = PendingIntent.getActivity(
            this, 0, openIntent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        val notification = NotificationCompat.Builder(this, ClipsyApp.CHANNEL_ID)
            .setContentTitle("Clipsy")
            .setContentText("Monitoring clipboard")
            .setSmallIcon(android.R.drawable.ic_menu_edit)
            .setContentIntent(pendingIntent)
            .setOngoing(true)
            .build()

        startForeground(1, notification)
        return START_STICKY
    }

    private fun saveClip(text: String) {
        val db = (application as ClipsyApp).database
        scope.launch {
            db.clipDao().insert(
                ClipEntity(text = text, timestamp = System.currentTimeMillis())
            )
        }
    }

    override fun onDestroy() {
        clipboardManager?.removePrimaryClipChangedListener(clipListener)
        scope.cancel()
        super.onDestroy()
    }

    override fun onBind(intent: Intent?): IBinder? = null
}
