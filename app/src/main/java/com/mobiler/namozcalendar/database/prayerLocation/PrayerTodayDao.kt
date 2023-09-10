package com.mobiler.namozcalendar.database.prayerLocation

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface PrayerTodayDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addPrayerTodayData(prayerTodayEntity: PrayerTodayEntity)

    @Query("select * from prayer_today_entity")
    suspend fun getPrayerTodayData(): List<PrayerTodayEntity>
}