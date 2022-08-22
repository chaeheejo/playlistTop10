package com.example.playlisttop10

import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await

object SongRepository {
    private val allSongMap: MutableMap<String, Song> = mutableMapOf()
    private val allSongList: MutableList<Song> = mutableListOf()

    suspend fun tryRegisterSong(song: Song): Result<String>{
        val db = FirebaseFirestore.getInstance()
        val songMap = hashMapOf<String, String>(
            "title" to song.title,
            "singer" to song.singer,
            "album" to song.album
        )

        return try {
            db.collection("song")
                .document(song.title)
                .set(songMap)
                .await()
            Result.success("Success")
        } catch (e: Exception) {
            Result.failure(Exception("fail to register song"))
        }
    }

    suspend fun loadAllSongs(){
        val db = FirebaseFirestore.getInstance()
        val result = db.collection("song").get().await()
        result.documents.forEach { doc ->
            val songToAdd = doc.toObject(Song::class.java)
            songToAdd?.let {
                allSongList.add(it)
                allSongMap[it.title] = it
            }
        }
    }

    fun getSongsByTitleList(titleList: List<String>): List<Song> {
        val toReturn: MutableList<Song> = mutableListOf()
        titleList.forEach { title ->
            val song = getSongByTitle(title)
            song?.let { toReturn.add(it) }
        }
        return toReturn
    }

    private fun getSongByTitle(title: String): Song? {
        return allSongMap[title]
    }

    fun getAllSongs(): List<Song> {
        return allSongList
    }
}