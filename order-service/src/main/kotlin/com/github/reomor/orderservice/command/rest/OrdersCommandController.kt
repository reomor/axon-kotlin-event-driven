package com.github.reomor.orderservice.command.rest

import com.github.reomor.orderservice.command.event.command.CreateOrderCommand
import com.github.reomor.orderservice.command.rest.dto.CreateOrderRequest
import com.github.reomor.orderservice.command.rest.dto.CreateOrderResponse
import com.github.reomor.orderservice.core.OrderId
import com.github.reomor.orderservice.core.OrderStatus
import org.axonframework.commandhandling.gateway.CommandGateway
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/v1/orders/command")
class OrdersCommandController(
  private val commandGateway: CommandGateway
) {

  @PostMapping
  fun create(
    @RequestBody request: CreateOrderRequest
  ): CreateOrderResponse {

    val command = CreateOrderCommand.build {
      productId = request.productId
      quantity = request.quantity
      addressId = request.addressId
      orderStatus = OrderStatus.CREATED
    }

    return CreateOrderResponse(OrderId(commandGateway.sendAndWait<String>(command)))
  }
}