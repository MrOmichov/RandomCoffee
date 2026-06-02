package com.mromichov.randomcoffeeswagger.data.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class OrderResponseDto(
    @SerialName("id") val id: Int,
    @SerialName("status") val status: String,
    @SerialName("total_price") val totalPrice: Double? = null,
    @SerialName("items") val items: List<CartItemResponseDto>? = null,
    @SerialName("created_at") val createdAt: String? = null
)