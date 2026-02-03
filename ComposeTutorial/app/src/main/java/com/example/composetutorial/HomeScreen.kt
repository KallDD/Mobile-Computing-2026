package com.example.composetutorial

import android.net.Uri
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
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.TextField
import androidx.compose.ui.unit.*
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import coil3.compose.AsyncImage
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts.PickVisualMedia
import androidx.compose.foundation.clickable
import java.io.File
import androidx.compose.ui.platform.LocalContext

@Composable
fun HomeScreen(navController: NavController, db: AppDatabase){
    ExitAppHandler()

    Column (modifier = Modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center){
        Text(text= "Home screen")
        ImageInputUI(db)
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


// AI has been used to figure out how to make the image picker from gallery and the saving
// of the image in onResult is AI generated
@Composable
fun ImageInputUI(db: AppDatabase){
    var imageUri by remember { mutableStateOf("") }
    val userDataDao = db.userDataDao()
    val coroutineScope = rememberCoroutineScope()
    val context = LocalContext.current

    val pickerLauncher = rememberLauncherForActivityResult(
        contract = PickVisualMedia(),
        onResult = { uri ->
            if (uri != null) {
                coroutineScope.launch {
                    val newImagePath = withContext(Dispatchers.IO) {
                        val imagesDir = File(context.filesDir, "images").apply { mkdirs() }
                        val newFile = File(imagesDir, "profile_${System.currentTimeMillis()}.jpg")

                        context.contentResolver.openInputStream(uri)?.use { input ->
                            newFile.outputStream().use { output ->
                                input.copyTo(output)
                            }
                        } ?: return@withContext ""

                        // (Optional) delete previous file if you store it
                        val oldPath = runCatching { userDataDao.getImageUri() }.getOrNull()
                        if (!oldPath.isNullOrBlank() && oldPath != newFile.absolutePath) {
                            runCatching { File(oldPath).delete() }
                        }

                        userDataDao.updateImageUri(newFile.absolutePath)
                        newFile.absolutePath
                    }

                    imageUri = newImagePath
                }
            }
        }
    )

    LaunchedEffect(Unit) {
        imageUri = withContext(Dispatchers.IO) {
            try {
                userDataDao.getImageUri() ?: ""
            } catch (e: Exception) {
                ""
            }
        }
    }

    val model = imageUri.ifEmpty { R.drawable.profile_picture }

    AsyncImage(
        model = model,
        placeholder = painterResource(R.drawable.profile_picture),
        contentDescription = null,
        modifier = Modifier
            .size(120.dp)
            .clip(CircleShape)
            .border(1.5.dp, MaterialTheme.colorScheme.primary, CircleShape)
            .clickable{pickerLauncher.launch(
                PickVisualMediaRequest(PickVisualMedia.ImageOnly)
            )}
    )
    Spacer(Modifier.width(8.dp))
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