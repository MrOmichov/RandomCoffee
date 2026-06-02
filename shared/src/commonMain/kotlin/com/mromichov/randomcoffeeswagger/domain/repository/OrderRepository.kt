package com.mromichov.randomcoffeeswagger.domain.repository

interface OrderRepository {
    suspend fun createOrder(): Result<Unit>
}