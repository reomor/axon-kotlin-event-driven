package com.github.reomor.orderservice.core.jpa.entity

import com.github.reomor.orderservice.core.*
import java.io.Serializable
import javax.persistence.*

@Entity
@Table
data class OrderEntity(
  @Id
  @Column(unique = true)
  val orderId: String,
  val productId: String,
  val userId: String,
  val quantity: Long,
  val addressId: String,
  @Enumerated(EnumType.STRING)
  val orderStatus: OrderStatus
) : Serializable
