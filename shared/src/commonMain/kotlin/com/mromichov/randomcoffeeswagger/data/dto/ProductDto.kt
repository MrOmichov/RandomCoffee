package com.mromichov.randomcoffeeswagger.data.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ProductDto(
    @SerialName("id")          val id: Int,
    @SerialName("name")        val name: String,
    @SerialName("description") val description: String? = null,
    @SerialName("price")       val price: Double,
    @SerialName("image_url")   val imageUrl: String? = null,
    @SerialName("category_id") val categoryId: Int
)