package com.adormantsakthi.holup.functions.statistics

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
            val startOfDay = calendar.timeInMillis
            calendar.add(Calendar.DAY_OF_YEAR, 1)
            val endOfDay = calendar.timeInMillis

            daysInThisWeek.add(Pair(startOfDay, endOfDay))
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
            val startOfDay = calendar.timeInMillis
            calendar.add(Calendar.DAY_OF_YEAR, 1)
            val endOfDay = calendar.timeInMillis

            daysInLastWeek.add(Pair(startOfDay, endOfDay))
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

        return daysInThisMonth
    }
}

