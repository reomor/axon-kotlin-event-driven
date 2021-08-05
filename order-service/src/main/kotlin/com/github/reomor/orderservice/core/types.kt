package com.github.reomor.orderservice.core

@JvmInline
value class OrderId(private val id: String) {

  companion object {
    val NONE = OrderId("")
  }

  fun asString() = id
}

@JvmInline
value class UserId(private val id: String) {

  companion object {
    val NONE = UserId("")
  }

  fun asString() = id
}

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
