package com.example.demo.controller

import com.example.demo.config.ConfigProperties
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/configs")
class ConfigPropertiesController(val configProperties: ConfigProperties) {
    @GetMapping("/string")
    fun getStringConfig() = configProperties.stringValue

    @GetMapping("/int")
    fun getIntConfig() = configProperties.intValue

    @GetMapping("/list")
    fun getListConfig() = configProperties.listValue
}
