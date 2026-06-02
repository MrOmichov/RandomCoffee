package com.mromichov.randomcoffeeswagger.data.dto

import com.mromichov.randomcoffeeswagger.domain.model.Cart
import com.mromichov.randomcoffeeswagger.domain.model.CartItem
import com.mromichov.randomcoffeeswagger.domain.model.Category
import com.mromichov.randomcoffeeswagger.domain.model.Product

fun CategoryDto.toDomain() = Category(id = id, name = name)

fun ProductDto.toDomain(categoriesMap: Map<Int, String> = emptyMap()) = Product(
    id = id,
    name = name,
    description = description ?: "",
    price = price,
    imageUrl = imageUrl,
    categoryId = categoryId,
    categoryName = categoriesMap[categoryId] ?: ""
)

fun CartItemResponseDto.toDomain() = CartItem(
    product = product.toDomain(),
    quantity = quantity,
    totalPrice = totalPrice
)

fun CartDto.toDomain() = Cart(
    items = items.map { it.toDomain() },
    totalPrice = totalPrice
)