package com.example.playlisttop10

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class PlaylistViewModel : ViewModel() {
    private val userRepository: UserRepository = UserRepository()
    private var isReceivedName = MutableLiveData<String>()

    fun tryGetName(id: String) {
        userRepository.tryGetName(id, ::onNameReceived)
    }

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