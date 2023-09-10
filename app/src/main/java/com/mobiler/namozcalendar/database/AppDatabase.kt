package com.mobiler.namozcalendar.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.mobiler.namozcalendar.database.prayer.PrayerDao
import com.mobiler.namozcalendar.database.prayer.PrayerEntity
import com.mobiler.namozcalendar.database.prayerLocation.PrayerTodayDao
import com.mobiler.namozcalendar.database.prayerLocation.PrayerTodayEntity


@Database(entities = [PrayerEntity::class, PrayerTodayEntity::class], version = 1)

abstract class AppDatabase : RoomDatabase() {

    abstract fun prayerDao(): PrayerDao
    abstract fun prayerTodayDao(): PrayerTodayDao

    companion object {
        private var instance: AppDatabase? = null
        @Synchronized
        fun getInstance(context: Context): AppDatabase {
            if (instance == null) {
                instance = Room.databaseBuilder(context, AppDatabase::class.java, "my.db")
                    .allowMainThreadQueries()
                    .build()
            }
            return instance as AppDatabase

        }
    }

}
