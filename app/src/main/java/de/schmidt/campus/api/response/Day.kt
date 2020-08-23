package de.schmidt.campus.api.response


import com.google.gson.annotations.SerializedName

data class Day(
    @SerializedName("date")
    val date: String = "", // 2020-02-03
    @SerializedName("dishes")
    val dishes: List<Dish> = listOf()
)