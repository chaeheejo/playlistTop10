package com.example.playlisttop10

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData

class SignupViewModel {
    private lateinit var id: String
    private lateinit var password: String
    private lateinit var name: String

    private val isRegistered = MutableLiveData<Boolean>()
    private val userRepository: UserRepository = UserRepository()

    fun setUserInformation(id: String, password: String, name: String){
        this.id = id
        this.password = password
        this.name = name
    }

    fun checkUserInformationFormat(id: String, password: String, name: String)= when{
        id.length<5 ->  "id"
        password.length<4 -> "password"
        name.length<2 -> "name"
        else -> null
    }

    fun trySignup(id: String, password: String, name: String): String{
        userRepository.trySignup(id, password, name,  )
    }

    public fun getRegisterState(): LiveData<Boolean> { return isRegistered }
}