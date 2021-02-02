package com.example.demo.config

import io.r2dbc.spi.ConnectionFactories
import io.r2dbc.spi.ConnectionFactory
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.data.r2dbc.config.AbstractR2dbcConfiguration
import org.springframework.data.r2dbc.repository.config.EnableR2dbcRepositories

@Configuration
@EnableR2dbcRepositories
class RelationalDatastoreConfig : AbstractR2dbcConfiguration() {
    @Value("\${spring.datasource.username}")
    private val username: String = ""

    @Value("\${spring.datasource.password}")
    private val password: String = ""

    @Value("\${spring.datasource.dbpath}")
    private val dbPath: String = ""

    @Bean
    override fun connectionFactory(): ConnectionFactory {
        return ConnectionFactories.get("r2dbc:h2:file:///$dbPath?options=USER=$username;PASSWORD=$password")
    }
}
