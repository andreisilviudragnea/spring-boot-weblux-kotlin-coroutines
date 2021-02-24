package com.example.demo.handlers

import com.example.demo.model.Product
import com.example.demo.model.ProductStockView
import com.example.demo.repository.ProductRepository
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import org.springframework.http.MediaType
import org.springframework.stereotype.Component
import org.springframework.web.reactive.function.client.WebClient
import org.springframework.web.reactive.function.client.awaitBody
import org.springframework.web.reactive.function.server.ServerRequest
import org.springframework.web.reactive.function.server.ServerResponse
import org.springframework.web.reactive.function.server.awaitBody
import org.springframework.web.reactive.function.server.bodyAndAwait
import org.springframework.web.reactive.function.server.bodyValueAndAwait
import org.springframework.web.reactive.function.server.buildAndAwait
import org.springframework.web.reactive.function.server.json

@Component
class ProductsHandler(
    private val webClient: WebClient,
    private val productRepositoryRedis: ProductRepository
) {

    suspend fun findAll(request: ServerRequest): ServerResponse =
        ServerResponse.ok().json().bodyAndAwait(productRepositoryRedis.getAllProducts())

    suspend fun findOneInStock(request: ServerRequest): ServerResponse = coroutineScope {
        val id = request.pathVariable("id").toInt()

        val product = async {
            productRepositoryRedis.getProductById(id)
        }

        val quantity = async {
            webClient
                .get()
                .uri("/v1/stock-service/product/$id/quantity")
                .accept(MediaType.APPLICATION_JSON)
                .retrieve()
                .awaitBody<Int>()
        }

        ServerResponse
            .ok()
            .json()
            .bodyValueAndAwait(ProductStockView(product.await()!!, quantity.await()))
    }

    suspend fun findOne(request: ServerRequest): ServerResponse {
        val id = request.pathVariable("id").toInt()

        val productById = productRepositoryRedis.getProductById(id) ?: return ServerResponse.notFound().buildAndAwait()

        return ServerResponse
            .ok()
            .json()
            .bodyValueAndAwait(productById)
    }

    suspend fun addNewProduct(request: ServerRequest): ServerResponse {
        val (_, name, price) = request.awaitBody<Product>()

        productRepositoryRedis.addNewProduct(name, price)

        return ServerResponse.ok().buildAndAwait()
    }
}
