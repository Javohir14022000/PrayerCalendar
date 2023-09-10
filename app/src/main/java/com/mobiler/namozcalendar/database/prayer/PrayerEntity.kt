package com.mobiler.namozcalendar.database.prayer

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "prayer_entity")
data class PrayerEntity(
    @PrimaryKey(autoGenerate = true)
    var id: Int,
    var data1: String,
    var data2: String,
    var month: String,
    var bomdod: String,
    var peshin: String,
    var asr: String,
    var shom: String,
    var xufton: String,
    var timeZone: String,
    var time: String,
    var weekday: String
)
