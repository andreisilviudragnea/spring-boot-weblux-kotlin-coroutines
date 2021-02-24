package com.example.demo.config

import com.example.demo.model.Product
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.data.redis.connection.ReactiveRedisConnectionFactory
import org.springframework.data.redis.connection.RedisConnectionFactory
import org.springframework.data.redis.core.ReactiveRedisTemplate
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer
import org.springframework.data.redis.serializer.RedisSerializationContext
import org.springframework.data.redis.serializer.StringRedisSerializer
import javax.annotation.PreDestroy

@Configuration
class RedisConfig(private val factory: RedisConnectionFactory) {

    @Bean
    fun reactiveRedisTemplate(factory: ReactiveRedisConnectionFactory): ReactiveRedisTemplate<String, Product> {
        return ReactiveRedisTemplate(
            factory,
            RedisSerializationContext
                .newSerializationContext<String, Product>(StringRedisSerializer())
                .value(Jackson2JsonRedisSerializer(Product::class.java))
                .build()
        )
    }

    @PreDestroy
    fun cleanRedis() {
        factory.connection.flushDb()
    }
}
