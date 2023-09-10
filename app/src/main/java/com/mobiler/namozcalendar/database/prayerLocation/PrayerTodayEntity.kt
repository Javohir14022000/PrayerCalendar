package com.mobiler.namozcalendar.database.prayerLocation

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "prayer_today_entity")
data class PrayerTodayEntity(
    @PrimaryKey
    var id: Int,
    var date1: String,
    var date2: String,
    var timeZone: String,
    var time: String,
    var weekday: String,
    var bomdod: String,
    var quyosh: String,
    var peshin: String,
    var asr: String,
    var shom: String,
    var xufton: String

)