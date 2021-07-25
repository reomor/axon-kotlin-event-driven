package com.github.reomor.productservice.rest

import com.github.reomor.productservice.events.command.CreateProductCommand
import com.github.reomor.productservice.events.command.ProductId
import com.github.reomor.productservice.rest.dto.CreateProductRequest
import com.github.reomor.productservice.rest.dto.CreateProductResponse
import org.axonframework.commandhandling.gateway.CommandGateway
import org.springframework.core.env.Environment
import org.springframework.web.bind.annotation.*
import java.util.*

@RestController
@RequestMapping("$V1$PRODUCTS")
class ProductController(
    private val environment: Environment,
    private val commandGateway: CommandGateway
) {

    @PostMapping
    fun create(@RequestBody request: CreateProductRequest): CreateProductResponse {

        val command = CreateProductCommand.build {
            id = ProductId.randomUUID()
            name = request.name
            price = request.price
            quantity = request.quantity
        }

        try {
            return CreateProductResponse(commandGateway.sendAndWait<UUID>(command))
        } catch (e: Exception) {
            throw RuntimeException(e)
        }
    }

    @GetMapping
    fun get(): String {
        return "GET " + environment.getProperty("local.server.port")
    }
}
