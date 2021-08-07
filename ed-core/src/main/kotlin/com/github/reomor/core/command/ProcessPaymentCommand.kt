package com.github.reomor.core.command

import com.github.reomor.core.domain.PaymentDetails
import org.axonframework.modelling.command.TargetAggregateIdentifier

data class ProcessPaymentCommand(
  @TargetAggregateIdentifier
  val paymentId: String,
  val orderId: String,
  val paymentDetails: PaymentDetails
)
