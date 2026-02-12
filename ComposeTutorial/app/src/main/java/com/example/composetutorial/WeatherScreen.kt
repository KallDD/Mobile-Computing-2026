package com.example.composetutorial

import android.content.Context
import android.net.Uri
import android.util.Log
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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.unit.*
import com.example.composetutorial.ui.theme.WeatherService
import kotlinx.coroutines.launch
import okhttp3.Response
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter


@Composable
fun WeatherScreen(navController: NavController){

    Column (modifier = Modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center){

        Text(text = "Weather screen")
        WeatherUI()

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
fun WeatherUI(){
    val weatherApi = RetrofitHelper.getInstance().create(WeatherService::class.java)
    val weatherScope = rememberCoroutineScope()
    var weatherData by remember { mutableStateOf<WeatherData?> (null) }
    Button(
        onClick = {
            weatherScope.launch {
                weatherData = weatherApi.getWeather()
            }
        },
        modifier = Modifier
            .padding(5.dp)
            .fillMaxWidth()
    ){
        Text(text = "Get weather")
    }

    formatWeather(weatherData)
}

// I used chatGPT to genereta clean formating of the data to desired format
@Composable
fun formatWeather(weatherData: WeatherData?){

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
            text = "$date",
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


