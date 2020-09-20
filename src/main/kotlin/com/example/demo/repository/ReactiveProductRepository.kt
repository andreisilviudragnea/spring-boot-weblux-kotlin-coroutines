package com.example.demo.repository

import com.example.demo.model.Product
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono

interface ReactiveProductRepository {

    fun getProductById(id: Int): Mono<Product>

    fun addNewProduct(name: String, price: Float): Mono<Void>

    fun getAllProducts(): Flux<Product>
}
