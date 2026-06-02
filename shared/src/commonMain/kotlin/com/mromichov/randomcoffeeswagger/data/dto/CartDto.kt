package com.mromichov.randomcoffeeswagger.data.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class CartDto(
    @SerialName("items") val items: List<CartItemResponseDto>,
    @SerialName("total") val totalPrice: Double
)

@Serializable
data class CartItemResponseDto(
    @SerialName("product") val product: ProductDto,
    @SerialName("quantity") val quantity: Int,
    @SerialName("total_price") val totalPrice: Double
)

@Serializable
data class AddToCartRequestDto(
    @SerialName("product_id") val productId: Int,
    @SerialName("quantity") val quantity: Int
)

@Serializable
data class UpdateCartItemRequestDto(
    @SerialName("quantity") val quantity: Int
)