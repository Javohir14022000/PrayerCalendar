package com.mobiler.namozcalendar.ui.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.mobiler.namozcalendar.ui.CalendarFragment
import com.mobiler.namozcalendar.ui.SettingsFragment
import com.mobiler.namozcalendar.ui.TimesFragment

class ViewPagerAdapter(fragmentManager: FragmentManager): FragmentStatePagerAdapter(fragmentManager, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {
    override fun getCount(): Int {
        return 3
    }

    override fun getItem(position: Int): Fragment {
        return when(position){
            0->{
                TimesFragment()
            }
            1->{
                CalendarFragment()
            }
            2->{
                SettingsFragment()
            }
            else -> {
                TimesFragment()
            }
        }
    }


}