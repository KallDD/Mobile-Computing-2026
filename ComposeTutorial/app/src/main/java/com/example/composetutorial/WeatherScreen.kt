package com.example.composetutorial

import android.content.Context
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.unit.*
import com.example.composetutorial.ui.theme.WeatherService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


@Composable
fun WeatherScreen(context: Context, db: AppDatabase, navController: NavController){

    Column (modifier = Modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center){

        Text(text = "Weather screen")
        WeatherUI(db, context)

        Button(
            onClick = { navController.navigate(Home) },
            modifier = Modifier
                .padding(5.dp)
                .fillMaxWidth()
        ){
            Text(text = "Home")
        }
    }
}

@Composable
fun WeatherUI(db : AppDatabase, context: Context){
    val weatherApi = RetrofitHelper.getInstance().create(WeatherService::class.java)
    val weatherScope = rememberCoroutineScope()
    val weatherDataDao = db.weatherDataDao()

    var apiWeather by remember { mutableStateOf<WeatherData?> (null) }
    var showApiWeather by remember { mutableStateOf<Boolean>(false) }

    var ids by remember { mutableStateOf<List<Long>>(emptyList()) }
    var index by remember { mutableStateOf(0) }
    var dbWeather by remember { mutableStateOf<WeatherWithHourly?> (null) }

    // This fun is chatGPT generated
    suspend fun loadIdsAndCurrent(keepIndex: Boolean = true) {
        val newIds = weatherDataDao.getAllWeatherIdsDesc()
        ids = newIds

        index = if (keepIndex) {
            index.coerceIn(0, maxOf(newIds.size - 1, 0))
        } else {
            0 // jump to newest
        }

        dbWeather = newIds.getOrNull(index)?.let { weatherDataDao.getWeatherWithHourly(it) }
    }

    LaunchedEffect(Unit) {
        dbWeather = withContext(Dispatchers.IO) {
            try {
                weatherDataDao.getLatestWeatherWithHourly()
            } catch (e: Exception) {
                null
            }
        }
    }

    Button(
        onClick = {
            weatherScope.launch {
                apiWeather = withContext(Dispatchers.IO) {
                    weatherApi.getWeather()
                }
                showApiWeather = true
            }
        },
        modifier = Modifier.padding(5.dp).fillMaxWidth()
    ) {
        Text("Get weather")
    }

    if(showApiWeather) {
        formatApiWeather(apiWeather)
    }
    else {
        formatDbWeather(dbWeather)
    }

    Row() {
        Button(
            onClick = {
                NotificationHelper.sendActionNotification(context, "Previous", "")
            },
            modifier = Modifier
                .padding(5.dp)
                .weight(1f)
        ) {
            Text(text = "Previous")
        }

        if(showApiWeather) {
            apiWeather?.let { current ->
                Button(
                    onClick = {
                        weatherScope.launch {
                            // DB work on IO
                            val latest = withContext(Dispatchers.IO) {
                                weatherDataDao.insertWeatherAndHourly(
                                    current.toEntity(),
                                    current.toHourlyEntities()
                                )
                                weatherDataDao.getLatestWeatherWithHourly()
                            }

                            // State updates on Main
                            dbWeather = latest
                            showApiWeather = false

                            NotificationHelper.sendActionNotification(
                                context,
                                "WeatherData saved",
                                ""
                            )
                        }
                    },
                    modifier = Modifier.padding(5.dp).weight(1f)
                ) { Text("Save weather") }
            }
        }

        Button(
            onClick = {
                NotificationHelper.sendActionNotification(context, "Next", "")
            },
            modifier = Modifier
                .padding(5.dp)
                .weight(1f)
        ) {
            Text(text = "Next")
        }
    }
}

// I used chatGPT to genereta clean formating of the data to desired format
@Composable
fun formatApiWeather(weatherData: WeatherData?){

    if (weatherData == null) {
        Text("No data available")
        return
    }

    val times = weatherData.hourly.time
    val temps = weatherData.hourly.temperature_2m

    if (times.isEmpty()) {
        Text("No hourly data")
        return
    }


    val firstTimestamp = times.first()
    val date = firstTimestamp.substringBefore("T")

    Column(modifier = Modifier.padding(16.dp)) {

        // ✅ Show date once at top
        Text(
            text = "Api: $date",
            style = MaterialTheme.typography.titleLarge
        )

        Spacer(modifier = Modifier.height(16.dp))

        times.forEachIndexed { index, time ->
            val hour = time.substringAfter("T")
            val temp = temps[index]

            Text(text = "$hour → $temp °C")
        }
    }

}

@Composable
fun formatDbWeather(weatherData: WeatherWithHourly?) {

    if (weatherData == null) {
        Text("No data available")
        return
    }

    val hourlyList = weatherData.hourly

    if (hourlyList.isEmpty()) {
        Text("No hourly data")
        return
    }

    val firstTimestamp = hourlyList.first().time
    val date = firstTimestamp.substringBefore("T")

    Column(modifier = Modifier.padding(16.dp)) {

        Text(
            text = date,
            style = MaterialTheme.typography.titleLarge
        )

        Spacer(modifier = Modifier.height(16.dp))

        hourlyList.forEach { row ->
            val hour = row.time.substringAfter("T")
            Text(text = "$hour → ${row.temperature_2m} °C")
        }
    }
}


