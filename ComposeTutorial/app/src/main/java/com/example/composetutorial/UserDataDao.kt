package com.example.composetutorial

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
    fun getImageUrl(): String

    @Query("UPDATE UserData SET username = :username WHERE id = 0")
    fun updateUsername(username: String)

    @Insert
    fun insertAll(vararg userdata: UserData)

    @Delete
    fun delete(userdata: UserData)

}