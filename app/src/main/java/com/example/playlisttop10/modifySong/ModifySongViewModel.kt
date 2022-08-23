package com.example.playlisttop10.modifySong

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.playlisttop10.Song
import com.example.playlisttop10.UserRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class ModifySongViewModel : ViewModel() {
    private val songRegistered = MutableLiveData<Boolean>()
    private var errorMessage: String ?= ""

    fun modifySongInformation(song: Song){
        CoroutineScope(Dispatchers.IO).launch{
            val result = UserRepository.modifySongInformation(song)

            errorMessage = if (result.isSuccess){
                songRegistered.postValue(true)
                ""
            }else {
                songRegistered.postValue(false)
                result.exceptionOrNull()?.message
            }
        }
    }
}