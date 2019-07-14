package com.example.calculator.api.http

import com.example.calculator.model.Calculator
import com.example.calculator.model.CalculatorImpl
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

fun main(args: Array<String>) {
    runApplication<App>(*args)
}

@SpringBootApplication
class App

@Configuration
class Configuration {

    @Bean
    fun calculator(): Calculator = CalculatorImpl()
}