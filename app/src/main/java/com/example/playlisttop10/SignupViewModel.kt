package com.example.playlisttop10

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData

class SignupViewModel {

    private val isSignedUp = MutableLiveData<Boolean>()
    private val userRepository: UserRepository = UserRepository()
    private lateinit var userIdList: MutableList<String>

    fun checkUserInformationFormat(id: String, password: String, name: String) = when {
        id.length < 5 -> "id"
        password.length < 4 -> "password"
        name.length < 2 -> "name"
        else -> null
    }

    fun trySignup(id: String, password: String, name: String) {
        userIdList = userRepository.getUerList()

        if () {

        }
        userRepository.trySignup(id, password, name, ::onSignupResultReceived)
    }


    private fun onSignupResultReceived(result: Result<String>) {
        when {
            result.isSuccess -> isSignedUp.value = true
            result.isFailure -> isSignedUp.value = false
        }
    }

    fun getSignupState(): LiveData<Boolean> {
        return isSignedUp
    }
}