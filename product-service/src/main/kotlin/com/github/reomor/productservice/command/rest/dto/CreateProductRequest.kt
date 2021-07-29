package com.github.reomor.productservice.command.rest.dto

import java.math.BigDecimal
import javax.validation.constraints.Max
import javax.validation.constraints.Min
import javax.validation.constraints.NotBlank

data class CreateProductRequest(
  @get:NotBlank(message = "The product name is mandatory")
  val name: String,
  @get:Min(value = 1, message = "The price cannot be less than 1")
  val price: BigDecimal,
  @get:Min(value = 1, message = "The quantity cannot be less than 1")
  @get:Max(value = 5, message = "The quantity cannot be more than 5")
  val quantity: Int
)
