package com.example.composetutorial

import android.Manifest
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.room.Room
import com.example.composetutorial.ui.theme.ComposeTutorialTheme
import kotlinx.serialization.Serializable
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import android.app.Notification
import android.content.pm.PackageManager
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.activity.viewModels

public const val CHANNEL_ID = "general_notifications"
public const val EXTRA_START_DESTINATION = "extra_start_destination"
class MainActivity : ComponentActivity() {
    private val PERMISSION_REQUEST_CODE = 666
    private val viewModel by viewModels<MainViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        installSplashScreen().apply {
            setKeepOnScreenCondition {
                !viewModel.isReady.value
            }
        }
        enableEdgeToEdge()

        val db = Room.databaseBuilder(
            applicationContext,
            AppDatabase::class.java, "appdata"
        )
        .fallbackToDestructiveMigration()
        .build()
        
        // Initialize database with default values if empty
        CoroutineScope(Dispatchers.IO).launch {
            val userDataDao = db.userDataDao()
            if (userDataDao.getAll().isEmpty()) {
                userDataDao.insertAll(UserData())
            }
        }

        requestPermissions()
        NotificationHelper.createNotificationChannel(this)

        //This is for navigation with notification
        val startDestination = when (intent.getStringExtra(EXTRA_START_DESTINATION)) {
            "conversation" -> Conversation
            "weather" -> WeatherScreen
            else -> Home
        }


        setContent {
            ComposeTutorialTheme {
                Surface(Modifier.fillMaxSize()) {
                    MyAppNavigation(db, this, startDestination)
                }
            }
        }
        
    }

    private fun requestPermissions(){
        val permissions = arrayOf(
            Manifest.permission.INTERNET,
            Manifest.permission.POST_NOTIFICATIONS
        )

        val permissionsToRequest = permissions.filter {permission ->
            ContextCompat.checkSelfPermission(this, permission) != PackageManager.PERMISSION_GRANTED
        }

        if (permissionsToRequest.isNotEmpty()) {
            ActivityCompat.requestPermissions(
                this,
                permissionsToRequest.toTypedArray(),
                PERMISSION_REQUEST_CODE
            )
        }else{
            Toast.makeText(this, "All permissions already granted", Toast.LENGTH_SHORT).show()
        }
    }
}




