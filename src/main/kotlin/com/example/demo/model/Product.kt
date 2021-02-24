package com.example.demo.model

import org.springframework.data.annotation.Id

data class Product(
    @Id
    val id: Int = 0,
    val name: String = "",
    val price: Float = 0.0f
)
