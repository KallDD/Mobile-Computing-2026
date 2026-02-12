package com.example.composetutorial

import android.content.Context
import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navDeepLink
import kotlinx.serialization.Serializable

@Serializable object Home
@Serializable object Conversation
@Serializable object WeatherScreen

@Composable
fun MyAppNavigation(db: AppDatabase, context: Context, startDestination: Any = Home){
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = startDestination) {
        composable<Home> { HomeScreen(navController, db, context) }
        composable<Conversation> { ConversationScreen(navController, db)}
        composable<WeatherScreen>{ WeatherScreen(navController)}
    }
}