package com.fuhm.calendar_horizontal.data.model
import java.util.Calendar

data class CalendarDay(
    val dayOfWeek: String,
    val day: Int,
    val month: Int,
    val year: Int
) {
    fun isToday(): Boolean {
        val today = Calendar.getInstance()
        return day == today.get(Calendar.DAY_OF_MONTH) &&
                month == (today.get(Calendar.MONTH) + 1) &&
                year == today.get(Calendar.YEAR)
    }
}
