package com.github.reomor.core.domain

data class User(
  val firstName: String,
  val lastName: String,
  val userId: String,
  val paymentDetails: PaymentDetails
)
