package com.example.composetutorial

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Transaction

@Dao
interface WeatherDataDao {

    @Insert
    suspend fun insertWeather(weather: WeatherDataEntity): Long

    @Insert
    suspend fun insertHourlyData(samples: List<HourlyDataEntity>)

    @Query("SELECT id FROM WeatherDataEntity ORDER BY id DESC")
    suspend fun getAllWeatherIdsDesc(): List<Long>

    @Transaction
    @Query("SELECT * FROM WeatherDataEntity WHERE id = :weatherId")
    suspend fun getWeatherWithHourly(weatherId: Long): WeatherWithHourly?

    @Transaction
    @Query("SELECT * FROM WeatherDataEntity ORDER BY id DESC LIMIT 1")
    suspend fun getLatestWeatherWithHourly(): WeatherWithHourly?

    //chatGPT has helped with this
    @Transaction
    suspend fun insertWeatherAndHourly(
        weather: WeatherDataEntity,
        hourly: List<HourlyDataEntity>
    ): Long {
        val weatherId = insertWeather(weather)
        val withFk = hourly.map { it.copy(weatherId = weatherId) }
        insertHourlyData(withFk)
        return weatherId
    }
}