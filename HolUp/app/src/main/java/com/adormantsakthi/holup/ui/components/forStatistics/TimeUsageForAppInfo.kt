package com.adormantsakthi.holup.ui.components.forStatistics

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp

@Composable
fun TimeUsageForApp (
    icon: ImageVector,
    name: String,
    usageTime: String
) {
    Row (
        modifier = Modifier
            .fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Icon(
            imageVector = icon,
            tint = Color.Black,
            contentDescription = "Dummy",
            modifier = Modifier
                .size(75.dp)
                .padding(start = 30.dp)
        )

        Column(
            modifier = Modifier
                .padding(end = 40.dp)

        ) {
            Text(
                name,
                style = MaterialTheme.typography.labelMedium
            )

            Text(
                usageTime,
                style = MaterialTheme.typography.labelSmall
            )
        }
    }

    HorizontalDivider(thickness = 2.dp, color = Color.DarkGray)
}