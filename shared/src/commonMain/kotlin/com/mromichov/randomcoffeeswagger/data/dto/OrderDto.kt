package com.mromichov.randomcoffeeswagger.data.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class OrderResponseDto(
    @SerialName("success") val success: Boolean,
    @SerialName("order") val order: OrderDto
)

@Serializable
data class OrderDto(
    @SerialName("id") val id: Int,
    @SerialName("items") val items: List<CartItemResponseDto>,
    @SerialName("status") val status: String,
    @SerialName("total") val totalPrice: Double,
    )