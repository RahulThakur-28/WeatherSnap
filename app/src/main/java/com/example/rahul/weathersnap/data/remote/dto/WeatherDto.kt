package com.example.rahul.weathersnap.data.remote.dto

import com.google.gson.annotations.SerializedName

data class WeatherResponse(
    val latitude: Double,
    val longitude: Double,
    @SerializedName("current_weather")
    val currentWeather: CurrentWeather?,
    val hourly: Hourly?
)

data class CurrentWeather(
    val temperature: Double,
    @SerializedName("windspeed")
    val windSpeed: Double,
    @SerializedName("weathercode")
    val weatherCode: Int,
    val time: String
)

data class Hourly(
    val time: List<String>,
    @SerializedName("relativehumidity_2m")
    val humidity: List<Int>,
    @SerializedName("pressure_msl")
    val pressure: List<Double>
)
