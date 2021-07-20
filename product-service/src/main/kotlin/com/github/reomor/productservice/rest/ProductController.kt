package com.github.reomor.productservice.rest

import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("$V1$PRODUCTS")
class ProductController {

    @GetMapping
    fun get(): String {
        return "GET"
    }
}