package com.example.demo.repository

import com.example.demo.model.Product
import kotlinx.coroutines.flow.Flow

interface ProductRepository {
    suspend fun getProductById(id: Int): Product?

    suspend fun addNewProduct(
        name: String,
        price: Float,
    )

    fun getAllProducts(): Flow<Product>
}
