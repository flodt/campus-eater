package de.schmidt.campus.api.response


import com.google.gson.annotations.SerializedName

data class Students(
    @SerializedName("base_price")
    val basePrice: Double = 0.0, // 1.0
    @SerializedName("price_per_unit")
    val pricePerUnit: Double = 0.0, // 0.75
    @SerializedName("unit")
    val unit: String = "" // 100g
)