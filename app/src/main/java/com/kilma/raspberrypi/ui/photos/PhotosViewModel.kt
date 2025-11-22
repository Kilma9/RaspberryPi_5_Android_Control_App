package com.kilma.raspberrypi.ui.photos

import android.content.Context
import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kilma.raspberrypi.api.ImmichApiClient
import kotlinx.coroutines.launch

class PhotosViewModel : ViewModel() {

    private val _statusText = MutableLiveData<String>().apply {
        value = "Not connected\nEnter Immich server URL and API key to begin."
    }
    val statusText: LiveData<String> = _statusText

    private val _uploadProgress = MutableLiveData<Pair<Int, Int>?>()
    val uploadProgress: LiveData<Pair<Int, Int>?> = _uploadProgress

    private val _uploadButtonEnabled = MutableLiveData<Boolean>().apply { value = false }
    val uploadButtonEnabled: LiveData<Boolean> = _uploadButtonEnabled

    private val apiClient = ImmichApiClient()
    private var selectedPhotos = listOf<Uri>()
    private var isConnected = false
    private var autoBackupEnabled = false

    fun connect(serverUrl: String, apiKey: String) {
        appendToStatus("\n> Connecting to $serverUrl...")

        apiClient.configure(serverUrl, apiKey)

        viewModelScope.launch {
            val result = apiClient.testConnection()
            if (result.isSuccess) {
                isConnected = true
                val info = result.getOrNull()
                appendToStatus("> Connected successfully!")
                appendToStatus("> Server: ${info ?: "Immich"}")
                appendToStatus("> Ready to upload photos.\n")
            } else {
                isConnected = false
                appendToStatus("> ERROR: ${result.exceptionOrNull()?.message}")
                appendToStatus("> Connection failed.\n")
            }
        }
    }

    fun setSelectedPhotos(uris: List<Uri>) {
        selectedPhotos = uris
        _uploadButtonEnabled.value = uris.isNotEmpty() && isConnected
        appendToStatus("\n> ${uris.size} photo(s) selected for upload")
    }

    fun uploadPhotos(context: Context) {
        if (!isConnected) {
            appendToStatus("\n> ERROR: Not connected to Immich server")
            return
        }

        if (selectedPhotos.isEmpty()) {
            appendToStatus("\n> ERROR: No photos selected")
            return
        }

        appendToStatus("\n> Starting upload of ${selectedPhotos.size} photo(s)...")
        _uploadProgress.value = Pair(0, selectedPhotos.size)

        viewModelScope.launch {
            var successCount = 0
            selectedPhotos.forEachIndexed { index, uri ->
                val result = apiClient.uploadPhoto(context, uri)
                if (result.isSuccess) {
                    successCount++
                    appendToStatus("> Uploaded ${index + 1}/${selectedPhotos.size}")
                } else {
                    appendToStatus("> Failed ${index + 1}/${selectedPhotos.size}: ${result.exceptionOrNull()?.message}")
                }
                _uploadProgress.postValue(Pair(index + 1, selectedPhotos.size))
            }

            appendToStatus("> Upload complete: $successCount/${selectedPhotos.size} successful\n")
            _uploadProgress.postValue(null)
            selectedPhotos = emptyList()
            _uploadButtonEnabled.postValue(false)
        }
    }

    fun setAutoBackup(enabled: Boolean) {
        autoBackupEnabled = enabled
        if (enabled) {
            appendToStatus("\n> Auto backup enabled (WiFi only)")
            appendToStatus("> Photos will be automatically backed up when connected to WiFi")
        } else {
            appendToStatus("\n> Auto backup disabled")
        }
    }

    private fun appendToStatus(text: String) {
        _statusText.value = _statusText.value + "\n" + text
    }
}
