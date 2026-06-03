package com.mromichov.randomcoffeeswagger.presentation.catalog

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material.icons.rounded.Remove
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FilledIconButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import com.mromichov.randomcoffeeswagger.domain.model.Product
import org.jetbrains.compose.resources.painterResource
import randomcoffeeswagger.shared.generated.resources.Res
import randomcoffeeswagger.shared.generated.resources.no_image

@Composable
fun ProductCard(
    product: Product,
    onCardClick: () -> Unit,
    onAddClick: () -> Unit,
    onIncrement: () -> Unit,
    onDecrement: () -> Unit,
    quantityInCart: Int,
    modifier: Modifier = Modifier
) {
    Card(
        onClick = onCardClick,
        modifier = modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 8.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            AsyncImage(
                model = product.imageUrl,
                contentDescription = product.name,
                contentScale = ContentScale.Fit,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(100.dp),
                placeholder = painterResource(Res.drawable.no_image),
                error = painterResource(Res.drawable.no_image),
            )

            Text(
                text = product.name,
                style = MaterialTheme.typography.bodyLarge,
                minLines = 2,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis
            )

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 9.dp, vertical = 10.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {

                if (quantityInCart > 0) {
                    QuantityCounter(
                        quantity = quantityInCart,
                        onIncrement = onIncrement,
                        onDecrement = onDecrement,
                        modifier = Modifier.fillMaxWidth()
                    )
                } else {
                    Text(
                        text = "${product.price.toInt()} ₽",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )
                    AddButton(onClick = onAddClick)
                }
            }
        }
    }
}

const val addRemoveBtnSize = 40
const val addRemoveIconSize = 24

@Composable
private fun AddButton(onClick: () -> Unit) {
    FilledIconButton(
        onClick = onClick,
        modifier = Modifier.size(addRemoveBtnSize.dp),
        shape = CircleShape,
        colors = IconButtonDefaults.filledIconButtonColors(
            containerColor = MaterialTheme.colorScheme.primary
        )
    ) {
        Icon(
            imageVector = Icons.Rounded.Add,
            contentDescription = "Добавить",
            modifier = Modifier.size(addRemoveIconSize.dp),
            tint = Color.White
        )
    }
}

@Composable
fun QuantityCounter(
    quantity: Int,
    onIncrement: () -> Unit,
    onDecrement: () -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center
    ) {
        FilledIconButton(
            onClick = onDecrement,
            modifier = Modifier.size(addRemoveBtnSize.dp),
            shape = CircleShape,
            colors = IconButtonDefaults.filledIconButtonColors(
                containerColor = MaterialTheme.colorScheme.secondary
            )
        ) {
            Icon(
                imageVector = Icons.Rounded.Remove,
                contentDescription = "Убрать",
                modifier = Modifier.size(addRemoveIconSize.dp),
                tint = MaterialTheme.colorScheme.onSurface
            )
        }

        Text(
            text = quantity.toString(),
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.widthIn(min = 30.dp),
            textAlign = TextAlign.Center
        )

        FilledIconButton(
            onClick = onIncrement,
            modifier = Modifier.size(addRemoveBtnSize.dp),
            shape = CircleShape,
            colors = IconButtonDefaults.filledIconButtonColors(
                containerColor = MaterialTheme.colorScheme.secondary
            )
        ) {
            Icon(
                imageVector = Icons.Rounded.Add,
                contentDescription = "Добавить",
                modifier = Modifier.size(addRemoveIconSize.dp),
                tint = MaterialTheme.colorScheme.onSurface
            )
        }
    }
}