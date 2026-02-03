package com.example.composetutorial

import android.net.Uri
import android.preference.PreferenceDataStore
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import androidx.activity.compose.BackHandler
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.TextButton
import androidx.compose.runtime.*
import androidx.activity.ComponentActivity
import androidx.activity.compose.LocalActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.TextField
import androidx.compose.ui.unit.*
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.room.util.convertByteToUUID
import coil3.compose.AsyncImage
import coil3.request.ImageRequest
import coil3.request.crossfade
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

@Composable
fun HomeScreen(navController: NavController, db: AppDatabase){
    ExitAppHandler()

    Column (modifier = Modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center){
        Text(text= "Home screen")
        TextInputUI(db)
        Button(onClick = { navController.navigate(Conversation) }) {
            Text(text = "Messages")
        }
    }
}

// Found help from GeeksForGeeks
@Composable
fun TextInputUI(db: AppDatabase){
    var text by remember { mutableStateOf("") }
    val coroutineScope = rememberCoroutineScope()
    val userDataDao = db.userDataDao()

    // Load username from database on background thread
    LaunchedEffect(Unit) {
        text = withContext(Dispatchers.IO) {
            try {
                userDataDao.getUsername()
            } catch (e: Exception) {
                "fail to load"
            }
        }
    }

    Column( modifier = Modifier
        .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ){
        TextField(
            value = text,
            onValueChange = { newText ->
                text = newText
                // Save to database on background thread
                coroutineScope.launch(Dispatchers.IO) {
                    userDataDao.updateUsername(newText)
                }
            },
            label = { Text("Username") },
        )
    }
}

// This methode is partially created with ChatGPT
@Composable
fun ExitAppHandler(){
    val activity = LocalActivity.current as ComponentActivity
    var showDialog by remember { mutableStateOf(false) }

    BackHandler(enabled = true) {
        showDialog = true
    }

    if (showDialog) {
        AlertDialog(
            onDismissRequest = { showDialog = false },
            title = { Text("Exit App") },
            text = { Text("Are you sure you want to quit the app?") },
            confirmButton = {
                TextButton(onClick = { activity.finishAffinity() }) {
                    Text("Yes")
                }
            },
            dismissButton = {
                TextButton(onClick = { showDialog = false }) {
                    Text("No")
                }
            }
        )
    }
}