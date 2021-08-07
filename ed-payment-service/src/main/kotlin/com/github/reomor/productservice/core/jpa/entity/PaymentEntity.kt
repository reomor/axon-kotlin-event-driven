package com.github.reomor.productservice.core.jpa.entity

import javax.persistence.Entity
import javax.persistence.Id
import javax.persistence.Table

@Entity
@Table(name = "payments")
data class PaymentEntity(
  @Id
  val paymentId: String,
  val orderId: String
)
