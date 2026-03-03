package com.example.composetutorial

import androidx.room.ColumnInfo
import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class WeatherDataEntity(
    @PrimaryKey(autoGenerate = true)
    val id : Long = 0,

    @ColumnInfo val latitude: Double,
    @ColumnInfo val longitude: Double,
    @ColumnInfo val generationtime_ms: Double,
    @ColumnInfo val utc_offset_seconds: Int,
    @ColumnInfo val timezone: String,
    @ColumnInfo val timezone_abbreviation: String,
    @ColumnInfo val elevation: Double,

    @Embedded(prefix = "hourly_units_")
    val hourly_units: HourlyUnitsData,

)
data class HourlyUnitsData(
    val time: String,
    val temperature_2m: String
)