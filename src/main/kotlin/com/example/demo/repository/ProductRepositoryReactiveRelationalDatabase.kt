package com.example.demo.repository

import com.example.demo.model.Product
import org.springframework.data.r2dbc.core.DatabaseClient
import org.springframework.stereotype.Repository
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono

@Repository
class ProductRepositoryReactiveRelationalDatabase(private val client: DatabaseClient) : ReactiveProductRepository {

    override fun getProductById(id: Int): Mono<Product> {
        return client
                .execute("SELECT * FROM products WHERE id = $1")
                .bind(0, id)
                .`as`(Product::class.java)
                .fetch()
                .one()
    }

    override fun addNewProduct(name: String, price: Float): Mono<Void> {
        return client
                .execute("INSERT INTO products (name, price) VALUES($1, $2)")
                .bind(0, name)
                .bind(1, price)
                .then()
    }

    override fun getAllProducts(): Flux<Product> {
        return client.select().from("products")
                .`as`(Product::class.java)
                .fetch()
                .all()
                .log()
    }
}
