package com.example.playlisttop10.songregistration

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.playlisttop10.Song
import com.example.playlisttop10.SongRepository
import com.example.playlisttop10.UserRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class RegisterSongViewModel: ViewModel() {
    private val songRegistered = MutableLiveData<Boolean>()
    private var errorMessage: String ?= ""

    fun tryRegisterSong(song: Song){
        CoroutineScope(Dispatchers.IO).launch{
            val resultSong = SongRepository.tryRegisterSong(song)

            val key = song.title
            val resultUser = UserRepository.tryRegisterSongTitle(key)

            errorMessage = if (resultSong.isSuccess and resultUser.isSuccess) {
                songRegistered.postValue(true)
                ""
            } else if (resultSong.isFailure) {
                songRegistered.postValue(false)
                resultSong.exceptionOrNull()?.message
            }
            else {
                songRegistered.postValue(false)
                resultUser.exceptionOrNull()?.message
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