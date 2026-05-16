package com.example.rahul.weathersnap.presentation.screens.weather

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.rahul.weathersnap.domain.model.Weather
import com.example.rahul.weathersnap.ui.theme.GrayText
import com.example.rahul.weathersnap.ui.theme.GreenAccent

@Composable
fun WeatherScreen(
    viewModel: WeatherViewModel = hiltViewModel(),
    onNavigateToCreateReport: (Weather) -> Unit,
    onNavigateToSavedReports: () -> Unit
) {

    val state by viewModel.state.collectAsState()

    val keyboardController =
        LocalSoftwareKeyboardController.current

    val focusManager =
        LocalFocusManager.current

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(
                        Color(0xFF20271F),
                        Color(0xFF111A12)
                    )
                )
            )
    ) {

        Column(
            modifier = Modifier
                .fillMaxSize()
                .statusBarsPadding()
                .imePadding()
                .padding(
                    horizontal = 12.dp,
                    vertical = 8.dp
                ),

            verticalArrangement =
                Arrangement.spacedBy(12.dp)
        ) {

            // HEADER
            Card(
                modifier = Modifier.fillMaxWidth(),

                shape = RoundedCornerShape(18.dp),

                elevation = CardDefaults.cardElevation(
                    defaultElevation = 6.dp
                ),

                colors = CardDefaults.cardColors(
                    containerColor = Color(0xFFC8D98A)
                )
            ) {

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),

                    verticalAlignment =
                        Alignment.CenterVertically
                ) {

                    Column(
                        modifier = Modifier.weight(1f)
                    ) {

                        Text(
                            text = "WeatherSnap",

                            fontSize = 28.sp,

                            fontWeight = FontWeight.Bold,

                            color = Color(0xFF2D3A0F)
                        )

                        Spacer(
                            modifier = Modifier.height(4.dp)
                        )

                        Text(
                            text =
                                "Live weather reports with camera evidence",

                            fontSize = 13.sp,

                            color = Color.DarkGray
                        )
                    }

                    Button(
                        onClick = onNavigateToSavedReports,

                        shape = RoundedCornerShape(12.dp),

                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color(0xFF556B12)
                        )
                    ) {

                        Text(
                            text = "Reports",
                            color = Color.White
                        )
                    }
                }
            }

            // SEARCH CARD
            Card(
                modifier = Modifier.fillMaxWidth(),

                shape = RoundedCornerShape(16.dp),

                elevation = CardDefaults.cardElevation(
                    defaultElevation = 6.dp
                ),

                colors = CardDefaults.cardColors(
                    containerColor = Color(0xFF0F1208)
                )
            ) {

                Column(
                    modifier = Modifier.padding(12.dp)
                ) {

                    Row(
                        verticalAlignment =
                            Alignment.CenterVertically
                    ) {

                        OutlinedTextField(

                            value = state.searchQuery,

                            onValueChange = {
                                viewModel.onSearchQueryChange(it)
                            },

                            modifier = Modifier
                                .weight(1f)
                                .height(56.dp),

                            placeholder = {

                                Text(
                                    text = "City",
                                    color = GrayText
                                )
                            },

                            singleLine = true,

                            colors =
                                OutlinedTextFieldDefaults.colors(

                                    focusedBorderColor =
                                        Color.LightGray,

                                    unfocusedBorderColor =
                                        Color.Gray,

                                    focusedTextColor =
                                        Color.White,

                                    unfocusedTextColor =
                                        Color.White,

                                    focusedContainerColor =
                                        Color.Transparent,

                                    unfocusedContainerColor =
                                        Color.Transparent
                                ),

                            shape =
                                RoundedCornerShape(10.dp)
                        )

                        Spacer(
                            modifier = Modifier.width(10.dp)
                        )

                        Button(

                            onClick = {

                                keyboardController?.hide()

                                focusManager.clearFocus()

                                viewModel.searchWeather()
                            },

                            modifier = Modifier
                                .height(56.dp),

                            shape =
                                RoundedCornerShape(50.dp),

                            colors =
                                ButtonDefaults.buttonColors(
                                    containerColor =
                                        Color(0xFFC8D98A)
                                )

                        ) {

                            Text(
                                text = "Search",

                                color = Color.Black,

                                fontWeight =
                                    FontWeight.SemiBold
                            )
                        }
                    }

                    Spacer(
                        modifier = Modifier.height(8.dp)
                    )

                    Text(
                        text =
                            "Enter more than 2 letters to start city suggestions.",

                        color = GrayText,

                        fontSize = 12.sp
                    )
                }
            }

            // SUGGESTIONS
            if (
                state.suggestions.isNotEmpty()
                || state.isLoadingSuggestions
            ) {

                Card(

                    modifier = Modifier.fillMaxWidth(),

                    shape = RoundedCornerShape(16.dp),

                    elevation = CardDefaults.cardElevation(
                        defaultElevation = 6.dp
                    ),

                    colors = CardDefaults.cardColors(
                        containerColor = Color(0xFF35352E)
                    )

                ) {

                    Column(
                        modifier = Modifier.padding(12.dp)
                    ) {

                        if (state.isLoadingSuggestions) {

                            Row(
                                verticalAlignment =
                                    Alignment.CenterVertically
                            ) {

                                CircularProgressIndicator(
                                    modifier = Modifier.size(18.dp),

                                    strokeWidth = 2.dp,

                                    color = GreenAccent
                                )

                                Spacer(
                                    modifier = Modifier.width(10.dp)
                                )

                                Text(
                                    text = "Finding cities...",

                                    color = Color.White,

                                    fontSize = 14.sp
                                )
                            }

                            Spacer(
                                modifier = Modifier.height(12.dp)
                            )
                        }

                        LazyColumn(

                            verticalArrangement =
                                Arrangement.spacedBy(10.dp),

                            modifier = Modifier
                                .fillMaxWidth()
                                .heightIn(max = 260.dp)

                        ) {

                            items(state.suggestions) { suggestion ->

                                Card(

                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .clickable {

                                            keyboardController?.hide()

                                            focusManager.clearFocus()

                                            viewModel.onCitySelected(
                                                suggestion
                                            )
                                        },

                                    shape = RoundedCornerShape(18.dp),

                                    colors = CardDefaults.cardColors(
                                        containerColor = Color(0xFF2E2E2A)
                                    ),

                                    border = BorderStroke(
                                        width = 1.dp,
                                        color = Color(0xFF4A4A43)
                                    )

                                ) {

                                    Text(

                                        text =
                                            "${suggestion.name}, ${suggestion.country}",

                                        modifier = Modifier.padding(
                                            vertical = 16.dp,
                                            horizontal = 18.dp
                                        ),

                                        color = Color.White,

                                        fontSize = 14.sp
                                    )
                                }
                            }
                        }
                    }
                }
            }

            // WEATHER CARD
            if (state.selectedWeather != null) {

                WeatherCard(
                    weather = state.selectedWeather!!,

                    onCreateReport = {
                        onNavigateToCreateReport(
                            state.selectedWeather!!
                        )
                    }
                )

            } else {

                Card(
                    modifier = Modifier.fillMaxWidth(),

                    shape = RoundedCornerShape(18.dp),

                    elevation = CardDefaults.cardElevation(
                        defaultElevation = 8.dp
                    ),

                    colors = CardDefaults.cardColors(
                        containerColor = Color(0xFF35352E)
                    )
                ) {

                    Column(
                        modifier = Modifier.padding(16.dp)
                    ) {

                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(130.dp)
                                .background(
                                    brush = Brush.horizontalGradient(
                                        colors = listOf(
                                            Color(0xFF5B6413),
                                            Color(0xFF2E5F4C),
                                            Color(0xFF176E69)
                                        )
                                    ),

                                    shape = RoundedCornerShape(14.dp)
                                ),

                            contentAlignment = Alignment.Center
                        ) {

                            Text(
                                text = "Search. Capture. Save.",

                                color = Color.White,

                                fontSize = 20.sp,

                                fontWeight = FontWeight.SemiBold
                            )
                        }

                        Spacer(
                            modifier = Modifier.height(16.dp)
                        )

                        Text(
                            text = "No weather loaded",

                            color = Color.White,

                            fontSize = 24.sp,

                            fontWeight = FontWeight.Bold
                        )

                        Spacer(
                            modifier = Modifier.height(8.dp)
                        )

                        Text(
                            text =
                                "Enter more than 2 letters, choose a city, then search.",

                            color = Color.LightGray,

                            fontSize = 15.sp,

                            lineHeight = 22.sp
                        )
                    }
                }
            }

            // LOADING
            if (
                state.isLoadingWeather
                || state.isLoadingSuggestions
            ) {

                Box(
                    modifier = Modifier.fillMaxWidth(),

                    contentAlignment =
                        Alignment.Center
                ) {

                    CircularProgressIndicator(
                        color = GreenAccent
                    )
                }
            }

            // ERROR
            state.error?.let { error ->

                Text(
                    text = error,
                    color = Color.Red
                )
            }
        }
    }
}

@Composable
fun WeatherCard(
    weather: Weather,
    onCreateReport: () -> Unit
) {

    Card(

        modifier = Modifier.fillMaxWidth(),

        shape = RoundedCornerShape(18.dp),

        elevation = CardDefaults.cardElevation(
            defaultElevation = 8.dp
        ),

        colors = CardDefaults.cardColors(
            containerColor = Color(0xFF35352E)
        )

    ) {

        Column(
            modifier = Modifier.padding(16.dp)
        ) {

            Row(
                verticalAlignment =
                    Alignment.CenterVertically
            ) {

                Column(
                    modifier = Modifier.weight(1f)
                ) {

                    Text(
                        text = weather.cityName,

                        color = Color.White,

                        fontSize = 24.sp,

                        fontWeight = FontWeight.Bold
                    )

                    Spacer(
                        modifier = Modifier.height(4.dp)
                    )

                    Text(
                        text = weather.condition,

                        color = GrayText,

                        fontSize = 15.sp
                    )
                }

                Box(

                    modifier = Modifier
                        .shadow(
                            6.dp,
                            RoundedCornerShape(12.dp)
                        )
                        .background(
                            Color(0xFF7C8F14),
                            RoundedCornerShape(12.dp)
                        )
                        .padding(
                            horizontal = 18.dp,
                            vertical = 12.dp
                        )

                ) {

                    Text(
                        text =
                            "${weather.temperature.toInt()}°C",

                        color = Color.White,

                        fontSize = 26.sp,

                        fontWeight = FontWeight.Bold
                    )
                }
            }

            Spacer(
                modifier = Modifier.height(16.dp)
            )

            Row(
                horizontalArrangement =
                    Arrangement.spacedBy(10.dp)
            ) {

                WeatherStatCard(
                    title = "Humidity",
                    value = "${weather.humidity}%",

                    modifier = Modifier.weight(1f),

                    color = Color(0xFF2B4235)
                )

                WeatherStatCard(
                    title = "Wind",
                    value =
                        "${weather.windSpeed} m/s",

                    modifier = Modifier.weight(1f),

                    color = Color(0xFF2A3A42)
                )

                WeatherStatCard(
                    title = "Pressure",
                    value =
                        "${weather.pressure}",

                    modifier = Modifier.weight(1f),

                    color = Color(0xFF473A28)
                )
            }

            Spacer(
                modifier = Modifier.height(14.dp)
            )

            Row(

                modifier = Modifier
                    .fillMaxWidth()
                    .background(
                        Color(0xFF4A4A43),
                        RoundedCornerShape(10.dp)
                    )
                    .padding(14.dp),

                horizontalArrangement =
                    Arrangement.SpaceBetween

            ) {

                Text(
                    text = "Report readiness",
                    color = Color.LightGray
                )

                Text(
                    text =
                        "Camera and Room DB enabled",

                    color = Color.White,

                    fontWeight = FontWeight.Bold,

                    fontSize = 12.sp
                )
            }

            Spacer(
                modifier = Modifier.height(18.dp)
            )

            Button(

                onClick = onCreateReport,

                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),

                shape = RoundedCornerShape(50.dp),

                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFFC8D98A)
                )

            ) {

                Text(
                    text = "Create Report",

                    color = Color.Black,

                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}

@Composable
fun WeatherStatCard(
    title: String,
    value: String,
    modifier: Modifier = Modifier,
    color: Color
) {

    Column(

        modifier = modifier
            .background(
                color,
                RoundedCornerShape(12.dp)
            )
            .padding(12.dp),

        verticalArrangement =
            Arrangement.spacedBy(6.dp)

    ) {

        Text(
            text = title,

            color = Color.LightGray,

            fontSize = 12.sp
        )

        Text(
            text = value,

            color = GreenAccent,

            fontWeight = FontWeight.Bold,

            fontSize = 18.sp
        )
    }
}