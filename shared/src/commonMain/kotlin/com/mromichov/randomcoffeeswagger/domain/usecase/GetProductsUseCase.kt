package com.mromichov.randomcoffeeswagger.domain.usecase

import com.mromichov.randomcoffeeswagger.domain.model.Product
import com.mromichov.randomcoffeeswagger.domain.repository.ProductRepository

class GetProductsUseCase(
    private val repository: ProductRepository
) {
    suspend operator fun invoke(
        page: Int,
        categoryId: Int?
    ): Result<List<Product>> =
        repository.getProducts(page = page, categoryId = categoryId)
}