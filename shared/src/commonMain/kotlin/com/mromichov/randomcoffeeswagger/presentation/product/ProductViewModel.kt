package com.mromichov.randomcoffeeswagger.presentation.product

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mromichov.randomcoffeeswagger.domain.usecase.AddToCartUseCase
import com.mromichov.randomcoffeeswagger.domain.usecase.GetProductByIdUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class ProductViewModel(
    private val productId: Int,
    private val getProductByIdUseCase: GetProductByIdUseCase,
) : ViewModel() {

    private val _uiState = MutableStateFlow(ProductUiState())
    val uiState: StateFlow<ProductUiState> = _uiState.asStateFlow()

    init {
        loadProduct()
    }

    private fun loadProduct() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, error = null) }

            getProductByIdUseCase(productId)
                .onSuccess { product ->
                    _uiState.update {
                        it.copy(
                            product = product,
                            isLoading = false
                        )
                    }
                }
                .onFailure { error ->
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            error = error.message ?: "Не удалось загрузить товар"
                        )
                    }
                }
        }
    }

    fun retry() = loadProduct()
}