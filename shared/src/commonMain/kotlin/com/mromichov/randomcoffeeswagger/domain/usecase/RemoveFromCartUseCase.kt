package com.mromichov.randomcoffeeswagger.domain.usecase

import com.mromichov.randomcoffeeswagger.domain.model.Cart
import com.mromichov.randomcoffeeswagger.domain.repository.CartRepository


class RemoveFromCartUseCase(private val repository: CartRepository) {
    suspend operator fun invoke(productId: Int): Result<Cart> =
        repository.removeItem(productId)
}