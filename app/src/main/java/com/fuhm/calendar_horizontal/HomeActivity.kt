package com.fuhm.calendar_horizontal

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.fuhm.calendar_horizontal.adapter.CalendarHorizontalAdapter
import com.fuhm.calendar_horizontal.data.model.CalendarDay
import com.fuhm.calendar_horizontal.databinding.ActivityHomeBinding
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

class HomeActivity : AppCompatActivity() {
    private var _binding: ActivityHomeBinding? = null
    private val binding get() = _binding!!
    private var calendarHorizontalAdapter: CalendarHorizontalAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initViews()
        handleEvents()
    }

    private fun initViews() {
        initTextViewDate(Calendar.getInstance().time)
        initRecyclerViewCalendar()
    }

    private fun initTextViewDate(date: Date) {
        val dateFormat = SimpleDateFormat("MMM dd, yyyy", Locale.getDefault()).format(date)
        binding.tvDate.text = dateFormat
    }

    private fun initRecyclerViewCalendar() {
        calendarHorizontalAdapter = CalendarHorizontalAdapter().apply {
            submitList(getCalendarDays())
        }
        binding.rcvCalendar.apply {
            layoutManager = LinearLayoutManager(this@HomeActivity, LinearLayoutManager.HORIZONTAL, false)
            itemAnimator = null
            adapter = calendarHorizontalAdapter
        }
    }

    private fun getCalendarDays() : List<CalendarDay> {
        val calendarDays = mutableListOf<CalendarDay>()

        val startDay = Calendar.getInstance().apply {
            set(Calendar.DAY_OF_MONTH, Calendar.getInstance().get(Calendar.DAY_OF_MONTH))
            set(Calendar.HOUR_OF_DAY, 0)
            set(Calendar.MINUTE, 0)
            set(Calendar.SECOND, 0)
            set(Calendar.MILLISECOND, 0)
        }.apply {
            set(Calendar.MONTH, Calendar.getInstance().get(Calendar.MONTH) - 2)
        }

        val endDay = Calendar.getInstance().apply {
            set(Calendar.DAY_OF_MONTH, getActualMaximum(Calendar.DAY_OF_MONTH))
            set(Calendar.HOUR_OF_DAY, 0)
            set(Calendar.MINUTE, 0)
            set(Calendar.SECOND, 0)
            set(Calendar.MILLISECOND, 0)
        }

        val currentDay = Calendar.getInstance().apply {
            time = startDay.time
        }

        while(!currentDay.after(endDay)) {
            val dayOfWeek = currentDay.getDisplayName(Calendar.DAY_OF_WEEK, Calendar.SHORT, Locale.getDefault())
            val day = currentDay.get(Calendar.DAY_OF_MONTH)
            val month = currentDay.get(Calendar.MONTH) + 1
            val year = currentDay.get(Calendar.YEAR)

            calendarDays.add(CalendarDay(dayOfWeek!!, day, month, year))
            currentDay.add(Calendar.DAY_OF_MONTH, 1)
        }

        return calendarDays
    }

    private fun handleEvents() {
        calendarHorizontalAdapter?.setOnClickListener { calendarDay ->
            val date = Calendar.getInstance().apply {
                set(Calendar.DAY_OF_MONTH, calendarDay.day)
                set(Calendar.MONTH, calendarDay.month - 1)
                set(Calendar.YEAR, calendarDay.year)
                set(Calendar.HOUR_OF_DAY, 0)
                set(Calendar.MINUTE, 0)
                set(Calendar.SECOND, 0)
                set(Calendar.MILLISECOND, 0)
            }.time

            initTextViewDate(date)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
        calendarHorizontalAdapter = null
    }
}