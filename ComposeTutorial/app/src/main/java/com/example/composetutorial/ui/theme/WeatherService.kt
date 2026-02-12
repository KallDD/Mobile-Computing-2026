package com.example.composetutorial.ui.theme

import androidx.room.Query
import com.example.composetutorial.WeatherData
import retrofit2.http.GET
import retrofit2.Response

interface WeatherService {
    @GET("v1/forecast?latitude=65.0124&longitude=25.4682&hourly=temperature_2m&timezone=auto&forecast_days=1")
    suspend fun getWeather(): WeatherData
}