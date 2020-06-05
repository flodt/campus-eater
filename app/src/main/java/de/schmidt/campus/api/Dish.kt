package de.schmidt.campus.api


import com.google.gson.annotations.SerializedName

data class Dish(
    @SerializedName("ingredients")
    val ingredients: List<String> = listOf(),
    @SerializedName("name")
    val name: String = "", // Gnocchi mit Schafskäse und frischem Basilikum
    @SerializedName("price")
    val price: String = "" // N/A
)