package com.adormantsakthi.holup.ui.screens.OnBoardingScreens

import android.app.Activity
import android.content.Intent
import android.os.Build
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.app.ActivityCompat
import com.adormantsakthi.holup.functions.NotificationAlarmReceiver
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.LottieConstants
import com.airbnb.lottie.compose.rememberLottieComposition
import kotlinx.coroutines.delay

@Composable
fun OnboardingScreen2(showSkipButton: MutableState<Boolean>) {

    var firstTextVisible by remember { mutableStateOf(false) }
    var secondTextVisible by remember { mutableStateOf(false) }

    val context = LocalContext.current
    val activity = context as Activity

    LaunchedEffect(Unit) {
        // Delay the appearance of the second text
        delay(1000)  // Wait for 1 second before showing the second text
        firstTextVisible = true
        delay(1000)
        secondTextVisible = true

        delay(1000)

        showSkipButton.value = true

        delay(2000)

        val accessibilitySettings = Intent(android.provider.Settings.ACTION_ACCESSIBILITY_SETTINGS)
        context.startActivity(accessibilitySettings)

        val usageAccessSettings = Intent(android.provider.Settings.ACTION_USAGE_ACCESS_SETTINGS)
        context.startActivity(usageAccessSettings)

        val overlaySettings = Intent(android.provider.Settings.ACTION_MANAGE_OVERLAY_PERMISSION)
        context.startActivity(overlaySettings)

        NotificationAlarmReceiver.requestExactAlarmPermission(context)

        delay(1000)

        val NOTIFICATION_PERMISSION_REQUEST_CODE = 100
        if ( Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            ActivityCompat.requestPermissions(
                activity,
                arrayOf(android.Manifest.permission.POST_NOTIFICATIONS),
                NOTIFICATION_PERMISSION_REQUEST_CODE
            )
        }

    }

    Column (
        modifier = Modifier
            .fillMaxWidth()
            .background(Color.Transparent),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f),
            contentAlignment = Alignment.TopStart
        ) {
            androidx.compose.animation.AnimatedVisibility(
                visible = firstTextVisible,
                enter = androidx.compose.animation.fadeIn(),
                exit = androidx.compose.animation.fadeOut()
            ) {
                Text(
                    "Before we continue,",
                    style = MaterialTheme.typography.labelMedium.copy(
                        MaterialTheme.colorScheme.primary,
                        fontSize = 52.sp
                    ),
                    modifier = Modifier
                )
            }
        }


        Box(
            modifier = Modifier
                .fillMaxWidth()
                .weight(0.8f),
            contentAlignment = Alignment.Center
        ) {
            Box(
                contentAlignment = Alignment.Center
            ) {
                PermissionAnimation()
            }
        }

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f),
            contentAlignment = Alignment.CenterEnd
        ) {
            androidx.compose.animation.AnimatedVisibility(
                visible = secondTextVisible,
                enter = androidx.compose.animation.fadeIn(),
                exit = androidx.compose.animation.fadeOut()
            ) {
                Text(
                    "We Need Some Permissions To Run The App As Intended",
                    style = MaterialTheme.typography.labelMedium.copy(
                        MaterialTheme.colorScheme.primary,
                        fontSize = 35.sp,
                        textAlign = TextAlign.End
                    ),
                )
            }
        }
    }
}

@Composable
fun PermissionAnimation() {
    val composition by rememberLottieComposition(spec = LottieCompositionSpec.Url("https://lottie.host/3dadd603-3ad4-43b3-9c50-d5dfab0fb2d7/DeTWk4hSfA.json"))

    if (composition != null) {
        LottieAnimation(composition = composition, iterations = LottieConstants.IterateForever)
    } else {
        CircularProgressIndicator(
            color = Color.White, // Custom color
            modifier = Modifier
                .size(50.dp), // Custom size
            strokeWidth = 6.dp // Custom stroke width
        )
    }
}