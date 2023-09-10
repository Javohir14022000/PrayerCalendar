package com.mobiler.namozcalendar.database.prayer

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface PrayerDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
     fun addPrayerData(prayerEntity: PrayerEntity)

    @Query("select * from prayer_entity")
     fun getPrayerData(): List<PrayerEntity>

}