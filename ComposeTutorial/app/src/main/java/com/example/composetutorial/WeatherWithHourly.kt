package com.example.composetutorial

import androidx.room.Embedded
import androidx.room.Relation

data class WeatherWithHourly(
    @Embedded val weather: WeatherDataEntity,

    @Relation(
        parentColumn = "id",
        entityColumn =  "weatherId"
    )
    val hourly: List<HourlyDataEntity>
)
