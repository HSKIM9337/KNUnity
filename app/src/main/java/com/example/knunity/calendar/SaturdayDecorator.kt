package com.example.knunity.calendar

import android.graphics.Color
import android.text.style.ForegroundColorSpan
import com.prolificinteractive.materialcalendarview.CalendarDay
import com.prolificinteractive.materialcalendarview.DayViewDecorator
import com.prolificinteractive.materialcalendarview.DayViewFacade
import java.util.*

class SaturdayDecorator: DayViewDecorator {
    private val calendar = Calendar.getInstance()

    override fun shouldDecorate(day: CalendarDay): Boolean {
        val calendar = day.toCalendar()
        val weekDay = calendar.get(Calendar.DAY_OF_WEEK)
        return weekDay == Calendar.SATURDAY
    }

    override fun decorate(view: DayViewFacade) {
        view.addSpan(ForegroundColorSpan(Color.BLUE))
    }
    fun CalendarDay.toCalendar(): Calendar {
        val calendar = Calendar.getInstance()
        calendar.set(year, month - 1, day)
        return calendar
    }
}