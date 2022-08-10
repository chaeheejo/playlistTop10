package com.example.playlisttop10.songregisteration

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.playlisttop10.Song
import com.example.playlisttop10.UserRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class RegisterSongViewModel: ViewModel() {
    private val songRegistered = MutableLiveData<Boolean>()
    private var errorMessage: String ?= ""

    fun tryRegisterSong(song: Song){
        CoroutineScope(Dispatchers.IO).launch{
            val result = UserRepository.tryRegisterSong(song)

            errorMessage = if (result.isSuccess) {
                songRegistered.postValue(true)
                ""
            } else {
                songRegistered.postValue(false)
                result.exceptionOrNull()?.message
            }
        }
    }

    fun isSongRegistered(): MutableLiveData<Boolean> {
        return songRegistered
    }

    fun getErrorMessage(): String?{
        return errorMessage
    }
}