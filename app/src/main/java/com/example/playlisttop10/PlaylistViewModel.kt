package com.example.playlisttop10

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class PlaylistViewModel: ViewModel() {
    private val userRepository: UserRepository = UserRepository()
    private var isReceivedName = MutableLiveData<Boolean>()
    private var name: Any ?= ""

    fun tryGetName(id: String){
        userRepository.tryGetName(id, ::onNameReceived)
    }

    private fun onNameReceived(result: Result<Any?>){
        when{
            result.isSuccess -> {
                name = result.getOrNull()
                isReceivedName.value = true
            }
            result.isFailure -> isReceivedName.value = false
        }
    }

    fun getReceivedNameState(): MutableLiveData<Boolean>{
        return isReceivedName
    }

    fun getName(): String{
        return name.toString()
    }
}