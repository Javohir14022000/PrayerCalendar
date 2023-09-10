package com.mobiler.namozcalendar.networking

import com.mobiler.namozcalendar.models.CalendarData
import com.mobiler.namozcalendar.models.prayerToday.PrayerTodayData
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiService {

    @GET("calendar/{year}/{month}")
    suspend fun getData(
        @Path("year") year: Int,
        @Path("month") month: Int,
        @Query("latitude") latitude: Double,
        @Query("longitude") longitude: Double,
        @Query("method") method: Int,
        @Query("school") school: Int,
    ): Response<CalendarData>

    @GET("timings/{date}")
    suspend fun getTodayData(
        @Path("date") date: String,
        @Query("latitude") latitude: Double,
        @Query("longitude") longitude: Double,
        @Query("method") method: Int
    ): Response<PrayerTodayData>
}