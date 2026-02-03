package com.example.composetutorial

import android.net.Uri
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query

@Dao
interface UserDataDao {
    @Query("SELECT * FROM userdata")
    fun getAll(): List<UserData>

    @Query("SELECT username FROM userdata LIMIT 1")
    fun getUsername(): String

    @Query("SELECT imageUri FROM userdata LIMIT 1")
    fun getImageUri(): String?

    @Query("UPDATE userdata SET username = :username WHERE id = 0")
    fun updateUsername(username: String)

    @Query("UPDATE userdata SET imageUri = :imageUri WHERE id = 0")
    fun updateImageUri(imageUri: String)

    @Insert
    fun insertAll(vararg userdata: UserData)

    @Delete
    fun delete(userdata: UserData)

}