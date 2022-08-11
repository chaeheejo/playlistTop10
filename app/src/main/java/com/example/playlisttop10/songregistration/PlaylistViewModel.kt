package com.example.playlisttop10.songregistration

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.playlisttop10.Song
import com.example.playlisttop10.SongRepository
import com.example.playlisttop10.UserRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class PlaylistViewModel : ViewModel() {
    val testText: MutableState<String> = mutableStateOf("")
    val songList: MutableList<Song> = mutableStateListOf()
    val allSongsLoaded: MutableLiveData<Boolean> = MutableLiveData(false)

    init {
        CoroutineScope(Dispatchers.IO).launch {
            val result = SongRepository.loadAllSongs()


            allSongsLoaded.postValue(true)
        }
    }

    fun loadMySongs(){
        val mySongIdList = UserRepository.currUser!!.titleListForPlaylist
        songList.clear()
        songList.addAll(SongRepository.getSongsByTitle(mySongIdList))
    }
}