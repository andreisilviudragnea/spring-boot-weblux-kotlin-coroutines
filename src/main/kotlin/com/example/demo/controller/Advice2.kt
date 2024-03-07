package com.example.demo.controller

import org.springframework.core.annotation.Order
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler

@ControllerAdvice
@Order(2)
class Advice2 {
    @ExceptionHandler
    fun handle(e: Exception1) {
        println("Handled exception 1")
    }

    @ExceptionHandler
    fun handle(e: Exception2) {
        println("Handled exception 2")
    }
}
