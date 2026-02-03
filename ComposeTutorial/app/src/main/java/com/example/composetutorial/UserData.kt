package com.example.composetutorial

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
//import coil3.Uri
import android.net.Uri

@Entity
data class UserData(
    @PrimaryKey val id: Int = 0,
    @ColumnInfo val username: String = "someone",
    @ColumnInfo val imageUri: String? = ""
)
