package com.github.reomor.orderservice.saga

import com.github.reomor.core.UserId
import com.github.reomor.core.command.ProcessPaymentCommand
import com.github.reomor.core.command.ReserveProductCommand
import com.github.reomor.core.domain.User
import com.github.reomor.core.domain.event.PaymentProcessedEvent
import com.github.reomor.core.domain.event.ProductReservedEvent
import com.github.reomor.core.query.FetchUserPaymentDetailsQuery
import com.github.reomor.orderservice.core.domain.event.OrderCreatedEvent
import org.axonframework.commandhandling.gateway.CommandGateway
import org.axonframework.messaging.responsetypes.ResponseTypes
import org.axonframework.modelling.saga.EndSaga
import org.axonframework.modelling.saga.SagaEventHandler
import org.axonframework.modelling.saga.StartSaga
import org.axonframework.queryhandling.QueryGateway
import org.axonframework.spring.stereotype.Saga
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import java.util.*
import java.util.concurrent.TimeUnit

const val ORDER_ID_ASSOCIATION = "orderId"

@Saga
class OrderSaga {

  @Autowired
  @Transient
  private lateinit var commandGateway: CommandGateway

  @Autowired
  @Transient
  private lateinit var queryGateway: QueryGateway

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
          // todo
          // commandMessage
          // start compensating transaction
          log.error("Error: {}", commandResultMessage.exceptionResult().message)
        }
      })
  }

  @SagaEventHandler(associationProperty = ORDER_ID_ASSOCIATION)
  fun handle(event: ProductReservedEvent) {

    log.info("Handle ProductReservedEvent: {}", event)

    val user: User? = try {
        queryGateway.query(
          FetchUserPaymentDetailsQuery(UserId(event.userId)),
          ResponseTypes.instanceOf(User::class.java)
        ).join()
    } catch (e: Exception) {
      log.error("Error: {}", e.message)
      null
    }

    if (user == null) {
      // todo
      // start compensating transaction
      return
    }

    log.info("Get User: {}", user)

    val paymentId = try {
      commandGateway.sendAndWait<String>(
        ProcessPaymentCommand(
          paymentId = UUID.randomUUID().toString(),
          orderId = event.orderId,
          paymentDetails = user.paymentDetails
        ),
        10,
        TimeUnit.SECONDS
      )
    } catch (e: Exception) {
      log.error("Error: {}", e.message)
      null
    }

    if (paymentId == null) {
      // todo
      // start compensating transaction
      return
    }
  }

  @EndSaga
  @SagaEventHandler(associationProperty = ORDER_ID_ASSOCIATION)
  fun handle(event: PaymentProcessedEvent) {
    log.info("Handle PaymentProcessedEvent: {}", event)
  }

  companion object {
    private val log: Logger = LoggerFactory.getLogger(OrderSaga::class.java)
  }
}
