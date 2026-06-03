package com.mromichov.randomcoffeeswagger.presentation

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.safeContentPadding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import com.mromichov.randomcoffeeswagger.Greeting
import com.mromichov.randomcoffeeswagger.domain.model.Product
import com.mromichov.randomcoffeeswagger.presentation.catalog.CatalogScreen
import com.mromichov.randomcoffeeswagger.presentation.catalog.ProductCard
import com.mromichov.randomcoffeeswagger.presentation.product.ProductScreen
import kotlinx.serialization.Serializable
import org.jetbrains.compose.resources.painterResource
import randomcoffeeswagger.shared.generated.resources.Res
import randomcoffeeswagger.shared.generated.resources.compose_multiplatform

@Serializable
object CatalogRoute

@Serializable
data class ProductRoute(val productId: Int)

@Composable
fun AppNavigation() {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = CatalogRoute
    ) {
        composable<CatalogRoute> {
            CatalogScreen(
                onProductCardClick = { productid -> navController.navigate(ProductRoute(productid)) }
            )
        }

        composable<ProductRoute> { backStackEntry ->
            val route: ProductRoute = backStackEntry.toRoute()
            ProductScreen(
                productId = route.productId,
                onBack = { navController.popBackStack() }
            )
        }
    }
}

@Composable
fun DefaultScreen() {
    var showContent by remember { mutableStateOf(false) }
    Column(
        modifier = Modifier
            .background(MaterialTheme.colorScheme.primaryContainer)
            .safeContentPadding()
            .fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Button(onClick = { showContent = !showContent }) {
            Text("Click me!")
        }
        AnimatedVisibility(showContent) {
            val greeting = remember { Greeting().greet() }
            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                Image(painterResource(Res.drawable.compose_multiplatform), null)
                Text("Compose: $greeting")
            }
        }
    }
}