package com.mobiler.namozcalendar.ui.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.mobiler.namozcalendar.models.MonthCategory
import com.mobiler.namozcalendar.ui.CalendarPageFragment

class CalendarPageAdapter(private val list: List<MonthCategory>, fragmentManager: FragmentManager, lifecycle: Lifecycle): FragmentStateAdapter(fragmentManager, lifecycle) {
    override fun getItemCount(): Int {
        return list.size
    }

    override fun createFragment(position: Int): Fragment {
        return CalendarPageFragment.newInstance(list[position].position!!, list[position].name!!)
    }


}