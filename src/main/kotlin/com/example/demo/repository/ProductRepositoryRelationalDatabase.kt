package com.example.demo.repository

import com.example.demo.model.Product
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.reactive.asFlow
import kotlinx.coroutines.reactive.awaitFirstOrNull
import org.springframework.stereotype.Repository

@Repository
class ProductRepositoryRelationalDatabase(private val reactiveProductRepository: ReactiveProductRepository) : ProductRepository {

    override suspend fun getProductById(id: Int): Product? =
            reactiveProductRepository.getProductById(id).awaitFirstOrNull()

    override suspend fun addNewProduct(name: String, price: Float) {
        reactiveProductRepository.addNewProduct(name, price).awaitFirstOrNull()
    }

    override fun getAllProducts(): Flow<Product> =
            reactiveProductRepository.getAllProducts().asFlow()
}
