package com.mobiler.namozcalendar.repository

import com.mobiler.namozcalendar.database.prayerLocation.PrayerTodayDao
import com.mobiler.namozcalendar.database.prayerLocation.PrayerTodayEntity
import com.mobiler.namozcalendar.networking.ApiService

class PrayerTodayRepository(
    private var apiService: ApiService,
    private var prayerTodayDao: PrayerTodayDao
) {

    //    Remote
    suspend fun getPrayerTodayDataRemote(year: String, lat: Double, long: Double, method: Int) =
        apiService.getTodayData(year, lat, long, method)

//    Local

    suspend fun addPrayerTodayDataLocal(prayerTodayEntity: PrayerTodayEntity) =
        prayerTodayDao.addPrayerTodayData(prayerTodayEntity)

    suspend fun getPrayerTodayDataLocal() = prayerTodayDao.getPrayerTodayData()
}