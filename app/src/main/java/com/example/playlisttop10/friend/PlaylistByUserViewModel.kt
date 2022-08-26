package com.example.playlisttop10.friend

import androidx.lifecycle.ViewModel
import com.example.playlisttop10.Song
import com.example.playlisttop10.UserRepository

class PlaylistByUserViewModel : ViewModel() {

    fun getPlaylistById(id: String): List<Song>?{
        return UserRepository.getPlaylistById(id)
    }
}