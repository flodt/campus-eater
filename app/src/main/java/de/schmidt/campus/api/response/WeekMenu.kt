package de.schmidt.campus.api.response


import com.google.gson.annotations.SerializedName

data class WeekMenu(
    @SerializedName("days")
    val days: List<Day> = listOf(),
    @SerializedName("number")
    val number: Int = 0, // 6
    @SerializedName("year")
    val year: Int = 0 // 2020
)