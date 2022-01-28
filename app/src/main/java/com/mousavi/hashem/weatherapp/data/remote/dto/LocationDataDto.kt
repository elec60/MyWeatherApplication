package com.mousavi.hashem.weatherapp.data.remote.dto


import com.google.gson.annotations.SerializedName

data class LocationDataDto(
    @SerializedName("latt_long")
    val lattLong: String,
    @SerializedName("location_type")
    val locationType: String,
    @SerializedName("title")
    val title: String,
    @SerializedName("woeid")
    val woeid: Int,
)