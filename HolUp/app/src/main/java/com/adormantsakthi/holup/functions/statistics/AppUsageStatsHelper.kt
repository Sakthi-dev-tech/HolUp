package com.adormantsakthi.holup.functions.statistics
import android.app.usage.UsageStats
import android.app.usage.UsageStatsManager
import android.content.Context

class AppUsageStatsHelper(private val context: Context) {

    /**
     * Returns daily usage stats as a float in hours (e.g., 1.5f for 1 hour 30 minutes).
     * @param startTime Start time in milliseconds for a day.
     * @param endTime End time in milliseconds for a day.
     * @param packageNames List of package names to filter.
     * @return Total foreground time for all apps in packageNames for the day as a float in hours.
     */
    fun getDailyUsageStats(
        startTime: Long,
        endTime: Long,
        packageNames: List<String>
    ): Float {
        val usageStatsManager = context.getSystemService(Context.USAGE_STATS_SERVICE) as UsageStatsManager

        val usageStatsList: List<UsageStats> =
            usageStatsManager.queryUsageStats(UsageStatsManager.INTERVAL_DAILY, startTime, endTime)

        // Sum the total foreground time for the specified packages
        val totalTimeInMillis = usageStatsList.filter { it.packageName in packageNames }
            .sumOf { it.totalTimeInForeground }

        // Convert milliseconds to hours as a float
        return convertMillisecondsToHoursFloat(totalTimeInMillis)
    }

    /**
     * Converts milliseconds to hours as a float (e.g., 1.5f for 1 hour 30 minutes).
     * @param milliseconds Time in milliseconds.
     * @return Time in hours as a float.
     */
    private fun convertMillisecondsToHoursFloat(milliseconds: Long): Float {
        val totalMinutes = milliseconds / 60000.0 // Convert milliseconds to minutes
        val hours = totalMinutes / 60 // Get the total hours as a float
        return String.format("%.1f", hours).toFloat() // Round to 1 decimal place
    }
}

