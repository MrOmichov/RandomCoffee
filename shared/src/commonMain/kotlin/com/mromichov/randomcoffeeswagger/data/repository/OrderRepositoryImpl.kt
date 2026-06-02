package com.mromichov.randomcoffeeswagger.data.repository

import com.mromichov.randomcoffeeswagger.data.api.RandomCoffeeApi
import com.mromichov.randomcoffeeswagger.domain.repository.OrderRepository

class OrderRepositoryImpl(
    private val api: RandomCoffeeApi
) : OrderRepository {

    override suspend fun createOrder() = runCatching {
        api.createOrder()
        Unit
    }
}