package com.example.rahul.weathersnap.navigation

sealed class Screen(val route: String) {
    object Weather : Screen("weather")
    object CreateReport : Screen("create_report?cityName={cityName}&temp={temp}&condition={condition}&humidity={humidity}&wind={wind}&pressure={pressure}") {
        fun passArgs(
            cityName: String,
            temp: Float,
            condition: String,
            humidity: Int,
            wind: Float,
            pressure: Float
        ): String {
            return "create_report?cityName=$cityName&temp=$temp&condition=$condition&humidity=$humidity&wind=$wind&pressure=$pressure"
        }
    }
    object Camera : Screen("camera")
    object SavedReports : Screen("saved_reports")
}
