package com.adormantsakthi.holup.functions.statistics

import android.util.Log
import java.util.Calendar

class TimeRangeHelper {

    /**
     * Returns a list of start and end timestamps for each day in this week (starting Monday).
     */
    fun getThisWeekDailyRanges(): List<Pair<Long, Long>> {
        val calendar = Calendar.getInstance()
        calendar.firstDayOfWeek = Calendar.MONDAY
        calendar.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY)

        val today = Calendar.getInstance()
        val daysInThisWeek = mutableListOf<Pair<Long, Long>>()

        while (calendar.before(today) || calendar.get(Calendar.DAY_OF_YEAR) == today.get(Calendar.DAY_OF_YEAR)) {
            // Set the start of the day to 12:00 AM
            calendar.set(Calendar.HOUR_OF_DAY, 0)
            calendar.set(Calendar.MINUTE, 0)
            calendar.set(Calendar.SECOND, 0)
            calendar.set(Calendar.MILLISECOND, 0)
            val startOfDay = calendar.timeInMillis

            // Set the end of the day to 11:59 PM
            calendar.set(Calendar.HOUR_OF_DAY, 23)
            calendar.set(Calendar.MINUTE, 59)
            calendar.set(Calendar.SECOND, 59)
            calendar.set(Calendar.MILLISECOND, 999)
            val endOfDay = calendar.timeInMillis

            daysInThisWeek.add(Pair(startOfDay, endOfDay))

            // Move to the next day
            calendar.add(Calendar.DAY_OF_YEAR, 1)
        }

        return daysInThisWeek
    }

    /**
     * Returns a list of start and end timestamps for each day in last week (Monday to Sunday).
     */
    fun getLastWeekDailyRanges(): List<Pair<Long, Long>> {
        val calendar = Calendar.getInstance()
        calendar.firstDayOfWeek = Calendar.MONDAY

        // Go to the last Monday
        calendar.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY)
        calendar.add(Calendar.WEEK_OF_YEAR, -1)

        val daysInLastWeek = mutableListOf<Pair<Long, Long>>()
        for (i in 0 until 7) {
            // Set the start of the day to 12:00 AM
            calendar.set(Calendar.HOUR_OF_DAY, 0)
            calendar.set(Calendar.MINUTE, 0)
            calendar.set(Calendar.SECOND, 0)
            calendar.set(Calendar.MILLISECOND, 0)
            val startOfDay = calendar.timeInMillis

            // Set the end of the day to 11:59 PM
            calendar.set(Calendar.HOUR_OF_DAY, 23)
            calendar.set(Calendar.MINUTE, 59)
            calendar.set(Calendar.SECOND, 59)
            calendar.set(Calendar.MILLISECOND, 999)
            val endOfDay = calendar.timeInMillis

            daysInLastWeek.add(Pair(startOfDay, endOfDay))

            // Move to the next day
            calendar.add(Calendar.DAY_OF_YEAR, 1)
        }

        return daysInLastWeek
    }


    /**
     * Returns a list of start and end timestamps for each day in this month.
     */
    fun getThisMonthDailyRanges(): List<Pair<Long, Long>> {
        val calendar = Calendar.getInstance()

        // Go to the first day of this month
        calendar.set(Calendar.DAY_OF_MONTH, 1)

        val today = Calendar.getInstance()
        val daysInThisMonth = mutableListOf<Pair<Long, Long>>()

        while (calendar.before(today) || calendar.get(Calendar.DAY_OF_YEAR) == today.get(Calendar.DAY_OF_YEAR)) {
            val startOfDay = calendar.timeInMillis
            calendar.add(Calendar.DAY_OF_YEAR, 1)
            val endOfDay = calendar.timeInMillis

            daysInThisMonth.add(Pair(startOfDay, endOfDay))
        }
        Log.d("DaysInThisMonth", daysInThisMonth.toString())
        return daysInThisMonth
    }
}

