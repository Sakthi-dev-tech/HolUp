package com.adormantsakthi.holup.ui.components.forStatistics

import android.content.pm.PackageManager
import android.graphics.drawable.Drawable
import android.widget.ImageView
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.materialIcon
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import com.adormantsakthi.holup.R

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
                .padding(5.dp)
        )

        Column(
            modifier = Modifier
                .padding(20.dp)
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

    HorizontalDivider(thickness = 1.dp, color = Color.LightGray)
}