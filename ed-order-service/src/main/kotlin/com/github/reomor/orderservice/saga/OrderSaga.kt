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
import com.github.reomor.orderservice.core.domain.event.OrderApprovedEvent
import com.github.reomor.orderservice.core.domain.event.OrderCreatedEvent
import com.github.reomor.orderservice.core.domain.event.OrderRejectedEvent
import org.axonframework.commandhandling.gateway.CommandGateway
import org.axonframework.deadline.DeadlineManager
import org.axonframework.deadline.annotation.DeadlineHandler
import org.axonframework.messaging.responsetypes.ResponseTypes
import org.axonframework.modelling.saga.EndSaga
import org.axonframework.modelling.saga.SagaEventHandler
import org.axonframework.modelling.saga.StartSaga
import org.axonframework.queryhandling.QueryGateway
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
      cancelProductReservationCommand(event, "Error or null user")
      return
    }

    log.info("Get User: {}", user)

    // define deadline instead of wait in sendAndWait
    scheduleId = deadlineManager.schedule(
      Duration.ofMinutes(120),
      PAYMENT_PROCESSING_DEADLINE,
      event
    )

//    to cause deadline
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
      log.error("Error: {}", e.message)
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
