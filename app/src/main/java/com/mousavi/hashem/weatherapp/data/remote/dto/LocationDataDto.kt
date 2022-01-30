package com.mousavi.hashem.weatherapp.data.remote.dto


import com.google.gson.annotations.SerializedName
import com.mousavi.hashem.weatherapp.domain.entity.LocationData

data class LocationDataDto(
    @SerializedName("latt_long")
    val lattLong: String,
    @SerializedName("location_type")
    val locationType: String,
    @SerializedName("title")
    val title: String,
    @SerializedName("woeid")
    val woeid: Int,
) {
    fun toLocationData(): LocationData {
        return LocationData(
            whereOnEarthID = woeid
        )
    }
}