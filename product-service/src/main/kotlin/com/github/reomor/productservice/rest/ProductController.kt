package com.github.reomor.productservice.rest

import org.springframework.core.env.Environment
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("$V1$PRODUCTS")
class ProductController(
    private val environment: Environment
) {

    @GetMapping
    fun get(): String {
        return "GET" + environment.getProperty("local.server.port")
    }
}