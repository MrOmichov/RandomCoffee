package com.mromichov.randomcoffeeswagger.data.api

import com.mromichov.randomcoffeeswagger.data.dto.AddToCartRequestDto
import com.mromichov.randomcoffeeswagger.data.dto.CartDto
import com.mromichov.randomcoffeeswagger.data.dto.CategoryDto
import com.mromichov.randomcoffeeswagger.data.dto.OrderResponseDto
import com.mromichov.randomcoffeeswagger.data.dto.ProductDto
import com.mromichov.randomcoffeeswagger.data.dto.UpdateCartItemRequestDto
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.delete
import io.ktor.client.request.get
import io.ktor.client.request.parameter
import io.ktor.client.request.post
import io.ktor.client.request.put
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.contentType
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

class RandomCoffeeApi(
    private val client: HttpClient
) {
    suspend fun getCategories(): List<CategoryDto> =
        client.get("categories").body()


    suspend fun getProducts(
    ): List<ProductDto> =
        client.get("products").body()

    suspend fun getProductById(id: Int): ProductDto =
        client.get("products/$id").body()


    suspend fun getCart(): CartDto =
        client.get("cart").body()

    suspend fun clearCart() {
        client.delete("cart")
    }

    suspend fun addToCart(productId: Int, quantity: Int): CartDto =
        client.post("cart/items") {
            contentType(ContentType.Application.Json)
            setBody(AddToCartRequestDto(productId, quantity))
        }.body()

    suspend fun updateCartItem(productId: Int, quantity: Int): CartDto =
        client.put("cart/items/$productId") {
            contentType(ContentType.Application.Json)
            setBody(UpdateCartItemRequestDto(quantity))
        }.body()

    suspend fun removeFromCart(productId: Int): CartDto =
        client.delete("cart/items/$productId").body()



    suspend fun createOrder(): OrderResponseDto =
        client.post("orders").body()
}