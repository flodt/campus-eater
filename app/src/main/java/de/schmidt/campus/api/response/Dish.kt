package de.schmidt.campus.api.response


import com.google.gson.annotations.SerializedName

data class Dish(
    @SerializedName("dish_type")
    val dishType: String = "", // Pasta
    @SerializedName("ingredients")
    val ingredients: List<String> = listOf(),
    @SerializedName("name")
    val name: String = "", // Älpler Makkaroni mit Bergkäse
    @SerializedName("prices")
    val prices: Prices = Prices()
)