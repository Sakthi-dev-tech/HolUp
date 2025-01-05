package com.adormantsakthi.holup.ui.screens.OnBoardingScreens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.LottieConstants
import com.airbnb.lottie.compose.rememberLottieComposition
import kotlinx.coroutines.delay

@Composable
fun OnboardingScreen1 () {

    var firstTextVisible by remember { mutableStateOf(false) }
    var secondTextVisible by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        // Delay the appearance of the second text
        delay(1000)  // Wait for 1 second before showing the second text
        firstTextVisible = true
    }

    LaunchedEffect(Unit) {
        // Delay the appearance of the second text
        delay(2000)  // Wait for 2 second before showing the second text
        secondTextVisible = true
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
                    "Welcome!",
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
                UsingPhoneAnimation()
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
                        "Thank you for choosing HolUp!",
                        style = MaterialTheme.typography.labelMedium.copy(
                            MaterialTheme.colorScheme.primary,
                            fontSize = 40.sp,
                            textAlign = TextAlign.End
                        ),
                    )
                }
            }
    }
}

@Composable
fun UsingPhoneAnimation() {
    val composition by rememberLottieComposition(spec = LottieCompositionSpec.Url("https://lottie.host/7e8edc82-76dc-44b7-b456-ed57e61cb984/q30rXVQtSf.json"))

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