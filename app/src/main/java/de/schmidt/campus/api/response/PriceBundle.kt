package de.schmidt.campus.api.response

abstract class PriceBundle {
    abstract val unit: String
    abstract val pricePerUnit: Double
    abstract val basePrice: Double
}