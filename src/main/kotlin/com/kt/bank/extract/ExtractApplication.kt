package com.kt.bank.extract

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.cloud.openfeign.EnableFeignClients


@SpringBootApplication
@EnableFeignClients
class ExtractApplication

fun main(args: Array<String>) {
    runApplication<ExtractApplication>(*args)
}
