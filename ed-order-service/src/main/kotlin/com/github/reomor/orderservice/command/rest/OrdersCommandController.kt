package com.github.reomor.orderservice.command.rest

import com.github.reomor.core.OrderId
import com.github.reomor.core.UserId
import com.github.reomor.orderservice.command.CreateOrderCommand
import com.github.reomor.orderservice.command.rest.dto.CreateOrderRequest
import com.github.reomor.orderservice.command.rest.dto.CreateOrderResponse
import com.github.reomor.orderservice.core.OrderStatus
import com.github.reomor.orderservice.query.FindOrderQuery
import com.github.reomor.orderservice.query.rest.OrderInfo
import org.axonframework.commandhandling.gateway.CommandGateway
import org.axonframework.messaging.responsetypes.ResponseTypes
import org.axonframework.queryhandling.QueryGateway
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/v1/orders/command")
class OrdersCommandController(
  private val commandGateway: CommandGateway,
  private val queryGateway: QueryGateway
) {

  @PostMapping
  fun create(
    @RequestBody request: CreateOrderRequest
  ): CreateOrderResponse {

    val userId = UserId("27b95829-4f3f-4ddf-8983-151ba010e35b")

    val command = CreateOrderCommand.build {
      productId = request.productId
      quantity = request.quantity
      addressId = request.addressId
      orderStatus = OrderStatus.CREATED
    }.copy(userId = userId)

    val subscriptionQueryResult = queryGateway.subscriptionQuery<FindOrderQuery, OrderInfo, OrderInfo>(
      FindOrderQuery(command.orderId),
      ResponseTypes.instanceOf(OrderInfo::class.java),
      ResponseTypes.instanceOf(OrderInfo::class.java)
    )

    subscriptionQueryResult.use { queryResult ->
      commandGateway.sendAndWait<String>(command)
      val orderInfo = queryResult.updates().blockFirst() ?: throw IllegalArgumentException("QueryResult is null")
      return CreateOrderResponse(
        orderId = OrderId(orderInfo.orderId),
        orderStatus = orderInfo.orderStatus,
        message = orderInfo.message
      )
    }
  }
}
