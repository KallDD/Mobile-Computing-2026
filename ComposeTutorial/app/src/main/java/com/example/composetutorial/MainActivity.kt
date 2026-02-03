package com.example.composetutorial

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

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        
        val db = Room.databaseBuilder(
            applicationContext,
            AppDatabase::class.java, "userdata"
        ).build()
        
        // Initialize database with default values if empty
        CoroutineScope(Dispatchers.IO).launch {
            val userDataDao = db.userDataDao()
            if (userDataDao.getAll().isEmpty()) {
                userDataDao.insertAll(UserData())
            }
        }
        
        setContent {
            ComposeTutorialTheme {
                Surface(Modifier.fillMaxSize()) {
                    MyAppNavigation(db)
                }
            }
        }
    }
}



