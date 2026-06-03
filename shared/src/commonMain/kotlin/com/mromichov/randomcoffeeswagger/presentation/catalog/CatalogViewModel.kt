package com.mromichov.randomcoffeeswagger.presentation.catalog

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mromichov.randomcoffeeswagger.domain.usecase.AddToCartUseCase
import com.mromichov.randomcoffeeswagger.domain.usecase.ClearCartUseCase
import com.mromichov.randomcoffeeswagger.domain.usecase.CreateOrderUseCase
import com.mromichov.randomcoffeeswagger.domain.usecase.GetCartUseCase
import com.mromichov.randomcoffeeswagger.domain.usecase.GetCategoriesUseCase
import com.mromichov.randomcoffeeswagger.domain.usecase.GetProductsUseCase
import com.mromichov.randomcoffeeswagger.domain.usecase.RemoveFromCartUseCase
import com.mromichov.randomcoffeeswagger.domain.usecase.UpdateCartItemUseCase
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class CatalogViewModel(
    private val getCategoriesUseCase: GetCategoriesUseCase,
    private val getProductsUseCase: GetProductsUseCase,
    private val getCartUseCase: GetCartUseCase,
    private val addToCartUseCase: AddToCartUseCase,
    private val updateCartItemUseCase: UpdateCartItemUseCase,
    private val removeFromCartUseCase: RemoveFromCartUseCase,
    private val clearCartUseCase: ClearCartUseCase,
    private val createOrderUseCase: CreateOrderUseCase
) : ViewModel() {
    private val _uiState = MutableStateFlow(CatalogUiState())
    val uiState: StateFlow<CatalogUiState> = _uiState.asStateFlow()

    init {
        loadInitialData()
    }

    private fun loadInitialData() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, error = null) }

            val categoriesResult = getCategoriesUseCase()
            val productsResult = getProductsUseCase()
            val cartResult = getCartUseCase()

            _uiState.update { state ->
                state.copy(
                    isLoading = false,
                    categories = categoriesResult.getOrElse { emptyList() },
                    products = productsResult.getOrElse { emptyList() },
                    error = if (productsResult.isFailure)
                        "Не удалось загрузить товары" else null
                )
            }
        }
    }

    fun selectCategory(categoryId: Int?) {
        _uiState.update { it.copy(selectedCategoryId = categoryId) }
    }
}