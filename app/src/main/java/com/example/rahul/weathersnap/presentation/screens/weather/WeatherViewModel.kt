package com.example.rahul.weathersnap.presentation.screens.weather

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.rahul.weathersnap.data.remote.dto.GeocodingResult
import com.example.rahul.weathersnap.data.repository.WeatherRepositoryImpl
import com.example.rahul.weathersnap.domain.model.Weather
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

data class WeatherUiState(
    val searchQuery: String = "",
    val suggestions: List<GeocodingResult> = emptyList(),
    val isLoadingSuggestions: Boolean = false,
    val selectedWeather: Weather? = null,
    val isLoadingWeather: Boolean = false,
    val error: String? = null
)

@HiltViewModel
class WeatherViewModel @Inject constructor(
    private val repository: WeatherRepositoryImpl
) : ViewModel() {

    private val _state =
        MutableStateFlow(WeatherUiState())

    val state = _state.asStateFlow()

    private var searchJob: Job? = null

    fun onSearchQueryChange(query: String) {

        _state.update {
            it.copy(
                searchQuery = query,
                error = null
            )
        }

        searchJob?.cancel()

        if (query.length >= 3) {

            searchJob = viewModelScope.launch {

                try {

                    delay(500)

                    _state.update {
                        it.copy(
                            isLoadingSuggestions = true,
                            error = null
                        )
                    }

                    val results =
                        repository.searchCity(query)

                    _state.update {
                        it.copy(
                            suggestions = results,
                            isLoadingSuggestions = false
                        )
                    }

                } catch (e: Exception) {

                    _state.update {
                        it.copy(
                            isLoadingSuggestions = false,
                            error = "No internet connection"
                        )
                    }
                }
            }

        } else {

            _state.update {
                it.copy(
                    suggestions = emptyList()
                )
            }
        }
    }

    fun searchWeather() {

        val firstSuggestion =
            state.value.suggestions.firstOrNull()

        if (firstSuggestion != null) {

            onCitySelected(firstSuggestion)

        } else {

            _state.update {
                it.copy(
                    error = "No matching city found"
                )
            }
        }
    }

    fun onCitySelected(
        result: GeocodingResult
    ) {

        _state.update {
            it.copy(
                searchQuery = result.name,
                suggestions = emptyList(),
                isLoadingWeather = true,
                error = null
            )
        }

        viewModelScope.launch {

            try {

                val weather =
                    repository.getWeather(
                        result.latitude,
                        result.longitude,
                        result.name
                    )

                if (weather != null) {

                    _state.update {
                        it.copy(
                            selectedWeather = weather,
                            isLoadingWeather = false
                        )
                    }

                } else {

                    _state.update {
                        it.copy(
                            isLoadingWeather = false,
                            error = "Failed to fetch weather data"
                        )
                    }
                }

            } catch (e: Exception) {

                _state.update {
                    it.copy(
                        isLoadingWeather = false,
                        error = "No internet connection"
                    )
                }
            }
        }
    }
}