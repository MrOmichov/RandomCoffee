package com.mromichov.randomcoffeeswagger.presentation.catalog

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.FilterChip
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.mromichov.randomcoffeeswagger.domain.model.Category
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun CatalogScreen(
    onProductCardClick: (Int) -> Unit,
    viewModel: CatalogViewModel = koinViewModel(),
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    Scaffold(
//        floatingActionButton = {
//            if (!uiState.isCartEmpty)
//                FloatingActionButton(
//                    onClick = { viewModel.changeCartVisibility(true) },
//                    shape = RoundedCornerShape(10.dp),
//                    containerColor = MaterialTheme.colorScheme.primary,
//                    contentColor = Color.White,
//                ) {
//                    Row(
//                        modifier = Modifier.padding(20.dp),
//                        horizontalArrangement = Arrangement.spacedBy(8.dp)
//                    ) {
//                        Icon(
//                            imageVector = Icons.Default.ShoppingCart,
//                            contentDescription = "Cart"
//                        )
//                        Text(
//                            text = "Summa",
//                            style = MaterialTheme.typography.bodyMedium,
//                        )
//                    }
//                }
//        },
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
                        onCategorySelect = {},
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
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
    ) {
        CategoryRow(
            categories = uiState.categories,
            selectedCategoryId = uiState.selectedCategoryId,
            onCategorySelect = onCategorySelect
        )

        LazyVerticalGrid(
            columns = GridCells.Fixed(2),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(18.dp),
            horizontalArrangement = Arrangement.spacedBy(18.dp),
        ) {
            val uniqueProducts = uiState.products.distinctBy { it.id }
            items(
                items = uniqueProducts,
                key = { it.id }
            ) { product ->
                Text(text = "Заголовок типа")
                ProductCard(
                    product = product,
                    onCardClick = { onProductCardClick(product.id) },
                    onAddClick = {},
                    onIncrement = {},
                    onDecrement = {},
                    quantityInCart = 0,
                    modifier = Modifier,
                )
            }
        }
    }
}

@Composable
fun CategoryRow(
    categories: List<Category>,
    selectedCategoryId: Int?,
    onCategorySelect: (Int) -> Unit
) {
    Surface {
        LazyRow(
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
        shape = RoundedCornerShape(50.dp)
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