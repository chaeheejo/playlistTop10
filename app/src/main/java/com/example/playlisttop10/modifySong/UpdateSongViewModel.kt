package com.example.playlisttop10.modifySong

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.playlisttop10.Song
import com.example.playlisttop10.UserRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class UpdateSongViewModel : ViewModel() {
    private val songUpdated = MutableLiveData<Boolean>()
    private var errorMessage: String? = ""

    fun updateSongInformation(oldSong: Song, newSong: Song) {
        if (newSong.singer.isEmpty() || newSong.album.isEmpty()) {
            errorMessage = "empty field is not allowed"
            songUpdated.value = false
            return
        }

        CoroutineScope(Dispatchers.IO).launch {
            val result = UserRepository.updateSongInformation(oldSong, newSong)

            errorMessage = if (result.isSuccess) {
                songUpdated.postValue(true)
                ""
            } else {
                songUpdated.postValue(false)
                result.exceptionOrNull()?.message
            }
        }
    }

    fun isSongUpdated(): MutableLiveData<Boolean> {
        return songUpdated
    }

    fun getErrorMessage(): String? {
        return errorMessage
    }
}