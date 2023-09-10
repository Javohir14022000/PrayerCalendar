package com.mobiler.namozcalendar.utils

import com.mobiler.namozcalendar.database.prayer.PrayerEntity

sealed class PrayerResource {

    object Loading : PrayerResource()
    data class Success(val list: List<PrayerEntity>) : PrayerResource()
    data class Error(val message: String) : PrayerResource()

}
