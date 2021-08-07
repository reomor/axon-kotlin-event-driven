package com.github.reomor.core.domain.event

data class PaymentProcessedEvent(
  val orderId: String,
  val paymentId: String
)
