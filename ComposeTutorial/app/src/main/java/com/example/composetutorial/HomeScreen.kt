package com.example.composetutorial

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

@Composable
fun HomeScreen(navController: NavController){
    ExitAppHandler()
    Column (modifier = Modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center){
        Text(text= "Home screen")
        Button(onClick = { navController.navigate(Conversation) }) {
            Text(text = "Messages")
        }
    }
}

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