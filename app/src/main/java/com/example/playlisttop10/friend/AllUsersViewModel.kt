package com.example.playlisttop10.friend

import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.playlisttop10.UserRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class AllUsersViewModel : ViewModel() {
    var friendIdList: MutableList<String> = mutableStateListOf()

    private var userListLoaded = MutableLiveData<Boolean>()
    val isUserListLoaded: LiveData<Boolean> = userListLoaded

    private var _errorMessage = MutableLiveData<String>()
    val errorMessage: LiveData<String> = _errorMessage

    fun loadFriends() {
        CoroutineScope(Dispatchers.IO).launch {
            val result = UserRepository.loadAllFriends()

            _errorMessage.postValue(
                if (result.isSuccess) {
                    val _userList = result.getOrDefault(listOf())
                    friendIdList = _userList.toMutableList()

                    userListLoaded.postValue(true)
                    ""
                } else {
                    userListLoaded.postValue(false)
                    result.exceptionOrNull()?.message
                }
            )
        }
    }

    fun getNumberOfLikesById(id: String): Int{
        return UserRepository.getNumberOfLikesById(id)
    }
}