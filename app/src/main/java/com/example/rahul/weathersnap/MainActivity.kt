package com.example.rahul.weathersnap

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.Composable
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.rahul.weathersnap.navigation.Screen
import com.example.rahul.weathersnap.presentation.screens.weather.WeatherScreen
import com.example.rahul.weathersnap.presentation.screens.report.CreateReportScreen
import com.example.rahul.weathersnap.presentation.screens.camera.CameraScreen
import com.example.rahul.weathersnap.presentation.screens.saved.SavedReportsScreen
import com.example.rahul.weathersnap.ui.theme.WeatherSnapTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            WeatherSnapTheme {
                WeatherSnapAppNavigation()
            }
        }
    }
}

@Composable
fun WeatherSnapAppNavigation() {
    val navController = rememberNavController()
    NavHost(
        navController = navController,
        startDestination = Screen.Weather.route
    ) {
        composable(Screen.Weather.route) {
            WeatherScreen(
                onNavigateToCreateReport = { weather ->
                    navController.navigate(
                        Screen.CreateReport.passArgs(
                            cityName = weather.cityName,
                            temp = weather.temperature.toFloat(),
                            condition = weather.condition,
                            humidity = weather.humidity,
                            wind = weather.windSpeed.toFloat(),
                            pressure = weather.pressure.toFloat()
                        )
                    )
                },
                onNavigateToSavedReports = {
                    navController.navigate(Screen.SavedReports.route)
                }
            )
        }
        composable(
            route = Screen.CreateReport.route,
            arguments = listOf(
                navArgument("cityName") { type = NavType.StringType },
                navArgument("temp") { type = NavType.FloatType },
                navArgument("condition") { type = NavType.StringType },
                navArgument("humidity") { type = NavType.IntType },
                navArgument("wind") { type = NavType.FloatType },
                navArgument("pressure") { type = NavType.FloatType }
            )
        ) {
            CreateReportScreen(
                onNavigateToCamera = {
                    navController.navigate(Screen.Camera.route)
                },
                onNavigateBack = {
                    navController.popBackStack()
                },
                onSaveSuccess = {
                    navController.navigate(Screen.SavedReports.route) {
                        popUpTo(Screen.Weather.route)
                        launchSingleTop = true
                    }
                }
            )
        }
        composable(Screen.Camera.route) {
            CameraScreen(
                onImageCaptured = {
                    navController.popBackStack()
                },
                onClose = {
                    navController.popBackStack()
                }
            )
        }
        composable(Screen.SavedReports.route) {
            SavedReportsScreen(
                onBackClick = {
                    navController.popBackStack()
                }
            )
        }
    }
}
