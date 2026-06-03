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
                    cart = cartResult.getOrNull(),
                    error = if (productsResult.isFailure)
                        "Не удалось загрузить товары" else null
                )
            }
        }
    }

    fun loadCart() {
        viewModelScope.launch {
            getCartUseCase()
                .onSuccess { cart -> _uiState.update { it.copy(cart = cart) } }
        }
    }

    fun selectCategory(categoryId: Int?) {
        _uiState.update { it.copy(selectedCategoryId = categoryId) }
    }

    fun changeCartVisibility(visible: Boolean) {
        _uiState.update { it.copy(isCartShown = visible) }
    }

    fun addToCart(productId: Int) {
        viewModelScope.launch {
            addToCartUseCase(productId, 1)
                .onSuccess { cart ->
                    _uiState.update { it.copy(cart = cart) }
                }
        }
    }

    fun incrementItem(productId: Int) {
        val current = _uiState.value.cart?.items
            ?.find { it.product.id == productId }?.quantity ?: return

        if (current >= 10) return

        viewModelScope.launch {
            updateCartItemUseCase(productId, current + 1)
                .onSuccess { cart ->
                    _uiState.update { it.copy(cart = cart) }
                }
                .onFailure { loadCart() }
        }
    }

    fun decrementItem(productId: Int) {
        val current = _uiState.value.cart?.items
            ?.find { it.product.id == productId }?.quantity ?: return

        viewModelScope.launch {
            if (current <= 1) {
                removeFromCartUseCase(productId)
                    .onSuccess { cart -> _uiState.update { it.copy(cart = cart) } }
                    .onFailure { loadCart() }
            } else {
                updateCartItemUseCase(productId, current - 1)
                    .onSuccess { cart -> _uiState.update { it.copy(cart = cart) } }
                    .onFailure { loadCart() }
            }
        }
    }

    fun removeItem(productId: Int) {
        viewModelScope.launch {
            removeFromCartUseCase(productId)
                .onSuccess { cart -> _uiState.update { it.copy(cart = cart) } }
                .onFailure { loadCart() }
        }
    }

    fun clearCart() {
        viewModelScope.launch {
            clearCartUseCase()
                .onSuccess { _uiState.update { it.copy(cart = null, isCartShown = false) } }
        }
    }



    fun createOrder() {
        if (_uiState.value.isCartEmpty) return
        viewModelScope.launch {
            _uiState.update { it.copy(isOrdering = true, orderError = false) }
            createOrderUseCase()
                .onSuccess {
                    _uiState.update { it.copy(
                        isOrdering = false,
                        orderSuccess = true,
                        cart = null,
                        isCartShown = false
                    )}
                }
                .onFailure { error ->
                    _uiState.update { it.copy(
                        isOrdering = false,
                        orderError = true
                    )}
                }
        }
    }

    fun consumeOrderSuccess() {
        _uiState.update {
            it.copy(
                orderSuccess = false,
                isCartShown = false,
                snackbarMsg = "Заказ создан"
            )
        }
    }

    fun consumeOrderError() {
        _uiState.update {
            it.copy(
                orderError = false,
                isCartShown = false,
                snackbarMsg = "Произошла ошибка"
            )
        }
    }

    fun endSnackbar() {
        _uiState.update { it.copy(snackbarMsg = null) }
    }
}