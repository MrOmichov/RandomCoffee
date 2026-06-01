package com.mromichov.randomcoffeeswagger.domain.model

data class Cart(
    val items: List<CartItem>,
    val totalPrice: Double
)

data class CartItem(
    val product: Product,
    val quantity: Int,
    val totalPrice: Double
)