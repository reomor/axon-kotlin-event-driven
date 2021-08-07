package com.github.reomor.core.domain

import com.github.reomor.core.UserId

data class User(
  val firstName: String,
  val lastName: String,
  val userId: UserId,
  val paymentDetails: PaymentDetails
)
