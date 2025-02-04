package com.adormantsakthi.holup.ui.components.forStatistics

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.adormantsakthi.holup.functions.statistics.AppUsageStatsHelper
import com.adormantsakthi.holup.functions.statistics.AppUsageTracker
import com.adormantsakthi.holup.functions.statistics.TimeRangeHelper
import com.adormantsakthi.holup.storage.LimitedAppsStorage
import com.patrykandpatrick.vico.compose.cartesian.CartesianChartHost
import com.patrykandpatrick.vico.compose.cartesian.axis.rememberBottom
import com.patrykandpatrick.vico.compose.cartesian.axis.rememberStart
import com.patrykandpatrick.vico.compose.cartesian.layer.rememberLineCartesianLayer
import com.patrykandpatrick.vico.compose.cartesian.rememberCartesianChart
import com.patrykandpatrick.vico.core.cartesian.axis.HorizontalAxis
import com.patrykandpatrick.vico.core.cartesian.axis.VerticalAxis
import com.patrykandpatrick.vico.core.cartesian.data.CartesianChartModelProducer
import com.patrykandpatrick.vico.core.cartesian.data.lineSeries

@Composable
fun Graph(selectedOption: String, isSubbed: Boolean) {
    val modelProducer = remember { CartesianChartModelProducer() }
    val dates = remember { mutableListOf<String>() }
    val isLoading = remember { mutableStateOf(true) }

    val context = LocalContext.current
    val usageHelper = remember { AppUsageStatsHelper(context) }
    val timeRangeHelper = remember { TimeRangeHelper() }

    // Data holders for different time periods
    val dailyStatsThisWeek = remember { mutableListOf<Float>() }
    val dailyStatsLastWeek = remember { mutableListOf<Float>() }
    val dailyStatsThisMonth = remember { mutableListOf<Float>() }

    val listOfAppPackageNames = LimitedAppsStorage(context).getTargetPackages().toList()

    val verticalAxis = VerticalAxis.rememberStart(
        valueFormatter = { _, value, _ ->
            val hours = value.toInt()
            val minutes = ((value - hours) * 60).toInt()
            "$hours h $minutes m"
        }
    )

    val horizontalAxis = HorizontalAxis.rememberBottom(
        valueFormatter = { _, index, _ ->
            dates.getOrNull(index.toInt()) ?: ""
        }
    )

    // Clear previous data
    dailyStatsThisWeek.clear()
    dailyStatsLastWeek.clear()
    dailyStatsThisMonth.clear()

    // Get time ranges
    val todayTimeUsed = AppUsageTracker(context).getTodayUsageStats(listOfAppPackageNames)
    val thisWeekRanges = timeRangeHelper.getThisWeekDailyRanges()
    val lastWeekRanges = timeRangeHelper.getLastWeekDailyRanges()

    // Calculate stats for this week
    thisWeekRanges.forEach { (start, end) ->
        val totalForegroundTime = usageHelper.getDailyUsageStats(start, end, listOfAppPackageNames)
        dailyStatsThisWeek.add(totalForegroundTime)
    }

    dailyStatsThisWeek.removeAt(dailyStatsThisWeek.size - 1)
    dailyStatsThisWeek.add(todayTimeUsed)

    // Calculate stats for last week
    lastWeekRanges.forEach { (start, end) ->
        val totalForegroundTime = usageHelper.getDailyUsageStats(start, end, listOfAppPackageNames)
        dailyStatsLastWeek.add(totalForegroundTime)
    }

    if (isSubbed) {
        // Effect to load data when selected option changes
        LaunchedEffect(selectedOption) {
            isLoading.value = true

            // Update dates and data based on selected option
            when (selectedOption) {
                "This Week" -> {
                    dates.clear()
                    dates.addAll(listOf("Mon", "Tue", "Wed", "Thu", "Fri", "Sat", "Sun"))
                    modelProducer.runTransaction {
                        lineSeries { series(dailyStatsThisWeek) }
                    }
                }
                "Last Week" -> {
                    dates.clear()
                    dates.addAll(listOf("Mon", "Tue", "Wed", "Thu", "Fri", "Sat", "Sun"))
                    modelProducer.runTransaction {
                        lineSeries { series(dailyStatsLastWeek) }
                    }
                }
            }

            isLoading.value = false
        }
    }

    Box(
        modifier = Modifier
            .fillMaxWidth(0.9f)
            .height(200.dp)
            .background(Color.Black),
        contentAlignment = Alignment.Center
    ) {
        if (isLoading.value) {
            CircularProgressIndicator(
                modifier = Modifier.width(48.dp),
                color = MaterialTheme.colorScheme.primary,
                strokeWidth = 4.dp
            )
        } else {
            CartesianChartHost(
                rememberCartesianChart(
                    rememberLineCartesianLayer(),
                    startAxis = verticalAxis,
                    bottomAxis = horizontalAxis,
                ),
                modelProducer,
                modifier = Modifier
                    .background(MaterialTheme.colorScheme.background)
            )
        }
    }
}