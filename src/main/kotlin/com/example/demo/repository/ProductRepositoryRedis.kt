package com.example.demo.repository

import com.example.demo.model.Product
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.reactive.awaitFirstOrNull
import org.springframework.data.redis.core.ReactiveRedisTemplate
import org.springframework.data.redis.core.rangeAsFlow
import org.springframework.data.redis.core.rightPushAndAwait
import org.springframework.data.redis.core.setAndAwait
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Repository
import org.springframework.web.server.ResponseStatusException
import kotlin.random.Random

@Repository
class ProductRepositoryRedis(private val reactiveRedisTemplate: ReactiveRedisTemplate<String, Product>) : ProductRepository {
    override suspend fun getProductById(id: Int): Product? {
        return reactiveRedisTemplate
                .opsForValue()
                .get(id.toString())
                .awaitFirstOrNull()
    }

    override suspend fun addNewProduct(name: String, price: Float) {
        val id = Random.nextInt()

        println("Created product with id=$id")

        val product = Product(id, name, price)

        val setResult = reactiveRedisTemplate
                .opsForValue()
                .setAndAwait(id.toString(), product)

        if (!setResult) {
            throw ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR)
        }

        reactiveRedisTemplate.opsForList().rightPushAndAwait("products", product)
    }

    override fun getAllProducts(): Flow<Product> {
        return reactiveRedisTemplate
                .opsForList()
                .rangeAsFlow("products", 0, -1)
    }
}
