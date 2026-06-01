package com.mromichov.randomcoffeeswagger.domain.model

data class Product(
    val id: Int,
    val name: String,
    val description: String,
    val price: Double,
    val imageUrl: String?,
    val categoryId: Int,
    val categoryName: String
)