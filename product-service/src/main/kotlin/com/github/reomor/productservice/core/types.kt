package com.github.reomor.productservice.core

@JvmInline
value class ProductId(private val id: String) {

  companion object {
    val NONE = ProductId("")
  }

  fun asString() = id
}
