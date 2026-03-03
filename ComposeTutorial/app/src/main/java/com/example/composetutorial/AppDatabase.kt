package com.example.composetutorial

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(
    entities = [
        UserData::class,
        WeatherDataEntity::class,
        HourlyDataEntity::class
    ], version = 2)

abstract class AppDatabase : RoomDatabase() {
    abstract fun userDataDao(): UserDataDao
    abstract fun weatherDataDao(): WeatherDataDao
}