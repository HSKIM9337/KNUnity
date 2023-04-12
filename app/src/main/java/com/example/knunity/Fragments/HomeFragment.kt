package com.example.knunity.Fragments

import android.graphics.Color
import android.os.Bundle
import android.text.Spannable
import android.text.SpannableString
import android.text.style.ForegroundColorSpan
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.example.knunity.R
import com.example.knunity.databinding.FragmentHomeBinding
import com.prolificinteractive.materialcalendarview.CalendarDay
import com.prolificinteractive.materialcalendarview.DayViewDecorator
import com.prolificinteractive.materialcalendarview.DayViewFacade
import com.prolificinteractive.materialcalendarview.format.TitleFormatter
import com.prolificinteractive.materialcalendarview.format.WeekDayFormatter
import org.threeten.bp.DayOfWeek
import java.text.SimpleDateFormat
import java.util.*


class HomeFragment : Fragment() {

    private lateinit var binding: FragmentHomeBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val today = CalendarDay.today()

        val disabledDates = hashSetOf<CalendarDay>()
        disabledDates.add(CalendarDay.from(2022, 7, 12))

        binding.calendarView.apply {
            // 휴무일 지정을 위한 Decorator 설정
            addDecorator(DayDisableDecorator(disabledDates, today))
            // 요일을 지정하기 위해 WeekDayFormatter 설정
            setWeekDayFormatter(MyWeekDayFormatter())
            // 달력 상단에 `월 년` 포맷을 수정하기 위해 TitleFormatter 설정
            setTitleFormatter(MyTitleFormatter())
        }
    }

    inner class MyTitleFormatter : TitleFormatter {
        override fun format(day: CalendarDay?): CharSequence {
            val calendar = Calendar.getInstance()
            day?.let {
                // 이전 달로 설정
                calendar.set(it.year, it.month - 1, 1)
            }
            val yearMonthFormat = SimpleDateFormat("yyyy.MM", Locale.US)
            return yearMonthFormat.format(calendar.time)
        }
    }

    inner class MyWeekDayFormatter : WeekDayFormatter {
        override fun format(dayOfWeek: DayOfWeek?): CharSequence? {
            // 각 요일마다 다른 색으로 표시
            val textColor = when (dayOfWeek) {
                DayOfWeek.SUNDAY -> R.color.main_color
                DayOfWeek.SATURDAY -> R.color.purple_700
                else -> R.color.black
            }
            // 해당 색을 이용해 SpannableString 생성
            val spannableString = SpannableString("${dayOfWeek?.let { getWeekdayName(it) }}")
            spannableString.setSpan(
                ForegroundColorSpan(ContextCompat.getColor(requireContext(), textColor)),
                0,
                spannableString.length,
                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
            )
            return spannableString
        }

        private fun getWeekdayName(dayOfWeek: DayOfWeek): String {
            val weekdays = arrayOf("일", "월", "화", "수", "목", "금", "토")
            return weekdays[dayOfWeek.value - 1]
        }
    }


    inner class DayDisableDecorator : DayViewDecorator {
        private var dates = HashSet<CalendarDay>()
        private var today: CalendarDay

        constructor(dates: HashSet<CalendarDay>, today: CalendarDay) {
            this.dates = dates
            this.today = today
        }

        override fun shouldDecorate(day: CalendarDay): Boolean {
            // 휴무일 || 이전 날짜
            return dates.contains(day) || day.isBefore(today)
        }

        override fun decorate(view: DayViewFacade?) {
            view?.let {
                it.setDaysDisabled(true)
                it.addSpan(ForegroundColorSpan(Color.GRAY))
            }
        }
    }
}


