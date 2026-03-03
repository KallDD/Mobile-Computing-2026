package com.example.composetutorial

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "hourly_sample",
    foreignKeys = [
        ForeignKey(
            entity = WeatherDataEntity::class,
            parentColumns = ["id"],
            childColumns = ["weatherId"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [Index("weatherId")]
)
data class HourlyDataEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,

    val weatherId: Long,          // FK to WeatherDataEntity.id
    val time: String,             // e.g. "2026-03-02T10:00"
    val temperature_2m: Double
)