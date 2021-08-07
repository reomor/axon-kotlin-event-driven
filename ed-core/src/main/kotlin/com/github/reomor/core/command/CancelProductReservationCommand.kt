package com.github.reomor.core.command

import org.axonframework.modelling.command.TargetAggregateIdentifier

data class CancelProductReservationCommand(
  @TargetAggregateIdentifier
  val productId: String,
  val quantity: Long,
  val orderId: String,
  val userId: String,
  val reason: String
)
