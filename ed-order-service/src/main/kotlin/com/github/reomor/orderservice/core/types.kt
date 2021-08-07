package com.github.reomor.orderservice.core

@JvmInline
value class ProductId(private val id: String) {

  companion object {
    val NONE = ProductId("")
  }

  fun asString() = id
}

@JvmInline
value class AddressId(private val id: String) {

  companion object {
    val NONE = AddressId("")
  }

  fun asString() = id
}

enum class OrderStatus {
  CREATED,
  APPROVED,
  REJECTED
}
