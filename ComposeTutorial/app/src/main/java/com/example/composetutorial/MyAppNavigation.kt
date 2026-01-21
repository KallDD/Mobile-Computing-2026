package com.example.composetutorial

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import kotlinx.serialization.Serializable

@Serializable object Home
@Serializable object Conversation

@Composable
fun MyAppNavigation(){
    val navController = rememberNavController()
    NavHost(navController = navController, startDestination = Home) {
        composable<Home> { HomeScreen(navController) }
        composable<Conversation> { ConversationScreen(navController)}
    }
}