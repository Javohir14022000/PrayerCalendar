package com.mobiler.namozcalendar.ui

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.mobiler.namozcalendar.R
import com.mobiler.namozcalendar.database.AppDatabase
import com.mobiler.namozcalendar.databinding.FragmentCalendarPageBinding
import com.mobiler.namozcalendar.networking.ApiClient
import com.mobiler.namozcalendar.ui.adapter.CalendarListAdapter
import com.mobiler.namozcalendar.utils.NetworkHelper
import com.mobiler.namozcalendar.utils.PrayerResource
import com.mobiler.namozcalendar.utils.SharedPreference
import com.mobiler.namozcalendar.vm.PrayerViewModel
import com.mobiler.namozcalendar.vm.PrayerViewModelFactory
import kotlinx.coroutines.launch


private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

class CalendarPageFragment : Fragment() {
    private var position: Int? = null
    private var name: String? = null
    private var _binding: FragmentCalendarPageBinding? = null
    private val binding get() = _binding
    lateinit var appDatabase: AppDatabase
    lateinit var prayerViewModel: PrayerViewModel
    lateinit var networkHelper: NetworkHelper
    lateinit var sharedPreference: SharedPreference
    lateinit var calendarListAdapter: CalendarListAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        sharedPreference = SharedPreference(requireContext())
        appDatabase = AppDatabase.getInstance(requireContext())
        networkHelper = NetworkHelper(requireContext())
        calendarListAdapter = CalendarListAdapter()
        prayerViewModel = ViewModelProvider(
            this,
            PrayerViewModelFactory(ApiClient.apiService, appDatabase.prayerDao(), networkHelper)
        )[PrayerViewModel::class.java]
        arguments?.let {
            position = it.getInt(ARG_PARAM1)
            name = it.getString(ARG_PARAM2)
        }
    }


    @SuppressLint("NotifyDataSetChanged")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCalendarPageBinding.inflate(inflater, container, false)
        val root: View = binding!!.root

        val location = sharedPreference.getLocation()
        binding?.apply {
            rv.adapter = calendarListAdapter

        lifecycleScope.launch {
            prayerViewModel.getPrayerData(2023, position!!, location!!.first, location.second, 0, 1)
                .collect {
                    when (it) {
                        is PrayerResource.Loading -> {
                            rv.visibility = View.INVISIBLE
                            shimmer.visibility = View.VISIBLE
                        }

                        is PrayerResource.Success -> {
                            Log.d("TAG", "Success: $it ")
                            rv.visibility = View.VISIBLE
                            shimmer.visibility = View.INVISIBLE
                            calendarListAdapter.submitList(it.list)
                            calendarListAdapter.notifyDataSetChanged()
                        }

                        is PrayerResource.Error -> {
                            rv.visibility = View.INVISIBLE
                            shimmer.visibility = View.INVISIBLE
                            Log.d("TAG", "Error: ${it.message}")
                        }

                        else -> {}
                    }
                }
        }
        }
        return root
    }

    companion object {

        @JvmStatic
        fun newInstance(param1: Int, param2: String) =
            CalendarPageFragment().apply {
                arguments = Bundle().apply {
                    putInt(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}