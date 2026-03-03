package com.example.composetutorial

data class WeatherData(

    val latitude: Double,
    val longitude: Double,
    val generationtime_ms: Double,
    val utc_offset_seconds: Int,
    val timezone: String,
    val timezone_abbreviation: String,
    val elevation: Double,
    val hourly_units: HourlyUnits,
    val hourly: Hourly
)
data class HourlyUnits(
    val time: String,
    val temperature_2m: String
)

data class Hourly(
    val time: List<String>,
    val temperature_2m: List<Double>
)

//ChatGPT helped with mappings
fun WeatherData.toEntity(): WeatherDataEntity =
    WeatherDataEntity(
        latitude = latitude,
        longitude = longitude,
        generationtime_ms = generationtime_ms,
        utc_offset_seconds = utc_offset_seconds,
        timezone = timezone,
        timezone_abbreviation = timezone_abbreviation,
        elevation = elevation,
        hourly_units = HourlyUnitsData(
            time = hourly_units.time,
            temperature_2m = hourly_units.temperature_2m
        )

    )

fun WeatherData.toHourlyEntities(weatherId: Long = 0): List<HourlyDataEntity> {
    // assumes hourly.time.size == hourly.temperature_2m.size
    return hourly.time.indices.map { i ->
        HourlyDataEntity(
            weatherId = weatherId,
            time = hourly.time[i],
            temperature_2m = hourly.temperature_2m[i]
        )
    }
}