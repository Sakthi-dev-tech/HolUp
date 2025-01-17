package com.adormantsakthi.holup.ui.screens.Dialogs.forHome

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
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
fun AddAppsDialog(
    showDialog: MutableState<Boolean>,
    isAppBarVisible: MutableState<Boolean>,
    showNeedPermissionsDialog: MutableState<Boolean>,
) {
    if (showDialog.value) {
        isAppBarVisible.value = false
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.LightGray)
        ) {
            Column (
                modifier = Modifier
                    .fillMaxSize()
            ) {
                Icon(
                    imageVector = Icons.Filled.Close,
                    contentDescription = null,
                    tint = Color.Black,
                    modifier = Modifier
                        .padding(10.dp)
                        .size(40.dp)
                        .align(Alignment.End)
                        .clickable {
                            showDialog.value = false
                            if (!showNeedPermissionsDialog.value){
                                isAppBarVisible.value = true
                            }
                        }
                )

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f),
                    contentAlignment = Alignment.TopStart
                ) {
                    Text(
                        "Fight the distractions\nnow!",
                        style = MaterialTheme.typography.labelMedium.copy(
                            MaterialTheme.colorScheme.primary,
                            fontSize = 42.sp
                        ),
                        modifier = Modifier
                            .padding(start = 20.dp)
                    )
                }


                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(0.6f),
                    contentAlignment = Alignment.Center
                ) {
                    Box(
                        contentAlignment = Alignment.Center
                    ) {
                        GuyUsingPhoneAnimation()
                    }
                }

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(end = 10.dp)
                        .weight(1.4f),
                    contentAlignment = Alignment.CenterEnd
                ) {
                    Text(
                        "Limit apps under \"Add Apps\"\nin the Settings Page",
                        style = MaterialTheme.typography.labelMedium.copy(
                            MaterialTheme.colorScheme.primary,
                            fontSize = 32.sp,
                            textAlign = TextAlign.End
                        ),
                        modifier = Modifier
                            .padding(end = 20.dp)
                    )
                }
            }
        }
    }
}

@Composable
fun GuyUsingPhoneAnimation() {
    val composition by rememberLottieComposition(spec = LottieCompositionSpec.Url("https://lottie.host/657222f5-e78e-4519-a7cf-7be6344fbec7/Iof6Nipt40.json"))

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