package com.mromichov.randomcoffeeswagger.presentation.catalog

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.grid.itemsIndexed
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SelectableChipElevation
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.mromichov.randomcoffeeswagger.domain.model.Category
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import org.koin.compose.viewmodel.koinViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CatalogScreen(
    onProductCardClick: (Int) -> Unit,
    viewModel: CatalogViewModel = koinViewModel(),
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    Scaffold(
        floatingActionButton = {
            if (!uiState.isCartEmpty)
                FloatingActionButton(
                    onClick = { viewModel.changeCartVisibility(true) },
                    shape = RoundedCornerShape(10.dp),
                    containerColor = MaterialTheme.colorScheme.primary,
                    contentColor = Color.White,
                ) {
                    Row(
                        modifier = Modifier.padding(20.dp),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.ShoppingCart,
                            contentDescription = "Cart"
                        )
                        Text(
                            text = "${uiState.cartTotal} ₽",
                            style = MaterialTheme.typography.bodyMedium,
                        )
                    }
                }
        },
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            when {
                uiState.isLoading -> {
                    CircularProgressIndicator(
                        modifier = Modifier.align(Alignment.Center)
                    )
                }

                uiState.error != null && uiState.products.isEmpty() -> {
                    ErrorState(
                        message = uiState.error!!,
                        onRetry = {},
                        modifier = Modifier.align(Alignment.Center)
                    )
                }

                else -> {
                    CatalogContent(
                        uiState = uiState,
                        onProductCardClick = onProductCardClick,
                        onCategorySelect = { id -> viewModel.selectCategory(id) },
                        onAddClick = viewModel::addToCart,
                        onIncrement = viewModel::incrementItem,
                        onDecrement = viewModel::decrementItem,
                    )

                    val sheetState = rememberModalBottomSheetState(
                        skipPartiallyExpanded = false
                    )
                    if (uiState.isCartShown)
                        CartBottomSheet(
                            viewModel = viewModel,
                            uiState = uiState,
                            sheetState = sheetState,
                            onDismissRequest = { viewModel.changeCartVisibility(false) },
                            modifier = Modifier
                                .padding(horizontal = 18.dp, vertical = 9.dp),
                        )
                }
            }

        }
    }
}

@Composable
fun CatalogContent(
    uiState: CatalogUiState,
    onProductCardClick: (Int) -> Unit,
    onCategorySelect: (Int?) -> Unit,
    onAddClick: (Int) -> Unit,
    onIncrement: (Int) -> Unit,
    onDecrement: (Int) -> Unit,
) {
    val gridState = rememberLazyGridState()
    val rowState = rememberLazyListState()
    val coroutineScope = rememberCoroutineScope()

    var isClickTriggered by remember { mutableStateOf(false) }

    val currentVisibleCategoryId by remember {
        derivedStateOf {
            val firstVisibleIndex = gridState.firstVisibleItemIndex
            uiState.getCategoryIdByGridIndex(firstVisibleIndex)
        }
    }

    LaunchedEffect(currentVisibleCategoryId) {
        val newId = currentVisibleCategoryId
        if (!isClickTriggered && newId != null && newId != uiState.selectedCategoryId) {
            onCategorySelect(newId)
        }
    }

    LaunchedEffect(uiState.selectedCategoryId) {
        val targetId = uiState.selectedCategoryId
        if (targetId != null) {
            val categoryIndex = uiState.categories.indexOfFirst { it.id == targetId }
            if (categoryIndex != -1) {
                rowState.animateScrollToItem(categoryIndex)
            }
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
    ) {
        CategoryRow(
            state = rowState,
                selectedCategoryId = uiState.selectedCategoryId,
            categories = uiState.categories,
            onCategorySelect = { categoryId ->
                coroutineScope.launch {
                    isClickTriggered = true

                    onCategorySelect(categoryId)
                    val targetGridIndex = uiState.getGridIndexByCategoryId(categoryId)
                    gridState.animateScrollToItem(targetGridIndex)

                    isClickTriggered = false
                }
            }
        )

        LazyVerticalGrid(
            state = gridState,
            columns = GridCells.Fixed(2),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(18.dp),
            horizontalArrangement = Arrangement.spacedBy(18.dp),
        ) {
            itemsIndexed(
                items = uiState.flatGridItems,
                key = { _, item ->
                    when (item) {
                        is CatalogUiState.GridItem.Header -> "header_${item.categoryName}"
                        is CatalogUiState.GridItem.ProductItem -> "product_${item.product.id}"
                    }
                },
                span = { _, item ->
                    val spanCount = if (item is CatalogUiState.GridItem.Header) maxLineSpan else 1
                    GridItemSpan(spanCount)
                }
            ) { _, item ->
                when (item) {
                    is CatalogUiState.GridItem.Header -> {
                        Text(
                            text = item.categoryName,
                            style = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
                        )
                    }
                    is CatalogUiState.GridItem.ProductItem -> {
                        ProductCard(
                            product = item.product,
                            onCardClick = { onProductCardClick(item.product.id) },
                            onAddClick = { onAddClick(item.product.id) },
                            onIncrement = { onIncrement(item.product.id) },
                            onDecrement = { onDecrement(item.product.id) },
                            quantityInCart = uiState.quantityInCart(item.product.id),
                            modifier = Modifier,
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun CategoryRow(
    state: LazyListState,
    categories: List<Category>,
    selectedCategoryId: Int?,
    onCategorySelect: (Int) -> Unit
) {
    Surface(
        color = MaterialTheme.colorScheme.background
    ) {
        LazyRow(
            state = state,
            contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            items(items = categories, key = { it.id }) { category ->
                CategoryChip(
                    category = category,
                    isSelected = category.id == selectedCategoryId,
                    onClick = { onCategorySelect(category.id) }
                )
            }
        }
    }
}
@Composable
fun CategoryChip(
    category: Category,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    FilterChip(
        selected = isSelected,
        onClick = onClick,
        label = {
            Text(
                text = category.name,
                style = MaterialTheme.typography.labelLarge
            )
        },
        shape = RoundedCornerShape(50.dp),
        border = null,
        colors = FilterChipDefaults.filterChipColors(
            selectedContainerColor = MaterialTheme.colorScheme.primary,
            selectedLabelColor = MaterialTheme.colorScheme.surface,
            disabledContainerColor = MaterialTheme.colorScheme.surface,
            disabledLabelColor = MaterialTheme.colorScheme.onSurface,
        )
    )
}

@Composable
private fun ErrorState(
    message: String,
    onRetry: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Text(
            text = message,
            style = MaterialTheme.typography.bodyLarge,
            textAlign = TextAlign.Center
        )
        Button(
            onClick = onRetry,
            colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary)
        ) {
            Text("Повторить")
        }
    }
}