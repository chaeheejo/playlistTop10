package com.example.playlisttop10

import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions
import kotlinx.coroutines.tasks.await

object UserRepository {
    var currUser: User? = null
    var allUserMap = HashMap<String, User>()
    var allUserIdList = mutableListOf<String>()

    suspend fun trySignUp(id: String, password: String, name: String): Result<String> {
        val db = FirebaseFirestore.getInstance()

        if (doesDuplicateIdExist(id))
            return Result.failure(Exception("Id Already Exists"))

        val userMap = hashMapOf<String, String>("id" to id, "password" to password, "name" to name)

        return try {
            db.collection("user")
                .document(id)
                .set(userMap)
                .await()
            Result.success("Success")
        } catch (e: Exception) {
            Result.failure(Exception("fail to sign up"))
        }
    }

    suspend fun tryLogIn(id: String, password: String): Result<String> {
        val db = FirebaseFirestore.getInstance()

        return try {
            val documentSnapshot = db.collection("user")
                .document(id)
                .get()
                .await()
            if (documentSnapshot.get("password") == password) {
                val result = documentSnapshot.toObject(User::class.java)

                if (result != null) {
                    currUser = result
                    Result.success("success")
                } else
                    Result.failure(Exception("couldn't find user"))

            } else {
                Result.failure(Exception("password does not match"))
            }
        } catch (e: Exception) {
            Result.failure(Exception("id does not exist"))
        }
    }

    private suspend fun doesDuplicateIdExist(id: String): Boolean {
        val db = FirebaseFirestore.getInstance()
        val documentSnapshot = db.collection("user")
            .whereEqualTo("id", id)
            .get()
            .await()
            .documents
        return documentSnapshot.isNotEmpty()
    }

    suspend fun tryRegisterMySong(song: Song): Result<String> {
        if (!doesSongExistInSongList(song)) {
            return Result.failure(Exception("this song is already registered"))
        }

        val db = FirebaseFirestore.getInstance()
        val newSongList: MutableList<Song> = mutableListOf()

        if (currUser!!.playlist.isNotEmpty()) {
            newSongList.addAll(currUser!!.playlist)
        }

        newSongList.add(song)
        currUser!!.playlist = newSongList

        val playlistMap = hashMapOf("playlist" to newSongList)

        return try {
            db.collection("user")
                .document(currUser!!.id)
                .set(playlistMap, SetOptions.merge())
                .await()
            Result.success("success")
        } catch (e: Exception) {
            Result.failure(Exception("fail to register song title"))
        }
    }

    private fun doesSongExistInSongList(song: Song): Boolean {
        return song !in currUser!!.playlist
    }

    suspend fun updateSongInformation(oldSong: Song, newSong: Song): Result<String> {
        val db = FirebaseFirestore.getInstance()

        for (tmp in currUser!!.playlist) {
            if (tmp == oldSong) {
                tmp.singer = newSong.singer
                tmp.album = newSong.album
            }
        }

        return try {
            db.collection("user")
                .document(currUser!!.id)
                .update("playlist", currUser!!.playlist)
                .await()
            Result.success("success")
        } catch (e: Exception) {
            Result.failure(Exception("fail to update song"))
        }
    }

    suspend fun loadUserList(): Result<List<String>> {
        val db = FirebaseFirestore.getInstance()

        allUserMap.clear()
        allUserIdList.clear()

        return try {
            db.collection("user")
                .get()
                .await()
                .forEach {
                    allUserIdList.add(it.id)
                    allUserMap[it.id] = it.toObject(User::class.java)
                }

            Result.success(allUserIdList)
        } catch (e: Exception) {
            Result.failure(Exception("fail to load user list"))
        }
    }

    fun getPlaylistById(id: String): List<Song>? {
        if (allUserMap[id]!!.playlist.isEmpty()) {
            return null
        }
        return allUserMap[id]!!.playlist
    }
}