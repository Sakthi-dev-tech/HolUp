package com.adormantsakthi.holup.ui.components.forStatistics

import android.content.pm.ApplicationInfo
import android.graphics.drawable.Drawable
import androidx.compose.foundation.Image
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.adormantsakthi.holup.functions.statistics.AppUsageTracker
import com.google.accompanist.drawablepainter.rememberDrawablePainter

@Composable
fun TimeUsageForApp (
    icon: Drawable,
    name: String,
    appInfo: ApplicationInfo
) {
    val timeUsedToday = AppUsageTracker(LocalContext.current).getTodayUsageStats(listOf(appInfo.packageName))

    val (hours, minutes) = convertToHoursAndMinutes(timeUsedToday)

    Row (
        modifier = Modifier
            .fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Image(
            painter = rememberDrawablePainter(drawable = icon),
            contentDescription = "App Icon",
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
                ("$hours hours $minutes minutes"),
                style = MaterialTheme.typography.labelSmall
            )
        }
    }

    HorizontalDivider(thickness = 2.dp, color = Color.DarkGray)
}
fun convertToHoursAndMinutes(decimalTime: Float): Pair<Int, Int> {
    val hours = decimalTime.toInt()
    val minutes = ((decimalTime - hours) * 60).toInt()
    return Pair(hours, minutes)
}
