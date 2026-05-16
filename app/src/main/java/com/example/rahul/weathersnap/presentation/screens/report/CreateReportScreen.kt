package com.example.rahul.weathersnap.presentation.screens.report

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.foundation.layout.imePadding
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import com.example.rahul.weathersnap.presentation.screens.camera.CameraResult
import com.example.rahul.weathersnap.presentation.screens.camera.CameraResultStore
import com.example.rahul.weathersnap.ui.theme.GreenAccent
import com.example.rahul.weathersnap.ui.theme.TempYellow

@Composable
fun CreateReportScreen(
    viewModel: CreateReportViewModel = hiltViewModel(),
    onNavigateToCamera: () -> Unit,

    onNavigateBack: () -> Unit,
    onSaveSuccess: () -> Unit
) {

    val state by viewModel.state.collectAsState()

    val cameraResult =
        CameraResultStore.result.collectAsState().value

    LaunchedEffect(cameraResult) {

        if (cameraResult is CameraResult.Success) {

            viewModel.onImageCaptured(cameraResult.uri)

            CameraResultStore.reset()
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF0A4A42))
    ) {

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(12.dp)
                .statusBarsPadding()
                .imePadding()
                .verticalScroll(rememberScrollState())
        ) {

            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(18.dp),
                colors = CardDefaults.cardColors(
                    containerColor = Color.Transparent
                )
            ) {

                Box(
                    modifier = Modifier
                        .background(
                            Brush.horizontalGradient(
                                listOf(
                                    Color(0xFFC8D98A),
                                    Color(0xFFB9D4A8)
                                )
                            )
                        )
                        .padding(18.dp)
                ) {

                    Row(
                        verticalAlignment =
                            Alignment.CenterVertically
                    ) {

                        Column(
                            modifier = Modifier.weight(1f)
                        ) {

                            Text(
                                text = "Create Report",
                                fontSize = 28.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color(0xFF183020)
                            )

                            Text(
                                text = "Capture, compress, annotate",
                                color = Color.DarkGray,
                                fontSize = 13.sp
                            )
                        }

                        Button(


                            onClick = {

                                state.capturedImageUri?.path?.let {
                                    java.io.File(it).delete()
                                }

                                onNavigateBack()
                            },
                            shape = RoundedCornerShape(50.dp),
                            colors = ButtonDefaults.buttonColors(
                                containerColor =
                                    Color(0xFF233300)
                            )
                        ) {

                            Text("Back")
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(18.dp),
                colors = CardDefaults.cardColors(
                    containerColor = Color(0xFF35352E)
                )
            ) {

                Column(
                    modifier = Modifier.padding(16.dp)
                ) {

                    Row {

                        Column(
                            modifier = Modifier.weight(1f)
                        ) {

                            Text(
                                text = state.cityName,
                                color = Color.White,
                                fontSize = 24.sp,
                                fontWeight = FontWeight.Bold
                            )

                            Text(
                                text = state.condition,
                                color = Color.LightGray
                            )
                        }

                        Text(
                            text =
                                "${state.temp.toInt()}°C",
                            color = TempYellow,
                            fontSize = 30.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }

                    Spacer(modifier = Modifier.height(14.dp))

                    Row(
                        horizontalArrangement =
                            Arrangement.spacedBy(10.dp)
                    ) {

                        InfoBox(
                            "Humidity",
                            "${state.humidity}%",
                            Modifier.weight(1f),
                            Color(0xFF31463B)
                        )

                        InfoBox(
                            "Wind",
                            "${state.wind} m/s",
                            Modifier.weight(1f),
                            Color(0xFF32424A)
                        )

                        InfoBox(
                            "Pressure",
                            "${state.pressure}",
                            Modifier.weight(1f),
                            Color(0xFF4A4130)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(18.dp),
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
                            .height(220.dp)
                            .clip(
                                RoundedCornerShape(12.dp)
                            )
                            .background(
                                Brush.horizontalGradient(
                                    listOf(
                                        Color(0xFF50534A),
                                        Color(0xFF5D6500)
                                    )
                                )
                            )
                            .clickable {
                                onNavigateToCamera()
                            },
                        contentAlignment = Alignment.Center
                    ) {

                        if (state.capturedImageUri != null) {

                            AsyncImage(
                                model =
                                    state.capturedImageUri,
                                contentDescription = null,
                                modifier =
                                    Modifier.fillMaxSize(),
                                contentScale =
                                    ContentScale.Crop
                            )

                        } else {

                            Text(
                                text = "Photo preview",
                                color = Color.White
                            )
                        }
                    }

                    Spacer(
                        modifier = Modifier.height(16.dp)
                    )

                    Button(
                        onClick = onNavigateToCamera,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(56.dp),
                        colors =
                            ButtonDefaults.buttonColors(
                                containerColor =
                                    Color(0xFFC8D98A)
                            ),
                        shape = RoundedCornerShape(50.dp)
                    ) {

                        Text(
                            text = "Capture Photo",
                            color = Color.Black
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(18.dp),
                colors = CardDefaults.cardColors(
                    containerColor = Color(0xFF35352E)
                )
            ) {

                Column(
                    modifier = Modifier.padding(16.dp)
                ) {

                    Text(
                        text = "Field Notes",
                        color = Color.White,
                        fontWeight = FontWeight.Bold
                    )

                    Spacer(
                        modifier = Modifier.height(12.dp)
                    )

                    OutlinedTextField(
                        value = state.notes,
                        onValueChange =
                            viewModel::onNotesChange,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(120.dp),
                        placeholder = {
                            Text("Notes")
                        },
                        shape = RoundedCornerShape(12.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(18.dp))

            Button(
                onClick = {
                    viewModel.saveReport(
                        onSaveSuccess
                    )
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                shape = RoundedCornerShape(50.dp),
                enabled =
                    state.capturedImageUri != null,
                colors = ButtonDefaults.buttonColors(
                    containerColor =
                        Color(0xFFC8D98A)
                )
            ) {

                Text(
                    text = "Save Report",
                    color = Color.Black,
                    fontWeight = FontWeight.Bold
                )
            }

            Spacer(modifier = Modifier.height(20.dp))
        }
    }
}

@Composable
fun InfoBox(
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

        Spacer(modifier = Modifier.height(4.dp))

        Text(
            text = value,
            color = GreenAccent,
            fontWeight = FontWeight.Bold,
            fontSize = 14.sp
        )
    }
}