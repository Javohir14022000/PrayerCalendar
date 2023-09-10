package com.mobiler.namozcalendar.repository

import com.mobiler.namozcalendar.database.prayer.PrayerDao
import com.mobiler.namozcalendar.database.prayer.PrayerEntity
import com.mobiler.namozcalendar.networking.ApiService

class PrayerRepository(private val apiService: ApiService, private val prayerDao: PrayerDao) {

    //    Remote
    suspend fun getPrayerDataRemote(
        year: Int,
        month: Int,
        lat: Double,
        long: Double,
        method: Int,
        school: Int
    ) = apiService.getData(year, month, lat, long, method, school)

//    Local

    suspend fun getPrayerDataLocal() = prayerDao.getPrayerData()

    suspend fun addPrayerDataLocal(prayerEntity: PrayerEntity) =
        prayerDao.addPrayerData(prayerEntity)

}