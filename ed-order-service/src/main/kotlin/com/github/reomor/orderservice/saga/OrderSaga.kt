package com.github.reomor.orderservice.saga

import com.github.reomor.core.command.ReserveProductCommand
import com.github.reomor.core.domain.event.ProductReservedEvent
import com.github.reomor.orderservice.core.domain.event.OrderCreatedEvent
import org.axonframework.commandhandling.gateway.CommandGateway
import org.axonframework.modelling.saga.EndSaga
import org.axonframework.modelling.saga.SagaEventHandler
import org.axonframework.modelling.saga.StartSaga
import org.axonframework.spring.stereotype.Saga
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired

const val ORDER_ID_ASSOCIATION = "orderId"

@Saga
class OrderSaga {

  @Autowired
  @Transient
  private lateinit var commandGateway: CommandGateway

  @StartSaga
  @SagaEventHandler(associationProperty = ORDER_ID_ASSOCIATION)
  fun handle(event: OrderCreatedEvent) {

    log.info("Handle OrderCreatedEvent: {}", event)

    val reserveProductCommand = ReserveProductCommand(
      productId = event.productId,
      quantity = event.quantity,
      orderId = event.orderId,
      userId = event.userId
    )

    commandGateway.send<ReserveProductCommand, Any>(
      reserveProductCommand,
      { _, commandResultMessage ->
        if (commandResultMessage.isExceptional) {
          // commandMessage
          // start compensating transaction
          log.error("Error: {}", commandResultMessage.exceptionResult().message)
        }
      })
  }

  @EndSaga
  @SagaEventHandler(associationProperty = ORDER_ID_ASSOCIATION)
  fun handle(event: ProductReservedEvent) {
    // process user payment
    log.info("Handle ProductReservedEvent: {}", event)
  }

  companion object {
    private val log: Logger = LoggerFactory.getLogger(OrderSaga::class.java)
  }
}
