package com.example.demo.controller

import com.example.demo.model.Product
import com.example.demo.model.ProductStockView
import com.example.demo.repository.ReactiveProductRepository
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.reactive.function.client.WebClient
import org.springframework.web.reactive.function.client.bodyToMono
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono

@RestController
@RequestMapping("/v1")
class ProductController(
    private val webClient: WebClient,
    private val productRepositoryReactive: ReactiveProductRepository
) {

    @GetMapping("/{id}")
    fun findOne(@PathVariable id: Int): Mono<Product> {
        return productRepositoryReactive.getProductById(id)
    }

    @GetMapping("/{id}/stock")
    fun findOneInStock(@PathVariable id: Int): Mono<ProductStockView> {
        val product = productRepositoryReactive.getProductById(id)

        val stockQuantity = webClient
            .get()
            .uri("/v1/stock-service/product/$id/quantity")
            .accept(MediaType.APPLICATION_JSON)
            .retrieve()
            .bodyToMono<Int>()

        return product.zipWith(stockQuantity) { productInStock, stockQty ->
            ProductStockView(productInStock, stockQty)
        }
    }

    @GetMapping("/stock-service/product/{id}/quantity")
    fun getStockQuantity(@PathVariable id: Int): Mono<Int> {
        return Mono.just(2 * id)
    }

    @GetMapping("")
    fun findAll(): Flux<Product> {
        return productRepositoryReactive.getAllProducts()
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    fun create(@RequestBody product: Product): Mono<Void> {
        return productRepositoryReactive.addNewProduct(product.name, product.price)
    }
}
