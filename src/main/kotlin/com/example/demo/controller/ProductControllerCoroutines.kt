package com.example.demo.controller

import com.example.demo.model.Product
import com.example.demo.model.ProductStockView
import com.example.demo.repository.ProductRepository
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.Flow
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType.APPLICATION_JSON
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.reactive.function.client.WebClient
import org.springframework.web.reactive.function.client.awaitBody

@RestController
@RequestMapping("/v2")
class ProductControllerCoroutines(
    private val webClient: WebClient,
    private val productRepositoryRedis: ProductRepository,
) {
    @GetMapping("/{id}")
    suspend fun findOne(
        @PathVariable id: Int,
    ): Product? {
        return productRepositoryRedis.getProductById(id)
    }

    @GetMapping("/{id}/stock")
    suspend fun findOneInStock(
        @PathVariable id: Int,
    ): ProductStockView =
        coroutineScope {
            val product =
                async {
                    productRepositoryRedis.getProductById(id)
                }

            val quantity =
                async {
                    webClient
                        .get()
                        .uri("/v1/stock-service/product/$id/quantity")
                        .accept(APPLICATION_JSON)
                        .retrieve()
                        .awaitBody<Int>()
                }

            ProductStockView(product.await()!!, quantity.await())
        }

    @GetMapping
    fun findAll(): Flow<Product> {
        return productRepositoryRedis.getAllProducts()
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    suspend fun create(
        @RequestBody product: Product,
    ) {
        productRepositoryRedis.addNewProduct(product.name, product.price)
    }
}
