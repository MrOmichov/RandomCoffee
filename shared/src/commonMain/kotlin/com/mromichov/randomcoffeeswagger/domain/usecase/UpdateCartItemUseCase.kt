package com.mromichov.randomcoffeeswagger.domain.usecase

import com.mromichov.randomcoffeeswagger.domain.model.Cart
import com.mromichov.randomcoffeeswagger.domain.repository.CartRepository

class UpdateCartItemUseCase(private val repository: CartRepository) {
    suspend operator fun invoke(productId: Int, quantity: Int): Result<Cart> =
        repository.updateItem(productId, quantity)
}