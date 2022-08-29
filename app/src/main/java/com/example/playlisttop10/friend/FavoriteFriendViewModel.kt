package com.example.playlisttop10.friend

import androidx.lifecycle.ViewModel
import com.example.playlisttop10.UserRepository

class FavoriteFriendViewModel : ViewModel() {

    fun getFriendList(): List<String> {
        return UserRepository.getMyFavoriteFriendList()
    }
}