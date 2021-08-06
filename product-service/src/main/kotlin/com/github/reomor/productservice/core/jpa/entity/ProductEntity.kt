package com.github.reomor.productservice.core.jpa.entity

import java.io.Serializable
import java.math.BigDecimal
import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.Id
import javax.persistence.Table

@Entity
@Table
data class ProductEntity(
  @Id
  @Column(unique = true)
  val productId: String,
  @Column(unique = true)
  val name: String,
  val price: BigDecimal,
  val quantity: Long
) : Serializable
