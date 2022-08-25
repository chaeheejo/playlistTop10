package com.example.playlisttop10.friend

import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.playlisttop10.Song
import com.example.playlisttop10.User
import com.example.playlisttop10.UserRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class UserListViewModel : ViewModel() {
    var userList: MutableList<String> = mutableStateListOf()

    private var userListLoaded = MutableLiveData<Boolean>()
    val isUserListLoaded: LiveData<Boolean> = userListLoaded

    private var _errorMessage = MutableLiveData<String>()
    val errorMessage: LiveData<String> = _errorMessage

    fun loadUserList() {
        CoroutineScope(Dispatchers.IO).launch {
            val result = UserRepository.loadUserList()

            _errorMessage.postValue(
                if (result.isSuccess) {
                    val _userList = result.getOrDefault(listOf())
                    userList = _userList.toMutableList()
                    userListLoaded.postValue(true)
                    ""
                } else {
                    userListLoaded.postValue(false)
                    result.exceptionOrNull()?.message
                }
            )
        }
    }

    fun getPlaylistById(id: String): List<Song>?{
        return UserRepository.getPlaylistById(id)
    }

}