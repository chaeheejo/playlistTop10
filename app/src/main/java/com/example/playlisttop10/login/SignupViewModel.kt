package com.example.playlisttop10.login

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.playlisttop10.UserRepository


class SignupViewModel : ViewModel() {
    private val isSignedUp = MutableLiveData<Boolean>()
    private val isReceivedIdList = MutableLiveData<Boolean>()
    private val userRepository: UserRepository = UserRepository()
    private var idList: List<String> = emptyList()

    fun checkUserInformationFormat(id: String, password: String, name: String) = when {
        id.length < 5 -> "id"
        password.length < 4 -> "password"
        name.length < 2 -> "name"
        else -> null
    }

    fun trySignup(id: String, password: String, name: String) {
        userRepository.trySignup(id, password, name, ::onSignupResultReceived)
    }

    fun tryGetIdList(){
        userRepository.tryGetIdList(::onIdListReceived)
    }

    private fun onIdListReceived(result: Result<List<String>>) {
        when {
            result.isSuccess -> {
                idList = result.getOrDefault(emptyList())
                isReceivedIdList.value = true
            }
            result.isFailure -> isReceivedIdList.value = false
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

    fun getDuplicatedState(): MutableLiveData<Boolean> {
        return isReceivedIdList
    }

    fun getIdList(): List<String>{
        return idList
    }
}