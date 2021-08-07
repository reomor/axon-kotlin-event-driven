package com.github.reomor.productservice.command.aggregate

import com.github.reomor.core.command.ProcessPaymentCommand
import com.github.reomor.core.domain.event.PaymentProcessedEvent
import org.axonframework.commandhandling.CommandHandler
import org.axonframework.eventsourcing.EventSourcingHandler
import org.axonframework.modelling.command.AggregateIdentifier
import org.axonframework.modelling.command.AggregateLifecycle
import org.axonframework.spring.stereotype.Aggregate
import java.time.LocalDate

@Aggregate
class PaymentAggregate {

  @AggregateIdentifier
  private lateinit var paymentId: String
  private lateinit var orderId: String

  @CommandHandler
  constructor(command: ProcessPaymentCommand) {

    if (isPaymentNotValid(command)) {
      throw IllegalArgumentException("Error during payment process: $command")
    }

    AggregateLifecycle.apply(
      PaymentProcessedEvent(
        paymentId = command.paymentId,
        orderId = command.orderId
      )
    )
  }

  @EventSourcingHandler
  fun on(event: PaymentProcessedEvent) {
    paymentId = event.paymentId
    orderId = event.orderId
  }

  companion object {
    fun isPaymentNotValid(command: ProcessPaymentCommand): Boolean {
      return command.paymentId.isBlank()
        || command.orderId.isBlank()
        || command.paymentDetails.validUntilYear < LocalDate.now().year
    }
  }
}
