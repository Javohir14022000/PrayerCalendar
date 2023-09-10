package com.mobiler.namozcalendar.vm

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mobiler.namozcalendar.database.prayerLocation.PrayerTodayDao
import com.mobiler.namozcalendar.database.prayerLocation.PrayerTodayEntity
import com.mobiler.namozcalendar.networking.ApiService
import com.mobiler.namozcalendar.repository.PrayerTodayRepository
import com.mobiler.namozcalendar.utils.NetworkHelper
import com.mobiler.namozcalendar.utils.PrayerTodayResource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.launch

class PrayerTodayViewModel(
    apiService: ApiService,
    prayerTodayDao: PrayerTodayDao,
    private val networkHelper: NetworkHelper
) : ViewModel() {

    private val prayerTodayRepository = PrayerTodayRepository(apiService, prayerTodayDao)

    fun getPrayerTodayData(
        year: String,
        lat: Double,
        long: Double,
        method: Int
    ): StateFlow<PrayerTodayResource> {
        val flow = MutableStateFlow<PrayerTodayResource>(PrayerTodayResource.Loading)
        viewModelScope.launch(Dispatchers.IO) {
            try {
                if (networkHelper.isNetworkConnected()) {
                    val response =
                        prayerTodayRepository.getPrayerTodayDataRemote(year, lat, long, method)
                    if (response.isSuccessful) {
                        val list = ArrayList<PrayerTodayEntity>()
                        val body = response.body()
                        if (body != null) {
                            list.add(
                                PrayerTodayEntity(
                                    body.code,
                                    body.data.date.readable,
                                    "${body.data.date.hijri.day} ${body.data.date.hijri.month.en} ${body.data.date.hijri.year}",
                                    body.data.meta.timezone,
                                    body.data.date.gregorian.date,
                                    body.data.date.gregorian.weekday.en,
                                    body.data.timings.Fajr,
                                    body.data.timings.Sunrise,
                                    body.data.timings.Dhuhr,
                                    body.data.timings.Asr,
                                    body.data.timings.Sunset,
                                    body.data.timings.Isha
                                )
                            )
                        }
                        flow.emit(PrayerTodayResource.Success(list))
                    }

                } else {
                    flow.emit(PrayerTodayResource.Success(prayerTodayRepository.getPrayerTodayDataLocal()))
                }
            } catch (e: Exception) {
                flow.emit(PrayerTodayResource.Error(e.message.toString()))
            }
        }

        return flow
    }
}