package com.example.playlisttop10.playlist

import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel
import com.example.playlisttop10.Song
import com.example.playlisttop10.UserRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class PlaylistViewModel : ViewModel() {
    val songList: MutableList<Song> = mutableStateListOf()

    fun loadMySongs() {
        CoroutineScope(Dispatchers.IO).launch {
            val mySongList = UserRepository.currUser?.playlist

            if (mySongList != null){
                songList.clear()
                songList.addAll(mySongList)
            }
        }
    }
}