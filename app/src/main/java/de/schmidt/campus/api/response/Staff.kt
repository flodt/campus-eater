package de.schmidt.campus.api.response


import com.google.gson.annotations.SerializedName

data class Staff(
    @SerializedName("base_price")
    override val basePrice: Double = 0.0, // 1.0
    @SerializedName("price_per_unit")
    override val pricePerUnit: Double = 0.0, // 0.9
    @SerializedName("unit")
    override val unit: String = "" // 100g
) : PriceBundle()