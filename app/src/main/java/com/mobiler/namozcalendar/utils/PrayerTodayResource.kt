package com.mobiler.namozcalendar.utils

import com.mobiler.namozcalendar.database.prayer.PrayerEntity
import com.mobiler.namozcalendar.database.prayerLocation.PrayerTodayEntity

sealed class PrayerTodayResource{

    object Loading : PrayerTodayResource()
    data class Success(val list: List<PrayerTodayEntity>) : PrayerTodayResource()
    data class Error(val message: String) : PrayerTodayResource()
}
