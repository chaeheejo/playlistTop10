package com.example.playlisttop10.modifySong

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.playlisttop10.Song
import com.example.playlisttop10.UserRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class RegisterSongViewModel : ViewModel() {
    private val songRegistered = MutableLiveData<Boolean>()
    val isSongRegistered: LiveData<Boolean> = songRegistered

    private var _errorMessage = MutableLiveData<String>()
    val errorMessage: LiveData<String> = _errorMessage

    fun tryRegisterSong(song: Song) {
        CoroutineScope(Dispatchers.IO).launch {
            val result = UserRepository.tryRegisterMySong(song)

            _errorMessage.postValue(
                if (result.isSuccess) {
                    songRegistered.postValue(true)
                    ""
                } else {
                    songRegistered.postValue(false)
                    result.exceptionOrNull()?.message
                }
            )
        }
    }
}