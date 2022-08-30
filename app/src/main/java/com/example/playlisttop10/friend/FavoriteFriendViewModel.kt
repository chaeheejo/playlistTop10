package com.example.playlisttop10.friend

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.playlisttop10.UserRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class FavoriteFriendViewModel : ViewModel() {
    private var myFavoriteFriendListLoaded = MutableLiveData<Boolean>()
    val isMyFavoriteFriendListLoaded: LiveData<Boolean> = myFavoriteFriendListLoaded

    private var _errorMessage = MutableLiveData<String>()
    val errorMessage: LiveData<String> = _errorMessage

    fun loadMyFavoriteFriendList(){
        CoroutineScope(Dispatchers.IO).launch {
            val result = UserRepository.loadMyFavoriteFriendList()

            _errorMessage.postValue(
                if (result.isSuccess) {
                    myFavoriteFriendListLoaded.postValue(true)
                    ""
                } else {
                    myFavoriteFriendListLoaded.postValue(false)
                    result.exceptionOrNull()?.message
                }
            )
        }
    }

    fun getFriendList(): List<String> {
        return UserRepository.getMyFavoriteFriendList()
    }
}