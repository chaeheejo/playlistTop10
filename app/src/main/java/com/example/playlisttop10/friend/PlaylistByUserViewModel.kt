package com.example.playlisttop10.friend

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.playlisttop10.Song
import com.example.playlisttop10.UserRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class PlaylistByUserViewModel : ViewModel() {
    private var addedFavoriteFriend = MutableLiveData<Boolean>()
    val isAddedFavoriteFriend: LiveData<Boolean> = addedFavoriteFriend

    private var _errorMessage = MutableLiveData<String>()
    val errorMessage: LiveData<String> = _errorMessage

    fun tryAddFavoriteFriend(toAddId: String) {
        CoroutineScope(Dispatchers.IO).launch {
            val result = UserRepository.tryAddFavoriteFriend(toAddId)

            _errorMessage.postValue(
                if (result.isSuccess) {
                    addedFavoriteFriend.postValue(true)
                    ""
                } else {
                    addedFavoriteFriend.postValue(false)
                    result.exceptionOrNull()?.message
                }
            )
        }
    }

    fun tryDeleteFavoriteFriend(toDeleteId: String) {
        CoroutineScope(Dispatchers.IO).launch {
            val result = UserRepository.tryDeleteFavoriteFriend(toDeleteId)

            _errorMessage.postValue(
                if (result.isSuccess) {
                    addedFavoriteFriend.postValue(true)
                    ""
                } else {
                    addedFavoriteFriend.postValue(false)
                    result.exceptionOrNull()?.message
                }
            )
        }
    }

    fun getPlaylistById(id: String): List<Song>? {
        return UserRepository.getPlaylistById(id)
    }
}