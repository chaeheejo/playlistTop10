package com.example.playlisttop10.songregistration

import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel
import com.example.playlisttop10.Song
import com.example.playlisttop10.SongRepository
import com.example.playlisttop10.UserRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class PlaylistViewModel : ViewModel() {
    val songList: MutableList<Song> = mutableStateListOf()

    fun loadMySongs() {
        CoroutineScope(Dispatchers.IO).launch {
            UserRepository.setSongTitleListForCurrUser()
            val mySongTitleList = UserRepository.currUser!!.songTitleList

            songList.clear()
            songList.addAll(SongRepository.getSongsByTitleList(mySongTitleList))
        }
    }
}