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
    sealed interface GridItem {
        data class Header(val categoryName: String, val categoryId: Int?) : GridItem
        data class ProductItem(val product: Product, val categoryId: Int?) : GridItem
    }

    val flatGridItems: List<GridItem>
        get() {
            val list = mutableListOf<GridItem>()
            val grouped = products.groupBy { it.categoryName }

            grouped.forEach { (categoryName, productsList) ->
                val categoryId = categories.find { it.name == categoryName }?.id
                list.add(GridItem.Header(categoryName, categoryId))
                productsList.distinctBy { it.id }.forEach { product ->
                    list.add(GridItem.ProductItem(product, categoryId))
                }
            }
            return list
        }

    fun getGridIndexByCategoryId(categoryId: Int): Int {
        val index = flatGridItems.indexOfFirst { item ->
            item is GridItem.Header && item.categoryId == categoryId
        }
        return if (index != -1) index else 0
    }

    fun getCategoryIdByGridIndex(index: Int): Int? {
        if (index !in flatGridItems.indices) return null
        return when (val item = flatGridItems[index]) {
            is GridItem.Header -> item.categoryId
            is GridItem.ProductItem -> item.categoryId
        }
    }
}