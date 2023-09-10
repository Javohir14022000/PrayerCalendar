package com.mobiler.namozcalendar.vm

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mobiler.namozcalendar.database.prayer.PrayerDao
import com.mobiler.namozcalendar.database.prayer.PrayerEntity
import com.mobiler.namozcalendar.networking.ApiService
import com.mobiler.namozcalendar.repository.PrayerRepository
import com.mobiler.namozcalendar.utils.NetworkHelper
import com.mobiler.namozcalendar.utils.PrayerResource
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.lang.Exception

class PrayerViewModel(
    apiService: ApiService,
    prayerDao: PrayerDao,
    private val networkHelper: NetworkHelper
) : ViewModel() {

    private val prayerRepository = PrayerRepository(apiService, prayerDao)

    fun getPrayerData(
        year: Int,
        month: Int,
        lat: Double,
        long: Double,
        method: Int,
        school: Int
    ): StateFlow<PrayerResource> {
        val flow = MutableStateFlow<PrayerResource>(PrayerResource.Loading)
        viewModelScope.launch {
            Log.d("TAG", "viewModelScope:  ")

            try {
                Log.d("TAG", "try:  ")

                if (networkHelper.isNetworkConnected()) {
                    Log.d("TAG", "network:  ")
                    Log.d("TAG", "$year, $month, $lat, $long, $method, $school  ")

                    val response =
                        prayerRepository.getPrayerDataRemote(year, month, lat, long, method, school)

                    Log.d("TAG", "response: ${response} ")

                    if (response.isSuccessful) {
                        val list = ArrayList<PrayerEntity>()
                        val body = response.body()
                        if (body != null) {
                            body.data.forEach {
                                list.add(
                                    PrayerEntity(
                                        it.date.timestamp.toInt(),
                                        it.date.readable,
                                        "${it.date.hijri.day} ${it.date.hijri.month.en} ${it.date.hijri.year}",
                                        it.date.gregorian.month.en,
                                        it.timings.Fajr.substring(0, 5),
                                        it.timings.Dhuhr.substring(0, 5),
                                        it.timings.Asr.substring(0, 5),
                                        it.timings.Sunset.substring(0, 5),
                                        it.timings.Isha.substring(0, 5),
                                        it.meta.timezone,
                                        it.date.gregorian.date,
                                        it.date.gregorian.weekday.en
                                    )
                                )
                            }
                        }

                        /*  Log.d("TAG", "getPrayerData: $body ")
                          Log.d("TAG", "getPrayerData: ${body} ")
                          Log.d("TAG", "getPrayerData: ${response} ")
                          Log.d("TAG", "getPrayerData: ${response} ")*/


                        flow.emit(PrayerResource.Success(list))
                    }
                } else {
                    flow.emit(PrayerResource.Success(prayerRepository.getPrayerDataLocal()))
                }
            } catch (e: Exception) {
                flow.emit(PrayerResource.Error(e.message.toString()))
            }
        }
        return flow
    }

}