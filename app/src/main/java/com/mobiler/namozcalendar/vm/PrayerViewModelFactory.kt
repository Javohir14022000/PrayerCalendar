package com.mobiler.namozcalendar.vm

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.mobiler.namozcalendar.database.prayer.PrayerDao
import com.mobiler.namozcalendar.networking.ApiService
import com.mobiler.namozcalendar.utils.NetworkHelper
import java.lang.RuntimeException

class PrayerViewModelFactory(
    private val apiService: ApiService,
    private val prayerDao: PrayerDao,
    private val networkHelper: NetworkHelper
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(PrayerViewModel::class.java)) {
            return PrayerViewModel(apiService, prayerDao, networkHelper) as T
        }
        throw RuntimeException("Error")

    }

}