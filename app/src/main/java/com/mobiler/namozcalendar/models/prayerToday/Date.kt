package com.mobiler.namozcalendar.models.prayerToday

data class Date(
    val gregorian: Gregorian,
    val hijri: Hijri,
    val readable: String,
    val timestamp: String
)