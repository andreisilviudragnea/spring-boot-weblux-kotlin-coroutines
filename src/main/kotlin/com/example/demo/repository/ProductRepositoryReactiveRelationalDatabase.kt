package com.example.demo.repository

import com.example.demo.model.Product
import org.springframework.data.r2dbc.core.R2dbcEntityTemplate
import org.springframework.data.relational.core.query.Criteria.where
import org.springframework.data.relational.core.query.Query
import org.springframework.stereotype.Repository
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono

@Repository
class ProductRepositoryReactiveRelationalDatabase(private val r2dbcEntityTemplate: R2dbcEntityTemplate) : ReactiveProductRepository {

    override fun getProductById(id: Int): Mono<Product> {
        return r2dbcEntityTemplate.selectOne(Query.query(where("id").`is`(id)), Product::class.java)
    }

    override fun addNewProduct(name: String, price: Float): Mono<Void> {
        return r2dbcEntityTemplate.insert(Product(name = name, price = price)).then()
    }

    override fun getAllProducts(): Flux<Product> {
        return r2dbcEntityTemplate.select(Product::class.java).all()
    }
}
