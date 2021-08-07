package com.github.reomor.core

@JvmInline
value class UserId(private val id: String) {

  companion object {
    val NONE = UserId("")
  }

  fun asString() = id
}

@JvmInline
value class OrderId(private val id: String) {

  companion object {
    val NONE = OrderId("")
  }

  fun asString() = id
}
