package sbs.apoorv.clipsy.ui

import android.app.Application
import android.content.ClipData
import android.content.ClipboardManager
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import sbs.apoorv.clipsy.ClipsyApp
import sbs.apoorv.clipsy.data.ClipEntity

class ClipsyViewModel(app: Application) : AndroidViewModel(app) {

    private val dao = (app as ClipsyApp).database.clipDao()

    val clips = dao.getAll().stateIn(
        viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList()
    )

    fun copyToClipboard(text: String) {
        val cm = getApplication<ClipsyApp>().getSystemService(ClipboardManager::class.java)
        cm.setPrimaryClip(ClipData.newPlainText("Clipsy", text))
    }

    fun updateClip(clip: ClipEntity, newText: String) {
        viewModelScope.launch {
            dao.update(clip.copy(text = newText))
        }
    }

    fun deleteClip(clip: ClipEntity) {
        viewModelScope.launch {
            dao.delete(clip)
        }
    }

    fun clearAll() {
        viewModelScope.launch {
            dao.deleteAll()
        }
    }
}
