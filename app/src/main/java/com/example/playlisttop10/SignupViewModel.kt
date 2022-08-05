package com.example.playlisttop10

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.firebase.firestore.DocumentSnapshot
import kotlinx.coroutines.*

class SignupViewModel {

    private val isSignedUp = MutableLiveData<Boolean>()
    private val isDuplicated = MutableLiveData<Boolean>()
    private val userRepository: UserRepository = UserRepository()

    fun checkUserInformationFormat(id: String, password: String, name: String) = when {
        id.length < 5 -> "id"
        password.length < 4 -> "password"
        name.length < 2 -> "name"
        else -> null
    }

    fun trySignup(id: String, password: String, name: String) {
        val idList: MutableList<String> = runBlocking {  userRepository.getIdList() }

        if (id !in idList) {
            Log.d("DEBUG ", "trySignup: " + 2 + " " + idList.size)
            userRepository.trySignup(id, password, name, ::onSignupResultReceived)
        } else {
            isDuplicated.value = true
        }
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

    fun getDuplicatedState(): LiveData<Boolean> {
        return isDuplicated
    }
}