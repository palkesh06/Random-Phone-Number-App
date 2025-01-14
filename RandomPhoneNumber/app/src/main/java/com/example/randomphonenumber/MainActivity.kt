package com.example.randomphonenumber

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.randomphonenumber.ui.displayNumberScreen.DisplayNumberScreen
import com.example.randomphonenumber.ui.phoneDetailScreen.PhoneDetailsScreen
import com.example.randomphonenumber.ui.startScreen.StartScreen
import com.example.randomphonenumber.ui.theme.RandomPhoneNumberTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            RandomPhoneNumberTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    MyApp()
                }
            }
        }
    }
}

@Composable
fun MyApp() {
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = "start") {
        composable("start") {
            StartScreen(
                navController
            )
        }
        composable(
            route = "phoneDetails/{numbers}",
            arguments = listOf(navArgument("numbers") { type = NavType.StringType })
        ) { backStackEntry ->
            val numbersArg = backStackEntry.arguments?.getString("numbers") ?: ""
            val numbers = numbersArg.split(",")
            PhoneDetailsScreen(
                navController,
                numbers
            )
        }
        composable("displayNumbers") {
            DisplayNumberScreen(navController)
        }
    }
}

