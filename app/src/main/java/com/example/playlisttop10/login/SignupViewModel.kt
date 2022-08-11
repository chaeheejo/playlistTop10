package com.example.playlisttop10.login

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.playlisttop10.User
import com.example.playlisttop10.UserRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


class SignupViewModel : ViewModel() {
    private val signedUp = MutableLiveData<Boolean>()
    private var errorMessage: String ?= ""

    fun validateInformationForm(id: String, password: String, name: String) = when {
        id.length < 5 -> "id"
        password.length < 4 -> "password"
        name.length < 2 -> "name"
        else -> null
    }

    fun trySignUp(id: String, password: String, name: String) {
        CoroutineScope(Dispatchers.IO).launch {
            val result = UserRepository.trySignUp(id, password, name)

            errorMessage = if (result.isSuccess) {
                signedUp.postValue(true)
                ""
            } else {
                signedUp.postValue(false)
                result.exceptionOrNull()?.message
            }
        }
    }

    fun isSignedUp(): LiveData<Boolean> {
        return signedUp
    }

    fun getErrorMessage(): String?{
        return errorMessage
    }
}