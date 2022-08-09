package com.example.playlisttop10.songregisteration

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.playlisttop10.UserRepository

class PlaylistViewModel : ViewModel() {
    private val userRepository: UserRepository = UserRepository()
    private var isReceivedName = MutableLiveData<String>()


    private fun onNameReceived(result: Result<Any?>) {
        when {
            result.isSuccess -> isReceivedName.value = result.getOrNull().toString()
            result.isFailure -> isReceivedName.value = "name"
        }
    }

    fun getReceivedName(): MutableLiveData<String> {
        return isReceivedName
    }
}