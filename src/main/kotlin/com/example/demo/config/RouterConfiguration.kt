package com.example.demo.config

import com.example.demo.handlers.ProductsHandler
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.web.reactive.function.server.coRouter

@Configuration
class RouterConfiguration {

    @Bean
    fun productRoutes(productsHandler: ProductsHandler) = coRouter {
        GET("/v3", productsHandler::findAll)
        POST("/v3", productsHandler::addNewProduct)
        GET("/v3/{id}", productsHandler::findOne)
        GET("/v3/{id}/stock", productsHandler::findOneInStock)
    }
}
