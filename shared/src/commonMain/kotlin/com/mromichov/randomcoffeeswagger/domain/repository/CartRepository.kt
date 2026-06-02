package com.mromichov.randomcoffeeswagger.domain.repository

import com.mromichov.randomcoffeeswagger.domain.model.Cart

interface CartRepository {
    suspend fun getCart(): Result<Cart>
    suspend fun addItem(productId: Int, quantity: Int): Result<Cart>
    suspend fun updateItem(productId: Int, quantity: Int): Result<Cart>
    suspend fun removeItem(productId: Int): Result<Cart>
    suspend fun clearCart(): Result<Unit>
}