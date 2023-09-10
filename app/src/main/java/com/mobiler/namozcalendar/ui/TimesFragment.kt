package com.mobiler.namozcalendar.ui

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.mobiler.namozcalendar.R
import com.mobiler.namozcalendar.database.AppDatabase
import com.mobiler.namozcalendar.database.prayerLocation.PrayerTodayEntity
import com.mobiler.namozcalendar.databinding.FragmentTimesBinding
import com.mobiler.namozcalendar.networking.ApiClient
import com.mobiler.namozcalendar.utils.NetworkHelper
import com.mobiler.namozcalendar.utils.PrayerTodayResource
import com.mobiler.namozcalendar.utils.SharedPreference
import com.mobiler.namozcalendar.vm.PrayerTodayViewModel
import com.mobiler.namozcalendar.vm.PrayerTodayViewModelFactory
import com.mobiler.namozcalendar.vm.PrayerViewModelFactory
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.Runnable

import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.Timer
import kotlin.concurrent.timer
import kotlin.coroutines.CoroutineContext

class TimesFragment : Fragment(R.layout.fragment_times), CoroutineScope {

    private var _binding: FragmentTimesBinding? = null
    private val binding get() = _binding
    lateinit var appDatabase: AppDatabase
    lateinit var networkHelper: NetworkHelper
    lateinit var prayerTodayViewModel: PrayerTodayViewModel
    lateinit var list: List<PrayerTodayEntity>
    lateinit var sharedPreference: SharedPreference
    private var handler: Handler = Handler(Looper.getMainLooper())
    private var isRunning = false
    private var elapsedTimeInSeconds = 0


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        appDatabase = AppDatabase.getInstance(requireContext())
        networkHelper = NetworkHelper(requireContext())
        sharedPreference = SharedPreference(requireContext())
        prayerTodayViewModel = ViewModelProvider(
            this,
            PrayerTodayViewModelFactory(
                ApiClient.apiService,
                appDatabase.prayerTodayDao(),
                networkHelper
            )
        )[PrayerTodayViewModel::class.java]

    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentTimesBinding.inflate(inflater, container, false)
        val root: View = binding!!.root
        val location = sharedPreference.getLocation()

//        val initialTime = SimpleDateFormat("HH:mm:ss", Locale.getDefault()).format(Date())
//
//        binding?.tvTime01?.text = initialTime


        val currentTimeMillis = System.currentTimeMillis()
        val currentData = Date(currentTimeMillis)
        val dateFormat = SimpleDateFormat("dd-MM-yyyy")
        val timeFormat = SimpleDateFormat("HH:mm")
        val timeFormatSekund = SimpleDateFormat("HH:mm:ss")
        val formatDateTo = dateFormat.format(currentData)
        val formatTimeTo = timeFormat.format(currentData)
        val formatTimeToSekund = timeFormatSekund.format(currentData)
        val time =
            (formatTimeTo.substring(0, 2).toInt() * 60).plus(formatTimeTo.substring(3, 5).toInt())

//


        Log.d("TAG  MINUTE", " $formatTimeTo----$time ")
        list = ArrayList()
        lifecycleScope.launch {
            prayerTodayViewModel.getPrayerTodayData(
                formatDateTo,
                location!!.first,
                location.second,
                2
            ).collect {
                when (it) {
                    is PrayerTodayResource.Loading -> {
                        Log.d("TAG", "Loading: ")
                    }

                    is PrayerTodayResource.Success -> {
                        binding?.apply {
                            val get = it.list.get(0)
                            tvLocation.text = get.timeZone
                            tvDay.text = get.date1
                            tvDay1.text = get.date2
                            tvWeek.text = get.weekday
                            tvTime1.text = get.bomdod
                            tvTime2.text = get.quyosh
                            tvTime3.text = get.peshin
                            tvTime4.text = get.asr
                            tvTime5.text = get.shom
                            tvTime6.text = get.xufton
                            val minute_bomdod = (get.bomdod.substring(0, 2).toInt() * 60).plus(
                                get.bomdod.substring(
                                    3,
                                    5
                                ).toInt()
                            )
                            val minute_quyosh = (get.quyosh.substring(0, 2).toInt() * 60).plus(
                                get.quyosh.substring(
                                    3,
                                    5
                                ).toInt()
                            )
                            val minute_peshin = (get.peshin.substring(0, 2).toInt() * 60).plus(
                                get.peshin.substring(
                                    3,
                                    5
                                ).toInt()
                            )
                            val minute_asr = (get.asr.substring(0, 2).toInt() * 60).plus(
                                get.asr.substring(3, 5).toInt()
                            )
                            val minute_shom = (get.shom.substring(0, 2).toInt() * 60).plus(
                                get.shom.substring(
                                    3,
                                    5
                                ).toInt()
                            )
                            val minute_xufton = (get.xufton.substring(0, 2).toInt() * 60).plus(
                                get.xufton.substring(
                                    3,
                                    5
                                ).toInt()
                            )

                            if (time > minute_bomdod && minute_quyosh > time) {

                                elapsedTimeInSeconds =
                                    (minute_quyosh*60).minus((((formatTimeToSekund.substring(0, 2).toInt() * 60).plus(
                                        formatTimeToSekund.substring(
                                            3,
                                            5
                                        ).toInt()
                                    )) * 60).plus(formatTimeToSekund.substring(6, 8).toInt()))
                                tvTime.setText("Quyoshga")

                            } else if (time >= minute_quyosh && minute_peshin > time) {
                                elapsedTimeInSeconds =
                                    (minute_peshin*60).minus((((formatTimeToSekund.substring(0, 2).toInt() * 60).plus(
                                        formatTimeToSekund.substring(
                                            3,
                                            5
                                        ).toInt()
                                    )) * 60).plus(formatTimeToSekund.substring(6, 8).toInt()))

                                tvTime.setText("Peshin")
                            } else if (time >= minute_peshin && minute_asr > time) {
                                elapsedTimeInSeconds =
                                    (minute_asr*60).minus((((formatTimeToSekund.substring(0, 2).toInt() * 60).plus(
                                        formatTimeToSekund.substring(
                                            3,
                                            5
                                        ).toInt()
                                    )) * 60).plus(formatTimeToSekund.substring(6, 8).toInt()))
                                tvTime.setText("Asr")
                            } else if (time >= minute_asr && minute_shom > time) {
                                elapsedTimeInSeconds =
                                    (minute_shom*60).minus((((formatTimeToSekund.substring(0, 2).toInt() * 60).plus(
                                        formatTimeToSekund.substring(
                                            3,
                                            5
                                        ).toInt()
                                    )) * 60).plus(formatTimeToSekund.substring(6, 8).toInt()))
                                tvTime.setText("Shom")
                            } else if (time >= minute_shom && minute_xufton > time) {
                                elapsedTimeInSeconds =
                                    (minute_xufton*60).minus((((formatTimeToSekund.substring(0, 2).toInt() * 60).plus(
                                        formatTimeToSekund.substring(
                                            3,
                                            5
                                        ).toInt()
                                    )) * 60).plus(formatTimeToSekund.substring(6, 8).toInt()))
                                tvTime.setText("Xufton")
                            } else if (time >= minute_xufton || minute_bomdod > time) {
                                elapsedTimeInSeconds =
                                    ((86400).minus((((formatTimeToSekund.substring(0, 2).toInt() * 60).plus(
                                        formatTimeToSekund.substring(
                                            3,
                                            5
                                        ).toInt()
                                    )) * 60).plus(formatTimeToSekund.substring(6, 8).toInt()))).plus(minute_bomdod*60)
                                tvTime.setText("Bomdod")
                            }
                            startTimer()
                        }
                    }

                    is PrayerTodayResource.Error -> {
                        Log.d("TAG", "Error ${it.message} ")
                    }
                }
            }
        }


        return root
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun startTimer() {
        isRunning = true
        handler.post(object : Runnable {
            override fun run() {
                if (isRunning) {
                    elapsedTimeInSeconds--
                    val formattedTime = formatTime(elapsedTimeInSeconds)
                    binding?.tvTime01?.text = formattedTime
                    handler.postDelayed(this, 1000)
                }
            }
        })
    }

    private fun formatTime(seconds: Int): String {
        val hours = seconds / 3600
        val minutes = (seconds % 3600) / 60
        val secs = seconds % 60
        return String.format("%02d:%02d:%02d", hours, minutes, secs)
    }


    override fun onDestroy() {
        super.onDestroy()
        isRunning = false
    }

    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Main + Job()


}