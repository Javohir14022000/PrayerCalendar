package com.mobiler.namozcalendar.ui

import android.graphics.Color
import android.graphics.drawable.GradientDrawable
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import com.mobiler.namozcalendar.R
import com.mobiler.namozcalendar.database.AppDatabase
import com.mobiler.namozcalendar.databinding.FragmentCalendarBinding
import com.mobiler.namozcalendar.databinding.ItemTabBinding
import com.mobiler.namozcalendar.models.MonthCategory
import com.mobiler.namozcalendar.networking.ApiClient
import com.mobiler.namozcalendar.ui.adapter.CalendarListAdapter
import com.mobiler.namozcalendar.ui.adapter.CalendarPageAdapter
import com.mobiler.namozcalendar.utils.NetworkHelper
import com.mobiler.namozcalendar.utils.PrayerResource
import com.mobiler.namozcalendar.utils.SharedPreference
import com.mobiler.namozcalendar.vm.PrayerViewModel
import com.mobiler.namozcalendar.vm.PrayerViewModelFactory
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import java.lang.Appendable
import kotlin.coroutines.CoroutineContext


class CalendarFragment : Fragment(R.layout.fragment_calendar), CoroutineScope {

    private var _binding: FragmentCalendarBinding? = null
    private val binding get() = _binding
    lateinit var calendarPageAdapter: CalendarPageAdapter
    lateinit var monthList: ArrayList<MonthCategory>


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCalendarBinding.inflate(inflater, container, false)
        val root: View = binding!!.root
        loadCategory()
        val fm = childFragmentManager
        val lifecycle = viewLifecycleOwner.lifecycle
        calendarPageAdapter = CalendarPageAdapter(monthList, fm, lifecycle)
        binding?.apply {
            viewPager.adapter = calendarPageAdapter

            TabLayoutMediator(tabLayout, viewPager) { tab, position ->
                tab.text = monthList[position].name
            }.attach()

            setTabs()

            tabLayout.setOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
                override fun onTabSelected(tab: TabLayout.Tab?) {
                    val customView = tab?.customView
                    val bind = customView?.let { ItemTabBinding.bind(it) }
                    bind?.textCategory?.setTextColor(Color.WHITE)
                    val gradientDrawable = bind?.cons?.background?.mutate() as GradientDrawable
                    gradientDrawable.setColor(Color.parseColor("#00B238"))
                }

                override fun onTabUnselected(tab: TabLayout.Tab?) {
                    val customView = tab!!.customView
                    val bind = ItemTabBinding.bind(customView!!)
//                    val gradientDrawable = bind.cons.background.mutate() as GradientDrawable
//                    if (sharedPreferences.getBoolean(KEY_ISNIGHTMODE, false)) {
//                        gradientDrawable.setColor(Color.parseColor("#25303F"))
//                    } else {
//                        gradientDrawable.setColor(Color.WHITE)
//                    }
                    bind.textCategory.setTextColor(Color.parseColor("#7C7979"))
                }

                override fun onTabReselected(tab: TabLayout.Tab?) {

                }

            })

        }

        return root
    }

    private fun setTabs() {
        binding?.apply {
            val tabCount = tabLayout.tabCount
            for (i in 0 until tabCount) {
                val itemTabBinding =
                    ItemTabBinding.inflate(LayoutInflater.from(context), null, false)
                val tabAt = tabLayout.getTabAt(i)
                tabAt?.customView = itemTabBinding.root

                itemTabBinding.textCategory.text = monthList[i].name


                if (i == 0) {
                    val gradientDrawable =
                        itemTabBinding.cons.background.mutate() as GradientDrawable
                    gradientDrawable.setColor(Color.parseColor("#00B238"))
                    itemTabBinding.textCategory.setTextColor(Color.WHITE)
                } else {
//            val gradientDrawable = itemTabBinding.cons.background.mutate() as GradientDrawable
//            if (sharedPreferences.getBoolean(KEY_ISNIGHTMODE, false)) {
//                gradientDrawable.setColor(Color.parseColor("#25303F"))
//            } else {
//                gradientDrawable.setColor(Color.WHITE)
//            }
                    itemTabBinding.textCategory.setTextColor(Color.parseColor("#7C7979"))
                }

            }
        }


    }


    private fun loadCategory() {
        monthList = ArrayList()
        monthList.add(MonthCategory("Yanvar", 1))
        monthList.add(MonthCategory("Fevral", 2))
        monthList.add(MonthCategory("Mart", 3))
        monthList.add(MonthCategory("Aprel", 4))
        monthList.add(MonthCategory("May", 5))
        monthList.add(MonthCategory("Iyun", 6))
        monthList.add(MonthCategory("Iyul", 7))
        monthList.add(MonthCategory("Avgust", 8))
        monthList.add(MonthCategory("Sentyabr", 9))
        monthList.add(MonthCategory("Oktyabr", 10))
        monthList.add(MonthCategory("Noyabr", 11))
        monthList.add(MonthCategory("Dekabr", 12))

    }

    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Main + Job()

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}