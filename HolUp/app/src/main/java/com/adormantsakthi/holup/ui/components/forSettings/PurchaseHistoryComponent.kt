package com.adormantsakthi.holup.ui.components.forSettings

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.adormantsakthi.holup.R
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.LottieConstants
import com.airbnb.lottie.compose.rememberLottieComposition
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun PurchaseHistoryComponent(
    purchaseID: String,
    purchaseTime: Long
) {
    Box(
        modifier = Modifier
            .padding(bottom = 20.dp)
            .fillMaxWidth(0.95f)
            .height(150.dp)
            .clip(RoundedCornerShape(30.dp))
            .background(MaterialTheme.colorScheme.secondary),
        contentAlignment = Alignment.Center
    ) {
        Row (
            modifier = Modifier
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Column for image and purchase name
            Column (
                modifier = Modifier
                    .padding(start = 10.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Box(
                    Modifier
                        .size(60.dp)
                ) {
                    if (purchaseID == "monthly_subscription") CoffeeLottieAnimation() else MealAnimation()
                }

                Text(
                    if (purchaseID == "monthly_subscription") "Cup of Coffee\nA Month" else "Meal A Year",
                    style = MaterialTheme.typography.labelSmall.copy(textAlign = TextAlign.Center, color = Color.Black)
                )
            }

            // Column for "Purchased On" title and the date
            Column (
                modifier = Modifier
                    .padding(end = 10.dp),
                verticalArrangement = Arrangement.spacedBy(6.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                Text(
                    "Purchased On",
                    style = MaterialTheme.typography.titleSmall.copy(textAlign = TextAlign.Center, color = Color.Black)
                )

                Text(
                    convertMillisecondsToDate(purchaseTime),
                    style = MaterialTheme.typography.labelSmall.copy(textAlign = TextAlign.Center, color = Color.Black)
                )
            }
        }
    }
}

fun convertMillisecondsToDate(milliseconds: Long): String {
    val date = Date(milliseconds)
    val format = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())
    return format.format(date)
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
                .size(30.dp), // Custom size
            strokeWidth = 6.dp // Custom stroke width
        )
    }
}

@Composable
fun MealAnimation() {
    val composition by rememberLottieComposition(spec = LottieCompositionSpec.Url("https://lottie.host/4dee2eb8-7ddd-43cf-aa3d-4295839c9c15/B5KpUBpwlN.json"))

    if (composition != null) {
        LottieAnimation(composition = composition, iterations = LottieConstants.IterateForever)
    } else {
        CircularProgressIndicator(
            color = Color.Black, // Custom color
            modifier = Modifier.size(30.dp), // Custom size
            strokeWidth = 6.dp // Custom stroke width
        )
    }
}