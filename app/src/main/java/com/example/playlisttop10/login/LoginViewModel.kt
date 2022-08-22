package com.example.playlisttop10.login

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.playlisttop10.UserRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class LoginViewModel: ViewModel() {
    private val loggedIn = MutableLiveData<Boolean>()
    private var errorMessage: String ?= ""

    fun tryLogIn(id: String, password: String){
        CoroutineScope(Dispatchers.IO).launch {
            val result = UserRepository.tryLogIn(id, password)

            errorMessage = if (result.isSuccess) {
                loggedIn.postValue(true)
                ""
            } else {
                loggedIn.postValue(false)
                result.exceptionOrNull()?.message
            }
        }
    }

    fun isLoggedIn(): LiveData<Boolean>{
        return loggedIn
    }

    fun getErrorMessage(): String?{
        return errorMessage
    }
}