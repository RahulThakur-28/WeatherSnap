package com.example.rahul.weathersnap.data.repository

import com.example.rahul.weathersnap.data.local.ReportDao
import com.example.rahul.weathersnap.data.local.ReportEntity
import com.example.rahul.weathersnap.data.remote.WeatherApi
import com.example.rahul.weathersnap.data.remote.dto.GeocodingResult
import com.example.rahul.weathersnap.domain.model.Weather
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class WeatherRepositoryImpl @Inject constructor(
    private val api: WeatherApi,
    private val dao: ReportDao
) {
    suspend fun searchCity(query: String): List<GeocodingResult> {
        return try {
            api.searchCity(query).results ?: emptyList()
        } catch (e: Exception) {
            emptyList()
        }
    }

    suspend fun getWeather(lat: Double, lon: Double, cityName: String): Weather? {
        return try {
            val response = api.getForecast(lat, lon)
            val current = response.currentWeather
            val hourly = response.hourly
            
            if (current != null && hourly != null) {
                Weather(
                    cityName = cityName,
                    temperature = current.temperature,
                    condition = getWeatherCondition(current.weatherCode),
                    humidity = hourly.humidity.firstOrNull() ?: 0,
                    windSpeed = current.windSpeed,
                    pressure = hourly.pressure.firstOrNull() ?: 0.0,
                    latitude = lat,
                    longitude = lon
                )
            } else null
        } catch (e: Exception) {
            null
        }
    }

    suspend fun saveReport(report: ReportEntity) {
        dao.insertReport(report)
    }

    fun getAllReports(): Flow<List<ReportEntity>> {
        return dao.getAllReports()
    }

    private fun getWeatherCondition(code: Int): String {
        return when (code) {
            0 -> "Clear sky"
            1, 2, 3 -> "Mainly clear, partly cloudy, and overcast"
            45, 48 -> "Fog"
            51, 53, 55 -> "Drizzle"
            61, 63, 65 -> "Rain"
            71, 73, 75 -> "Snow fall"
            77 -> "Snow grains"
            80, 81, 82 -> "Rain showers"
            85, 86 -> "Snow showers"
            95 -> "Thunderstorm"
            96, 99 -> "Thunderstorm with hail"
            else -> "Unknown"
        }
    }
}
