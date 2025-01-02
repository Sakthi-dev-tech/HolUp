package com.adormantsakthi.holup.ui.components.Dialogs.forSettings

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Done
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CardElevation
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shadow
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun ComparisonChart() {
    Card (
        colors = CardDefaults.cardColors(
            containerColor = Color.Transparent,
        ),
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Transparent)
    ) {
        // row for columns
        Row (
            modifier = Modifier
                .fillMaxSize()
                .padding(top = 20.dp)
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceAround
        ) {
            // column for features
            Column (
                modifier = Modifier
                    .fillMaxHeight(),
                verticalArrangement = Arrangement.SpaceEvenly,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                val features = listOf("Limited apps", "Anti-Doomscroll", "Edit Pop-Up", "Re-Interruption", "Statistics")
                Text(
                    "Features",
                    style = MaterialTheme.typography.labelMedium.copy(
                        color = Color.Black,
                        shadow = Shadow(
                            color = Color.Gray,
                            offset = Offset(10f, 10f),
                            blurRadius = 5f
                        )
                    ),
                )

                features.forEach { feature ->
                    Text(
                        feature,
                        style = MaterialTheme.typography.labelSmall
                    )
                }
            }

            // row for columns of free and pro
            Row (
                horizontalArrangement = Arrangement.SpaceEvenly,
                modifier = Modifier
                    .fillMaxWidth()
            ) {
                Column (
                    modifier = Modifier
                        .fillMaxHeight()
                        .fillMaxWidth(0.4f)
                        .clip(RoundedCornerShape(15.dp))
                        .background(Color.LightGray),
                    verticalArrangement = Arrangement.SpaceEvenly,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    val free = listOf("2", false, false, false, false)
                    Text(
                        "Free",
                        style = MaterialTheme.typography.labelMedium.copy(
                            color = Color.Black,
                            shadow = Shadow(
                                color = Color.Gray,
                                offset = Offset(10f, 10f),
                                blurRadius = 5f
                            )
                        ),
                    )

                    free.forEach { element ->
                        if (element is String) {
                            Text(
                                element,
                                style = MaterialTheme.typography.labelSmall
                            )
                        } else if (element is Boolean) {
                            Icon(
                                imageVector = if (element) Icons.Default.Done else Icons.Default.Close,
                                contentDescription = null,
                                tint = if (element) Color.Green else Color.Red
                            )
                        }
                    }
                }

                Column (
                    modifier = Modifier
                        .fillMaxHeight()
                        .fillMaxWidth(0.5f),
                    verticalArrangement = Arrangement.SpaceEvenly,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    val plus = listOf("No Limit", true, true, true, true)
                    Text(
                        "Plus",
                        style = MaterialTheme.typography.labelMedium.copy(
                            color = Color.Black,
                            shadow = Shadow(
                                color = Color.Gray,
                                offset = Offset(10f, 10f),
                                blurRadius = 5f
                            )
                        ),
                    )

                    plus.forEach { element ->
                        if (element is String) {
                            Text(
                                element,
                                style = MaterialTheme.typography.labelSmall.copy(fontSize = 16.7.sp)
                            )
                        } else if (element is Boolean) {
                            Icon(
                                imageVector = if (element) Icons.Default.Done else Icons.Default.Close,
                                contentDescription = null,
                                tint = if (element) Color.Green else Color.Red
                            )
                        }
                    }
                }
            }
        }
    }
}