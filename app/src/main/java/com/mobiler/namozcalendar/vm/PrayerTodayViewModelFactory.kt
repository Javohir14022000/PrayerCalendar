package com.mobiler.namozcalendar.vm

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.mobiler.namozcalendar.database.prayerLocation.PrayerTodayDao
import com.mobiler.namozcalendar.networking.ApiService
import com.mobiler.namozcalendar.utils.NetworkHelper

class PrayerTodayViewModelFactory(
    private val apiService: ApiService,
    private val prayerTodayDao: PrayerTodayDao,
    private val networkHelper: NetworkHelper
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(PrayerTodayViewModel::class.java)){
            return PrayerTodayViewModel(apiService, prayerTodayDao, networkHelper) as T
        }
        throw RuntimeException("Error")
    }
}