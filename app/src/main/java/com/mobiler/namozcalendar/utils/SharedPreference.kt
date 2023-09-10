package com.mobiler.namozcalendar.utils

import android.content.Context
import android.content.SharedPreferences

class SharedPreference(context: Context) {

    private val PREFS_NAME = "MyShared"
    private val KEY_LAT = "latitude"
    private val KEY_LONG = "longitude"

    private val sharedPreference: SharedPreferences? = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)

    fun saveLocation(latitude: Double, longitude: Double){
        val editor = sharedPreference?.edit()
        editor?.putString(KEY_LAT,latitude.toString())
        editor?.putString(KEY_LONG,longitude.toString())
        editor?.apply()
    }

    fun getLocation(): Pair<Double, Double>?{
        val latitudeStr = sharedPreference?.getString(KEY_LAT, null)
        val longitudeStr = sharedPreference?.getString(KEY_LONG, null)

        if (latitudeStr != null && longitudeStr != null){
            val latitude = latitudeStr.toDouble()
            val longitude = longitudeStr.toDouble()
            return Pair(latitude, longitude)
        }
        return null
    }

    fun removeLocation() {
        val editor = sharedPreference?.edit()
        editor?.remove(KEY_LAT)
        editor?.remove(KEY_LONG)
        editor?.apply()
    }
}