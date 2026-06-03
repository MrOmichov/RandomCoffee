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

    val gridIndexToCategoryIdMap: Map<Int, Int>
        get() {
            val mapping = mutableMapOf<Int, Int>()
            var globalIndex = 0

            groupedProducts.forEach { (categoryName, products) ->
                val categoryId = categories.find { it.name == categoryName }?.id
                if (categoryId != null) {
                    mapping[globalIndex] = categoryId
                    globalIndex += 1

                    val uniqueProductsCount = products.distinctBy { it.id }.size
                    for (i in 0 until uniqueProductsCount) {
                        mapping[globalIndex] = categoryId
                        globalIndex += 1
                    }
                }
            }
            return mapping
        }

    val categoryToGridIndexMap: Map<Int, Int>
        get() {
            val mapping = mutableMapOf<Int, Int>()
            var globalIndex = 0

            groupedProducts.forEach { (categoryName, products) ->
                val categoryId = categories.find { it.name == categoryName }?.id
                if (categoryId != null) {
                    mapping[categoryId] = globalIndex
                }

                globalIndex += 1

                val uniqueProductsCount = products.distinctBy { it.id }.size
                globalIndex += uniqueProductsCount
            }
            return mapping
        }
}