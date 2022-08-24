package com.example.playlisttop10.login

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.playlisttop10.UserRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class LoginViewModel : ViewModel() {
    private val loggedIn = MutableLiveData<Boolean>()
    val isLoggedIn: LiveData<Boolean> = loggedIn

    private var _errorMessage = MutableLiveData<String>()
    val errorMessage: LiveData<String> = _errorMessage

    fun tryLogIn(id: String, password: String) {
        CoroutineScope(Dispatchers.IO).launch {
            val result = UserRepository.tryLogIn(id, password)

            _errorMessage.postValue(
                if (result.isSuccess) {
                    loggedIn.postValue(true)
                    ""
                } else {
                    loggedIn.postValue(false)
                    result.exceptionOrNull()?.message
                }
            )
        }
    }
}