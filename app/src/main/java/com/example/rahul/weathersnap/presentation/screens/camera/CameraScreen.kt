package com.example.rahul.weathersnap.presentation.screens.camera

import android.Manifest
import android.content.Context
import android.net.Uri
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageCapture
import androidx.camera.core.ImageCaptureException
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.io.File
import java.util.concurrent.Executor
import java.util.concurrent.Executors

@Composable
fun CameraScreen(
    onImageCaptured: () -> Unit,
    onClose: () -> Unit
) {

    val context = LocalContext.current

    val lifecycleOwner =
        LocalLifecycleOwner.current

    val cameraExecutor = remember {
        Executors.newSingleThreadExecutor()
    }

    val imageCapture = remember {
        ImageCapture.Builder().build()
    }

    val preview = remember {
        Preview.Builder().build()
    }

    val cameraSelector =
        CameraSelector.DEFAULT_BACK_CAMERA

    var hasCameraPermission by remember {
        mutableStateOf(false)
    }

    val launcher =
        rememberLauncherForActivityResult(
            contract =
                ActivityResultContracts.RequestPermission()
        ) { granted ->

            hasCameraPermission = granted
        }

    LaunchedEffect(Unit) {
        launcher.launch(Manifest.permission.CAMERA)
    }

    DisposableEffect(Unit) {

        onDispose {
            cameraExecutor.shutdown()
        }
    }

    if (hasCameraPermission) {

        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color(0xFF20271F))
        ) {


            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(
                        top = 48.dp,
                        start = 16.dp,
                        end = 16.dp
                    ),

                verticalAlignment =
                    Alignment.CenterVertically
            ) {

                Text(
                    text = "Custom Camera",

                    color = Color.White,

                    style =
                        MaterialTheme.typography.titleLarge,

                    modifier = Modifier.weight(1f)
                )

                OutlinedButton(

                    onClick = onClose,

                    shape =
                        RoundedCornerShape(50.dp),

                    colors =
                        ButtonDefaults.outlinedButtonColors(
                            contentColor = Color.White
                        )

                ) {

                    Text("Close")
                }
            }


            Box(
                modifier = Modifier
                    .align(Alignment.Center)
                    .fillMaxWidth()
                    .height(420.dp)
                    .padding(horizontal = 20.dp)
                    .clip(RoundedCornerShape(20.dp))
            ) {

                AndroidView(

                    factory = { ctx ->

                        val previewView =
                            PreviewView(ctx).apply {

                                scaleType =
                                    PreviewView.ScaleType.FILL_CENTER

                                implementationMode =
                                    PreviewView.ImplementationMode.COMPATIBLE
                            }

                        val cameraProviderFuture =
                            ProcessCameraProvider.getInstance(ctx)

                        cameraProviderFuture.addListener({

                            val cameraProvider =
                                cameraProviderFuture.get()

                            try {

                                cameraProvider.unbindAll()

                                cameraProvider.bindToLifecycle(
                                    lifecycleOwner,
                                    cameraSelector,
                                    preview,
                                    imageCapture
                                )

                                preview.setSurfaceProvider(
                                    previewView.surfaceProvider
                                )

                            } catch (e: Exception) {

                                Log.e(
                                    "CameraScreen",
                                    "Camera binding failed",
                                    e
                                )
                            }

                        }, ContextCompat.getMainExecutor(ctx))

                        previewView

                    },

                    modifier = Modifier.fillMaxSize()
                )
            }


            Button(

                onClick = {

                    takePhoto(

                        imageCapture = imageCapture,

                        executor = cameraExecutor,

                        context = context,

                        onImageCaptured = { uri ->

                            CoroutineScope(
                                Dispatchers.Main
                            ).launch {

                                CameraResultStore.setResult(uri)

                                onImageCaptured()
                            }
                        }
                    )
                },

                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .padding(
                        start = 24.dp,
                        end = 24.dp,
                        bottom = 40.dp
                    )
                    .fillMaxWidth()
                    .height(56.dp),

                shape = RoundedCornerShape(50.dp),

                colors =
                    ButtonDefaults.buttonColors(
                        containerColor = Color(0xFFC6D57E)
                    )

            ) {

                Text(
                    text = "Capture",
                    color = Color.Black
                )
            }
        }

    } else {

        Box(
            modifier = Modifier.fillMaxSize(),

            contentAlignment = Alignment.Center
        ) {

            Text(
                text = "Camera permission required",

                color = Color.White
            )
        }
    }
}

private fun takePhoto(
    imageCapture: ImageCapture,
    executor: Executor,
    context: Context,
    onImageCaptured: (Uri) -> Unit
) {

    val photoFile = File(
        context.cacheDir,
        "captured_${System.currentTimeMillis()}.jpg"
    )

    val outputOptions =
        ImageCapture.OutputFileOptions.Builder(photoFile)
            .build()

    imageCapture.takePicture(

        outputOptions,

        executor,

        object : ImageCapture.OnImageSavedCallback {

            override fun onImageSaved(
                output: ImageCapture.OutputFileResults
            ) {

                val savedUri =
                    Uri.fromFile(photoFile)

                onImageCaptured(savedUri)
            }

            override fun onError(
                exc: ImageCaptureException
            ) {

                Log.e(
                    "CameraScreen",
                    "Photo capture failed: ${exc.message}",
                    exc
                )
            }
        }
    )
}