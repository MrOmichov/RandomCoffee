package com.mromichov.randomcoffeeswagger.presentation.catalog

import com.mromichov.randomcoffeeswagger.domain.model.Category
import com.mromichov.randomcoffeeswagger.domain.model.Product

data class CatalogUiState(
    val categories: List<Category> = emptyList(),
    val selectedCategoryId: Int? = null,
    val products: List<Product> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null,


) {
    val groupedProducts: Map<String, List<Product>>
        get() = products.groupBy { it.categoryName }
}