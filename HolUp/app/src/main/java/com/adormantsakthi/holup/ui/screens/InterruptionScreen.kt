package com.adormantsakthi.holup.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun InterruptionScreen(
    onDismiss: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Yellow)  // Semi-transparent background
            .padding(16.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = "This is your overlay!",
            color = Color.White,
            fontSize = 24.sp
        )

        Button(
            onClick = onDismiss
        ) { }
    }
}
