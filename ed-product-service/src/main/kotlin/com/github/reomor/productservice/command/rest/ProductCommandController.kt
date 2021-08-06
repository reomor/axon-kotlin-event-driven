package com.github.reomor.productservice.command.rest

import com.github.reomor.productservice.command.event.command.CreateProductCommand
import com.github.reomor.productservice.command.rest.dto.CreateProductRequest
import com.github.reomor.productservice.command.rest.dto.CreateProductResponse
import com.github.reomor.productservice.core.ProductId
import org.axonframework.commandhandling.gateway.CommandGateway
import org.springframework.web.bind.annotation.*
import java.util.*
import javax.validation.Valid

@RestController
@RequestMapping("/v1/products/command")
class ProductCommandController(
    private val commandGateway: CommandGateway
) {
    @PostMapping
    fun create(
        @Valid @RequestBody request: CreateProductRequest
    ): CreateProductResponse {

        val command = CreateProductCommand.build {
            productId = ProductId(UUID.randomUUID().toString())
            name = request.name
            price = request.price
            quantity = request.quantity
        }

        return CreateProductResponse(ProductId(commandGateway.sendAndWait<String>(command)))
    }
}
