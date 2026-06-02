package com.mromichov.randomcoffeeswagger.domain.usecase

import com.mromichov.randomcoffeeswagger.domain.model.Cart
import com.mromichov.randomcoffeeswagger.domain.repository.CartRepository

class GetCartUseCase(private val repository: CartRepository) {
    suspend operator fun invoke(): Result<Cart> = repository.getCart()
}