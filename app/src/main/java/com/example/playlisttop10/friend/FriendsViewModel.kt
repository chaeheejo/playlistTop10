package com.example.playlisttop10.friend

import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.playlisttop10.UserRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class FriendsViewModel : ViewModel() {
    var friendIdList: MutableList<String> = mutableStateListOf()

    private var friendListLoaded = MutableLiveData<Boolean>()
    val isFriendListLoaded: LiveData<Boolean> = friendListLoaded

    private var _errorMessage = MutableLiveData<String>()
    val errorMessage: LiveData<String> = _errorMessage

    fun loadFriends() {
        CoroutineScope(Dispatchers.IO).launch {
            val result = UserRepository.loadAllFriends()

            _errorMessage.postValue(
                if (result.isSuccess) {
                    val _userList = result.getOrDefault(listOf())
                    friendIdList = _userList.toMutableList()

                    friendListLoaded.postValue(true)
                    ""
                } else {
                    friendListLoaded.postValue(false)
                    result.exceptionOrNull()?.message
                }
            )
        }
    }

    fun loadMyFavoriteFriendList(){
        CoroutineScope(Dispatchers.IO).launch {
            val result = UserRepository.loadMyFavoriteFriendList()

            _errorMessage.postValue(
                if (result.isSuccess) {
                    ""
                } else {
                    result.exceptionOrNull()?.message
                }
            )
        }
    }
}