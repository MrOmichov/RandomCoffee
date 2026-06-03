package com.mromichov.randomcoffeeswagger.di

import com.mromichov.randomcoffeeswagger.data.dto.ProductDto
import com.mromichov.randomcoffeeswagger.data.repository.CartRepositoryImpl
import com.mromichov.randomcoffeeswagger.data.repository.OrderRepositoryImpl
import com.mromichov.randomcoffeeswagger.data.repository.ProductRepositoryImpl
import com.mromichov.randomcoffeeswagger.domain.repository.CartRepository
import com.mromichov.randomcoffeeswagger.domain.repository.OrderRepository
import com.mromichov.randomcoffeeswagger.domain.repository.ProductRepository
import com.mromichov.randomcoffeeswagger.domain.usecase.AddToCartUseCase
import com.mromichov.randomcoffeeswagger.domain.usecase.ClearCartUseCase
import com.mromichov.randomcoffeeswagger.domain.usecase.CreateOrderUseCase
import com.mromichov.randomcoffeeswagger.domain.usecase.GetCartUseCase
import com.mromichov.randomcoffeeswagger.domain.usecase.GetCategoriesUseCase
import com.mromichov.randomcoffeeswagger.domain.usecase.GetProductByIdUseCase
import com.mromichov.randomcoffeeswagger.domain.usecase.GetProductsUseCase
import com.mromichov.randomcoffeeswagger.domain.usecase.RemoveFromCartUseCase
import com.mromichov.randomcoffeeswagger.domain.usecase.UpdateCartItemUseCase
import com.mromichov.randomcoffeeswagger.presentation.catalog.CatalogViewModel
import com.mromichov.randomcoffeeswagger.presentation.product.ProductViewModel
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

val appModule = module {
    single<ProductRepository> { ProductRepositoryImpl(get()) }
    single<CartRepository>    { CartRepositoryImpl(get()) }
    single<OrderRepository>   { OrderRepositoryImpl(get()) }

    factory { GetCategoriesUseCase(get()) }
    factory { GetProductsUseCase(get()) }
    factory { GetProductByIdUseCase(get()) }
    factory { AddToCartUseCase(get()) }
    factory { GetCartUseCase(get()) }
    factory { UpdateCartItemUseCase(get()) }
    factory { RemoveFromCartUseCase(get()) }
    factory { ClearCartUseCase(get()) }
    factory { CreateOrderUseCase(get()) }

    viewModel {
        CatalogViewModel(
            get(),
            get(),
            get(),
            get(),
            get(),
            get(),
            get(),
            get(),
        )
    }
    viewModel { (productId: Int) ->
        ProductViewModel(
            productId = productId,
            get()
        )
    }
}