package com.fuhm.calendar_horizontal.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.fuhm.calendar_horizontal.R
import com.fuhm.calendar_horizontal.data.model.CalendarDay
import com.fuhm.calendar_horizontal.databinding.ItemCalendarHorizontalBinding

@SuppressLint("DefaultLocale")
class CalendarHorizontalAdapter : ListAdapter<CalendarDay, CalendarHorizontalAdapter.CalendarHorizontalViewHolder>(CalendarHorizontalDiffCallback()) {
    private var selectedPosition = RecyclerView.NO_POSITION
    private var onClick: ((CalendarDay) -> Unit)? = null

    inner class CalendarHorizontalViewHolder(
        private val binding: ItemCalendarHorizontalBinding
    ) : RecyclerView.ViewHolder(binding.root) {
        fun onBind(calendarDay: CalendarDay) {
            binding.run {
                tvDay.text = String.format("%02d", calendarDay.day)
                tvDayOfWeek.text = calendarDay.dayOfWeek
                vIndicator.setBackgroundResource(getBackgroundIndicator(calendarDay))
                main.isSelected = selectedPosition == adapterPosition
                root.setOnClickListener {
                    if (selectedPosition != RecyclerView.NO_POSITION) {
                        val previousPosition = selectedPosition
                        selectedPosition = adapterPosition
                        notifyItemChanged(previousPosition)
                        notifyItemChanged(selectedPosition)

                        onClick?.let {
                            it(calendarDay)
                        }
                    }
                }
            }
        }

        private fun getBackgroundIndicator(calendarDay: CalendarDay): Int {
            return when {
                calendarDay.isToday() -> R.drawable.bg_cal_hor_indicator_selector
                else -> R.drawable.bg_cal_hor_indicator_transparent
            }
        }
    }

    class CalendarHorizontalDiffCallback : DiffUtil.ItemCallback<CalendarDay>() {
        override fun areItemsTheSame(oldItem: CalendarDay, newItem: CalendarDay): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(oldItem: CalendarDay, newItem: CalendarDay): Boolean {
            return oldItem == newItem
        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): CalendarHorizontalViewHolder {
        val binding = ItemCalendarHorizontalBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return CalendarHorizontalViewHolder(binding)
    }

    override fun onBindViewHolder(holder: CalendarHorizontalViewHolder, position: Int) {
        holder.onBind(getItem(position))
    }

    override fun submitList(list: List<CalendarDay>?) {
        super.submitList(list)
        selectedPosition = list?.indexOfFirst { it.isToday() } ?: RecyclerView.NO_POSITION
    }

    fun setOnClickListener(onClick: (CalendarDay) -> Unit) {
        this.onClick = onClick
    }
}
