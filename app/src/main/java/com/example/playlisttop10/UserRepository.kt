package com.example.playlisttop10

import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.SetOptions
import kotlinx.coroutines.tasks.await

object UserRepository {
    var currUser: User? = null
    private var allFriendsMap = hashMapOf<String, User>()
    private var allFriendsList = mutableListOf<String>()
    private var myFavoriteFriendList = mutableListOf<String>()

    fun clear() {
        currUser = null
        allFriendsMap = hashMapOf()
        allFriendsList = mutableListOf()
    }

    suspend fun trySignUp(id: String, password: String, name: String): Result<String> {
        val db = FirebaseFirestore.getInstance()

        if (doesDuplicateIdExist(id))
            return Result.failure(Exception("id already exists"))

        val user = User(id = id, password = password, name = name)
        val likeMap = hashMapOf("like list" to emptyList<String>())

        return try {
            db.collection("user")
                .document(id)
                .set(user)
                .await()

            db.collection("like list")
                .document(id)
                .set(likeMap)
                .await()

            Result.success("Success")
        } catch (e: Exception) {
            Result.failure(Exception("fail to sign up by network problem"))
        }
    }

    suspend fun tryLogIn(id: String, password: String): Result<String> {
        val db = FirebaseFirestore.getInstance()

        return try {
            val documentSnapshot = db.collection("user")
                .document(id)
                .get()
                .await()

            if (documentSnapshot.data == null) {
                return Result.failure(Exception("id does not exist"))
            }

            if (documentSnapshot.get("password") == password) {
                val result = documentSnapshot.toObject(User::class.java)
                currUser = result
                Result.success("success")

            } else {
                Result.failure(Exception("password does not match"))
            }
        } catch (e: Exception) {
            Result.failure(Exception("fail to log in by network problem"))
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

    suspend fun loadAllFriends(): Result<List<String>> {
        val db = FirebaseFirestore.getInstance()

        allFriendsMap.clear()
        allFriendsList.clear()

        return try {
            db.collection("user")
                .orderBy("like", Query.Direction.DESCENDING)
                .get()
                .await()
                .forEach {
                    allFriendsList.add(it.id)
                    allFriendsMap[it.id] = it.toObject(User::class.java)
                }

            Result.success(allFriendsList)
        } catch (e: Exception) {
            Result.failure(Exception("fail to load user list"))
        }
    }

    suspend fun loadMyFavoriteFriendList(): Result<String>{
        val db = FirebaseFirestore.getInstance()

        return try {
            val documentSnapshot = db.collection("like list")
                .document(currUser!!.id)
                .get()
                .await()

            myFavoriteFriendList = documentSnapshot.get("like list") as MutableList<String>

            Result.success("success")
        } catch (e: Exception) {
            Result.failure(Exception("fail to load favorite friend"))
        }
    }

    suspend fun tryAddFavoriteFriend(toAddId: String): Result<String> {
        val db = FirebaseFirestore.getInstance()

        return try {
            myFavoriteFriendList.add(toAddId)

            val favoriteMap = hashMapOf("like list" to myFavoriteFriendList)

            db.collection("like list")
                .document(currUser!!.id)
                .set(favoriteMap, SetOptions.merge())
                .await()

            loadAllFriends()
            allFriendsMap[toAddId]!!.like++
            updateNumberOfLikesForFavoriteFriend(allFriendsMap[toAddId]!!)

            Result.success("success")
        } catch (e: Exception) {
            Result.failure(Exception("fail to add favorite friend"))
        }
    }

    private suspend fun updateNumberOfLikesForFavoriteFriend(toAddUser: User) {
        val db = FirebaseFirestore.getInstance()
        val docRef = db.collection("user").document(toAddUser.id)

        db.runTransaction { transaction ->
            val snapshot = transaction.get(docRef)

            var newLike = snapshot.get("like") as Long
            newLike +=1
            transaction.update(docRef, "like", newLike)
        }.await()
    }

    suspend fun tryDeleteFavoriteFriend(toDeleteId: String): Result<String>{
        val db = FirebaseFirestore.getInstance()

        return try {
            myFavoriteFriendList.remove(toDeleteId)

            val favoriteMap = hashMapOf("like list" to myFavoriteFriendList)

            db.collection("like list")
                .document(currUser!!.id)
                .set(favoriteMap, SetOptions.merge())
                .await()

            loadAllFriends()
            allFriendsMap[toDeleteId]!!.like--
            deleteNumberOfLikesForFavoriteFriend(allFriendsMap[toDeleteId]!!)

            Result.success("success")
        } catch (e: Exception) {
            Result.failure(Exception("fail to add favorite friend"))
        }
    }

    private fun deleteNumberOfLikesForFavoriteFriend(toAddUser: User) {
        val db = FirebaseFirestore.getInstance()
        val docRef = db.collection("user").document(toAddUser.id)

        db.runTransaction { transaction ->
            val snapshot = transaction.get(docRef)

            var newLike = snapshot.get("like") as Long
            newLike -=1
            transaction.update(docRef, "like", newLike)
        }
    }

    fun getPlaylistById(id: String): List<Song>? {
        if (allFriendsMap[id]!!.playlist.isEmpty()) {
            return null
        }
        return allFriendsMap[id]!!.playlist
    }

    fun getMyFavoriteFriendList(): List<String>{
        return myFavoriteFriendList
    }

    fun getNumberOfLikesById(id: String): Int {
        return if (id in allFriendsMap){
            allFriendsMap[id]!!.like
        }else{
            0
        }
    }
}