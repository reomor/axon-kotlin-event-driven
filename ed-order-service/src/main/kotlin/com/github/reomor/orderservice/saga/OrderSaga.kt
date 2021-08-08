package com.github.reomor.orderservice.saga

import com.github.reomor.core.OrderId
import com.github.reomor.core.UserId
import com.github.reomor.core.command.CancelProductReservationCommand
import com.github.reomor.core.command.ProcessPaymentCommand
import com.github.reomor.core.command.ReserveProductCommand
import com.github.reomor.core.domain.User
import com.github.reomor.core.domain.event.PaymentProcessedEvent
import com.github.reomor.core.domain.event.ProductReservationCancelEvent
import com.github.reomor.core.domain.event.ProductReservedEvent
import com.github.reomor.core.query.FetchUserPaymentDetailsQuery
import com.github.reomor.orderservice.command.ApproveOrderCommand
import com.github.reomor.orderservice.command.RejectOrderCommand
import com.github.reomor.orderservice.core.OrderStatus
import com.github.reomor.orderservice.core.domain.event.OrderApprovedEvent
import com.github.reomor.orderservice.core.domain.event.OrderCreatedEvent
import com.github.reomor.orderservice.core.domain.event.OrderRejectedEvent
import com.github.reomor.orderservice.query.FindOrderQuery
import com.github.reomor.orderservice.query.rest.OrderInfo
import org.axonframework.commandhandling.gateway.CommandGateway
import org.axonframework.deadline.DeadlineManager
import org.axonframework.deadline.annotation.DeadlineHandler
import org.axonframework.messaging.responsetypes.ResponseTypes
import org.axonframework.modelling.saga.EndSaga
import org.axonframework.modelling.saga.SagaEventHandler
import org.axonframework.modelling.saga.StartSaga
import org.axonframework.queryhandling.QueryGateway
import org.axonframework.queryhandling.QueryUpdateEmitter
import org.axonframework.spring.stereotype.Saga
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import java.time.Duration
import java.util.*

const val ORDER_ID_ASSOCIATION = "orderId"
const val PAYMENT_PROCESSING_DEADLINE = "payment-processing-deadline"

@Saga
class OrderSaga {

  @Autowired
  @Transient
  private lateinit var commandGateway: CommandGateway

  @Autowired
  @Transient
  private lateinit var queryGateway: QueryGateway

  @Autowired
  @Transient
  private lateinit var deadlineManager: DeadlineManager

  @Autowired
  @Transient
  private lateinit var queryUpdateEmitter: QueryUpdateEmitter

  private var scheduleId: String? = null

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
      reserveProductCommand
    ) { commandMessage, commandResultMessage ->
      if (commandResultMessage.isExceptional) {
        // start compensating transaction
        log.error("Error after command ReserveProductCommand: {}", commandResultMessage.exceptionResult().message)
        commandGateway.send<String>(
          RejectOrderCommand.build {
            orderId = OrderId(commandMessage.payload.orderId)
            reason = commandResultMessage.exceptionResult().message ?: "Error after command ReserveProductCommand"
          }
        )
      }
    }
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
      log.error("Error after FetchUserPaymentDetailsQuery: {}", e.message)
      null
    }

    if (user == null) {
      cancelProductReservationCommand(event, "Error or null user")
      return
    }

    log.info("Get User: {}", user)

    // define deadline instead of wait in sendAndWait
    scheduleId = deadlineManager.schedule(
//      Duration.ofMinutes(120),
      Duration.ofSeconds(10),
      PAYMENT_PROCESSING_DEADLINE,
      event
    )

//    to cause deadline and rollback
//    if (true) return

    val paymentId = try {
      commandGateway.sendAndWait<String>(
        ProcessPaymentCommand(
          paymentId = UUID.randomUUID().toString(),
          orderId = event.orderId,
          paymentDetails = user.paymentDetails
        )
      )
    } catch (e: Exception) {
      log.error("Error after ProcessPaymentCommand: {}", e.message)
      null
    }

    if (paymentId == null) {
      cancelProductReservationCommand(event, "Error or null payment")
      return
    }
  }

  @SagaEventHandler(associationProperty = ORDER_ID_ASSOCIATION)
  fun handle(event: PaymentProcessedEvent) {

    cancelDeadline()

    log.info("Handle PaymentProcessedEvent: {}", event)

    commandGateway.send<String>(
      ApproveOrderCommand.build {
        orderId = OrderId(event.orderId)
      }
    )
  }

  @EndSaga
  @SagaEventHandler(associationProperty = ORDER_ID_ASSOCIATION)
  fun handle(event: OrderApprovedEvent) {
    log.info("Order is approved: {}", event.orderId)
    // emit response for subscription query on success
    queryUpdateEmitter.emit(
      FindOrderQuery::class.java,
      { true },
      OrderInfo(
        orderId = event.orderId,
        orderStatus = event.orderStatus
      )
    )
//    alternative way of @EndSaga
//    SagaLifecycle.end()
  }

  @SagaEventHandler(associationProperty = ORDER_ID_ASSOCIATION)
  fun handle(event: ProductReservationCancelEvent) {

    log.info("Handle ProductReservationCancelEvent: {}", event)

    commandGateway.send<String>(
      RejectOrderCommand.build {
        orderId = OrderId(event.orderId)
        reason = event.reason
      }
    )
  }

  @EndSaga
  @SagaEventHandler(associationProperty = ORDER_ID_ASSOCIATION)
  fun handle(event: OrderRejectedEvent) {
    log.info("Order is rejected: {}", event.orderId)
    // emit response for subscription query on cancel
    queryUpdateEmitter.emit(
      FindOrderQuery::class.java,
      { true },
      OrderInfo(
        orderId = event.orderId,
        orderStatus = event.orderStatus,
        message = event.reason
      )
    )
  }

  @DeadlineHandler(deadlineName = PAYMENT_PROCESSING_DEADLINE)
  fun handlePaymentDeadline(event: ProductReservedEvent) {

    log.info("Payment processing deadline took place. Sending compensating ...")

    cancelProductReservationCommand(event, "Deadline timeout")
  }

  /**
   * Compensating transaction for [ProductReservedEvent]
   */
  private fun cancelProductReservationCommand(event: ProductReservedEvent, reason: String) {

    cancelDeadline()

    commandGateway.send<String>(
      CancelProductReservationCommand(
        productId = event.productId,
        quantity = event.quantity,
        orderId = event.orderId,
        userId = event.userId,
        reason = reason
      )
    )
  }

  /**
   * Cancel planned deadline to execute
   * Not need to wait because of success or already running compensating transaction
   */
  private fun cancelDeadline() {
//    deadlineManager.cancelAll(PAYMENT_PROCESSING_DEADLINE)
    if (scheduleId != null) {
      deadlineManager.cancelSchedule(PAYMENT_PROCESSING_DEADLINE, scheduleId)
      scheduleId = null
    }
  }

  companion object {
    private val log: Logger = LoggerFactory.getLogger(OrderSaga::class.java)
  }
}
