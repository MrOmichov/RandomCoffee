package com.mromichov.randomcoffeeswagger.domain.repository

import com.mromichov.randomcoffeeswagger.domain.model.Category
import com.mromichov.randomcoffeeswagger.domain.model.Product

interface ProductRepository {
    suspend fun getProducts(): Result<List<Product>>
    suspend fun getProductById(id: Int): Result<Product>
    suspend fun getCategories(): Result<List<Category>>
}