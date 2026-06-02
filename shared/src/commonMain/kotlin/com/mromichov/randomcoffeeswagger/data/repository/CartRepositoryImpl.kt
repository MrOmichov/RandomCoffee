package com.mromichov.randomcoffeeswagger.data.repository

import com.mromichov.randomcoffeeswagger.data.api.RandomCoffeeApi
import com.mromichov.randomcoffeeswagger.data.dto.toDomain
import com.mromichov.randomcoffeeswagger.domain.repository.CartRepository

class CartRepositoryImpl(
    private val api: RandomCoffeeApi
) : CartRepository {

    override suspend fun getCart() = runCatching { api.getCart().toDomain() }

    override suspend fun addItem(productId: Int, quantity: Int) = runCatching {
        api.addToCart(productId, quantity).toDomain()
    }

    override suspend fun updateItem(productId: Int, quantity: Int) = runCatching {
        api.updateCartItem(productId, quantity).toDomain()
    }

    override suspend fun removeItem(productId: Int) = runCatching {
        api.removeFromCart(productId).toDomain()
    }

    override suspend fun clearCart() = runCatching { api.clearCart() }
}