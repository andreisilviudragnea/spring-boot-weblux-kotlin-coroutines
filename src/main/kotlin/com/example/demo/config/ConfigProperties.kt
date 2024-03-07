package com.example.demo.config

import org.springframework.boot.context.properties.ConfigurationProperties

@ConfigurationProperties("config")
data class ConfigProperties(
    val stringValue: String,
    val intValue: Int,
    val listValue: List<String>,
)
