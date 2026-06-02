package com.mromichov.randomcoffeeswagger.data.repository

import com.mromichov.randomcoffeeswagger.data.api.RandomCoffeeApi
import com.mromichov.randomcoffeeswagger.data.dto.toDomain
import com.mromichov.randomcoffeeswagger.domain.repository.ProductRepository
import com.mromichov.randomcoffeeswagger.domain.model.Product
import com.mromichov.randomcoffeeswagger.domain.model.Category


class ProductRepositoryImpl(
    private val api: RandomCoffeeApi
) : ProductRepository {

    override suspend fun getProducts(
        page: Int,
        categoryId: Int?
    ): Result<List<Product>> = runCatching {
        val categories: Map<Int, String> = api.getCategories().associate { it.id to it.name }

        api.getProducts(page = page, categoryId = categoryId)
            .map { it.toDomain(categories) }
    }

    override suspend fun getProductById(id: Int): Result<Product> = runCatching {
        val categories: Map<Int, String> = api.getCategories().associate { it.id to it.name }
        api.getProductById(id).toDomain(categories)
    }

    override suspend fun getCategories(): Result<List<Category>> = runCatching {
        val categories = api.getCategories().map { it.toDomain() }
        categories
    }
}