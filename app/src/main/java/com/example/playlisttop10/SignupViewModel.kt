package com.example.playlisttop10

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.firebase.firestore.DocumentSnapshot
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.collect

class SignupViewModel {
    private var idList: List<String> = arrayListOf()
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
        CoroutineScope(Dispatchers.IO).launch {
            val two = async { idList = userRepository.getIdList() }

            val one = async {
                Log.d("debug", "trySignup: " + 3 + idList.size)
                if (id !in idList) {
                    Log.d("debug", "trySignup: " + 4 + idList.size)
                    userRepository.trySignup(id, password, name, ::onSignupResultReceived)
                } else {
                    isDuplicated.postValue(true)
                }
            }
            two.await()
            one.await()


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