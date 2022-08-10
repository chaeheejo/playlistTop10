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

    fun validateInformationForm(user: User) = when {
        user.id.length < 5 -> "id"
        user.password.length < 4 -> "password"
        user.name.length < 2 -> "name"
        else -> null
    }

    fun trySignUp(user: User) {
        CoroutineScope(Dispatchers.IO).launch {
            val result = UserRepository.trySignUp(user)

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