package com.mromichov.randomcoffeeswagger.presentation.product

import com.mromichov.randomcoffeeswagger.domain.model.Product

data class ProductUiState (
    val product: Product? = null,
    val isLoading: Boolean = false,
    val error: String? = null,
)