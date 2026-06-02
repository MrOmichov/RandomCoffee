package com.mromichov.randomcoffeeswagger.domain.usecase

import com.mromichov.randomcoffeeswagger.domain.repository.OrderRepository

class CreateOrderUseCase(private val repository: OrderRepository) {
    suspend operator fun invoke(): Result<Unit> = repository.createOrder()
}