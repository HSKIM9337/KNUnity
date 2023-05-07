package com.example.knunity.Fragments

import android.app.Activity
import android.content.ContentValues.TAG
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.text.Spannable
import android.text.SpannableString
import android.text.style.ForegroundColorSpan
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.example.knunity.R
import com.example.knunity.calendar.Memo
import com.example.knunity.calendar.MemoActivity
import com.example.knunity.calendar.SaturdayDecorator
import com.example.knunity.calendar.SundayDecorator
import com.example.knunity.databinding.FragmentHomeBinding
import com.example.knunity.utils.FBAuth
import com.example.knunity.utils.FBRef
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.prolificinteractive.materialcalendarview.CalendarDay
import com.prolificinteractive.materialcalendarview.DayViewDecorator
import com.prolificinteractive.materialcalendarview.DayViewFacade
import com.prolificinteractive.materialcalendarview.format.TitleFormatter
import com.prolificinteractive.materialcalendarview.format.WeekDayFormatter
import org.threeten.bp.DayOfWeek
import org.threeten.bp.LocalDate
import java.text.SimpleDateFormat
import java.util.*


class HomeFragment : Fragment() {

    private lateinit var binding: FragmentHomeBinding
    private lateinit var eventDates: HashSet<CalendarDay>
   // private lateinit var eventDecorator: EventDecorator
    private var eventDecorator: EventDecorator? = null
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?

    ): View {
        binding = FragmentHomeBinding.inflate(inflater, container, false)
        binding.calendarView.setOnDateChangedListener { widget, date, selected ->
                val intent = Intent(requireContext(), MemoActivity::class.java)
                intent.putExtra("selected_date", date)
                startActivity(intent)
        }
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val today = CalendarDay.today()

        val disabledDates = hashSetOf<CalendarDay>()
        disabledDates.add(CalendarDay.from(2022, 7, 12))
        eventDates = hashSetOf()
        val memoRef = FBRef.memoRef.child(FBAuth.getUid())
        memoRef.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                for (dateSnapshot in snapshot.children) {
                    val memo = dateSnapshot.getValue(Memo::class.java)
                    if (memo != null) {
                        val date = LocalDate.parse(memo.date, org.threeten.bp.format.DateTimeFormatter.ISO_LOCAL_DATE)
                        eventDates.add(CalendarDay.from(date.year, date.monthValue, date.dayOfMonth))
                    }
                }
                // 달력에 이벤트 표시하기
                val eventDecorator = EventDecorator(
                    ContextCompat.getColor(requireContext(), R.color.purple_200),
                    eventDates,
                    requireContext()
                )
                eventDecorator?.let {
                    binding.calendarView.addDecorator(it)
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Log.e(TAG, "onCancelled", error.toException())
            }
        })

        binding.calendarView.apply {
            // 휴무일 지정을 위한 Decorator 설정
            addDecorator(DayDisableDecorator(disabledDates, today))
            // 요일을 지정하기 위해 WeekDayFormatter 설정
            setWeekDayFormatter(MyWeekDayFormatter())
            // 달력 상단에 `월 년` 포맷을 수정하기 위해 TitleFormatter 설정
            setTitleFormatter(MyTitleFormatter())
            addDecorator(eventDecorator)
            addDecorator(SundayDecorator())
            addDecorator(SaturdayDecorator())
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
           context?.let {
               spannableString.setSpan(
                   ForegroundColorSpan(ContextCompat.getColor(requireContext(), textColor)),
                   0,
                   spannableString.length,
                   Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
               )
           }
            return spannableString
        }

        private fun getWeekdayName(dayOfWeek: DayOfWeek): String {
            val weekdays = arrayOf("일", "월", "화", "수", "목", "금", "토")
            return weekdays[dayOfWeek.value - 1]
        }
    }

    inner class EventDecorator(
        private val color: Int,
        private val dates: Collection<CalendarDay>,
        private val context: Context
    ) : DayViewDecorator {

        private val drawable: Drawable = context.resources.getDrawable(R.drawable.more)

        override fun shouldDecorate(day: CalendarDay): Boolean {
            return dates.contains(day)
        }

        override fun decorate(view: DayViewFacade) {
            view.setSelectionDrawable(drawable)
            view.addSpan(ForegroundColorSpan(color))
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
            return dates.contains(day)
        }

        override fun decorate(view: DayViewFacade?) {
            view?.let {
                it.setDaysDisabled(true)
                it.addSpan(ForegroundColorSpan(Color.GRAY))
            }
        }
    }
}


