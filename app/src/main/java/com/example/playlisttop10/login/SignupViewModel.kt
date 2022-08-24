package com.example.playlisttop10.login

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.playlisttop10.UserRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


class SignupViewModel : ViewModel() {
    private val signedUp = MutableLiveData<Boolean>()
    val isSignedUp: LiveData<Boolean> = signedUp

    private var _errorMessage = MutableLiveData<String>()
    val errorMessage: LiveData<String> = _errorMessage

    fun validateInformationForm(id: String, password: String, name: String) = when {
        id.length < 5 -> "id"
        password.length < 4 -> "password"
        name.length < 2 -> "name"
        else -> null
    }

    fun trySignUp(id: String, password: String, name: String) {
        CoroutineScope(Dispatchers.IO).launch {
            val result = UserRepository.trySignUp(id, password, name)

            _errorMessage.postValue(
                if (result.isSuccess) {
                    signedUp.postValue(true)
                    ""
                } else {
                    signedUp.postValue(false)
                    result.exceptionOrNull()?.message
                }
            )
        }
    }
}