package com.example.rahul.weathersnap.presentation.screens.saved

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import com.example.rahul.weathersnap.ui.theme.GreenAccent
import java.io.File
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun SavedReportsScreen(
    viewModel: SavedReportsViewModel = hiltViewModel(),
    onBackClick: () -> Unit
) {

    val reports by viewModel.reports.collectAsState()

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF20271F))
    ) {

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(12.dp)
        ) {

            // HEADER
            Card(

                modifier = Modifier
                    .fillMaxWidth()
                    .statusBarsPadding(),

                shape = RoundedCornerShape(18.dp),

                colors = CardDefaults.cardColors(
                    containerColor = Color.Transparent
                )

            ) {

                Box(

                    modifier = Modifier
                        .background(

                            brush = Brush.horizontalGradient(
                                colors = listOf(
                                    Color(0xFFC8D98A),
                                    Color(0xFFB9D4A8)
                                )
                            )
                        )
                        .padding(18.dp)
                ) {

                    Row(
                        modifier = Modifier.fillMaxWidth(),

                        verticalAlignment =
                            Alignment.CenterVertically
                    ) {

                        Column(
                            modifier = Modifier.weight(1f)
                        ) {

                            Text(
                                text = "Saved Reports",

                                fontSize = 28.sp,

                                fontWeight = FontWeight.Bold,

                                color = Color(0xFF183020)
                            )

                            Spacer(
                                modifier = Modifier.height(4.dp)
                            )

                            Text(
                                text =
                                    "${reports.size} reports stored locally",

                                color = Color.DarkGray,

                                fontSize = 13.sp
                            )
                        }

                        Button(

                            onClick = onBackClick,

                            shape = RoundedCornerShape(50.dp),

                            colors = ButtonDefaults.buttonColors(
                                containerColor = Color(0xFF0A4A42)
                            )

                        ) {

                            Text(
                                text = "Back",
                                color = Color.White
                            )
                        }
                    }
                }
            }

            Spacer(
                modifier = Modifier.height(16.dp)
            )

            // EMPTY STATE
            if (reports.isEmpty()) {

                Card(

                    modifier = Modifier.fillMaxWidth(),

                    shape = RoundedCornerShape(18.dp),

                    elevation = CardDefaults.cardElevation(
                        defaultElevation = 6.dp
                    ),

                    colors = CardDefaults.cardColors(
                        containerColor = Color(0xFF3A392F)
                    )

                ) {

                    Column(
                        modifier = Modifier.padding(16.dp)
                    ) {

                        Box(

                            modifier = Modifier
                                .fillMaxWidth()
                                .height(140.dp)
                                .background(

                                    brush = Brush.horizontalGradient(
                                        colors = listOf(
                                            Color(0xFF5B6400),
                                            Color(0xFF5F6640)
                                        )
                                    ),

                                    shape = RoundedCornerShape(14.dp)
                                ),

                            contentAlignment =
                                Alignment.Center

                        ) {

                            Text(
                                text = "No reports yet",

                                color = Color.White,

                                fontSize = 18.sp,

                                fontWeight = FontWeight.SemiBold
                            )
                        }

                        Spacer(
                            modifier = Modifier.height(16.dp)
                        )

                        Text(

                            text =
                                "Create and save a weather report to see image, notes, and weather details here.",

                            color = Color.White,

                            fontSize = 15.sp,

                            lineHeight = 22.sp
                        )
                    }
                }

            } else {

                LazyColumn(

                    verticalArrangement =
                        Arrangement.spacedBy(14.dp)

                ) {

                    items(reports) { report ->

                        Card(

                            modifier = Modifier.fillMaxWidth(),

                            shape = RoundedCornerShape(18.dp),

                            colors = CardDefaults.cardColors(
                                containerColor = Color(0xFF35352E)
                            )

                        ) {

                            Column(
                                modifier = Modifier.padding(14.dp)
                            ) {

                                // IMAGE
                                AsyncImage(

                                    model = File(report.imagePath),

                                    contentDescription = null,

                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .height(220.dp),

                                    contentScale = ContentScale.Crop
                                )

                                Spacer(
                                    modifier = Modifier.height(14.dp)
                                )

                                // CITY + TEMP
                                Row(
                                    verticalAlignment =
                                        Alignment.CenterVertically
                                ) {

                                    Column(
                                        modifier = Modifier.weight(1f)
                                    ) {

                                        Text(
                                            text = report.cityName,

                                            color = Color.White,

                                            fontSize = 22.sp,

                                            fontWeight = FontWeight.Bold
                                        )

                                        Spacer(
                                            modifier = Modifier.height(4.dp)
                                        )

                                        Text(
                                            text = report.condition,

                                            color = Color.LightGray,

                                            fontSize = 15.sp
                                        )

                                        Spacer(
                                            modifier = Modifier.height(4.dp)
                                        )

                                        Text(

                                            text = SimpleDateFormat(
                                                "dd MMM yyyy, hh:mm a",
                                                Locale.getDefault()
                                            ).format(
                                                Date(report.timestamp)
                                            ),

                                            color = Color.Gray,

                                            fontSize = 12.sp
                                        )
                                    }

                                    Box(

                                        modifier = Modifier
                                            .background(
                                                Color(0xFF7C8F14),
                                                RoundedCornerShape(12.dp)
                                            )
                                            .padding(
                                                horizontal = 14.dp,
                                                vertical = 10.dp
                                            )

                                    ) {

                                        Text(

                                            text =
                                                "${report.temperature.toInt()}°C",

                                            color = Color.White,

                                            fontWeight = FontWeight.Bold,

                                            fontSize = 22.sp
                                        )
                                    }
                                }

                                Spacer(
                                    modifier = Modifier.height(14.dp)
                                )

                                // STORAGE INFO
                                Row(
                                    horizontalArrangement =
                                        Arrangement.spacedBy(10.dp)
                                ) {

                                    InfoCard(
                                        title = "Original",
                                        value =
                                            "${report.originalSize / 1024} KB",

                                        modifier = Modifier.weight(1f),

                                        color = Color(0xFF4D3D25)
                                    )

                                    InfoCard(
                                        title = "Compressed",
                                        value =
                                            "${report.compressedSize / 1024} KB",

                                        modifier = Modifier.weight(1f),

                                        color = Color(0xFF25453B)
                                    )
                                }

                                Spacer(
                                    modifier = Modifier.height(14.dp)
                                )

                                // NOTES
                                Box(

                                    modifier = Modifier
                                        .background(
                                            Color(0xFF4A4A43),
                                            RoundedCornerShape(10.dp)
                                        )
                                        .padding(
                                            horizontal = 12.dp,
                                            vertical = 10.dp
                                        )

                                ) {

                                    Text(
                                        text = report.notes,

                                        color = Color.White,

                                        fontSize = 13.sp
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun InfoCard(
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
            .padding(12.dp)

    ) {

        Text(
            text = title,

            color = Color.LightGray,

            fontSize = 11.sp
        )

        Spacer(
            modifier = Modifier.height(4.dp)
        )

        Text(
            text = value,

            color = GreenAccent,

            fontWeight = FontWeight.Bold,

            fontSize = 14.sp
        )
    }
}