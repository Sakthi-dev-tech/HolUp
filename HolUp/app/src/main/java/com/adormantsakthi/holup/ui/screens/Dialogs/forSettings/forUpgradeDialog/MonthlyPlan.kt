package com.adormantsakthi.holup.ui.screens.Dialogs.forSettings.forUpgradeDialog

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.LottieConstants
import com.airbnb.lottie.compose.rememberLottieComposition

@Composable
fun MonthlyPlan() {
    Card (
        colors = CardDefaults.cardColors(
            containerColor = Color.Transparent,
        ),
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Transparent)
    ) {
        Column (
            modifier = Modifier
                .fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
        ) {
            Box(
                modifier = Modifier
                    .size(200.dp),
                contentAlignment = Alignment.Center
            ) {
                CoffeeLottieAnimation()
            }

            Text(
                "Price of a cup of coffee/mth for Upgraded Focus",
                style = MaterialTheme.typography.labelMedium.copy(fontSize = 25.sp),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(20.dp),
                textAlign = TextAlign.Center
            )

            ElevatedButton(
                modifier = Modifier
                    .width(150.dp)
                    .height(55.dp),
                onClick = {},
                colors = ButtonColors(
                    containerColor = Color.Red,
                    contentColor = Color.White,
                    disabledContainerColor = Color.LightGray,
                    disabledContentColor = Color.LightGray
                )
            ) {
                Text(
                    "$2.98/month",
                    style = MaterialTheme.typography.labelSmall.copy(Color.White)
                )
            }
        }
    }
}

@Composable
fun CoffeeLottieAnimation() {
    val composition by rememberLottieComposition(spec = LottieCompositionSpec.Url("https://lottie.host/301d29dd-f737-40cc-bd45-4f06298cc60b/VpYeRWMKNC.json"))

    if (composition != null) {
        LottieAnimation(composition = composition, iterations = LottieConstants.IterateForever)
    } else {
        CircularProgressIndicator(
            color = Color.Black, // Custom color
            modifier = Modifier
                .size(80.dp), // Custom size
            strokeWidth = 6.dp // Custom stroke width
        )
    }
}