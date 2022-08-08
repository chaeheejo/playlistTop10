package com.example.playlisttop10

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class LoginViewModel: ViewModel() {
    private val userRepository: UserRepository = UserRepository()
    private val isLogin = MutableLiveData<Boolean>()

    fun tryLogIn(id: String, password: String){
        userRepository.tryLogIn(id, password, ::onLoginResultReceived)
    }

    private fun onLoginResultReceived(result: Result<String>){
        when{
            result.isSuccess -> isLogin.value = true
            result.isFailure -> isLogin.value = false
        }
    }

    fun getLoginState(): LiveData<Boolean>{
        return isLogin
    }

}