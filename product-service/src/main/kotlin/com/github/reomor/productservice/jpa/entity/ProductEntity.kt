package com.github.reomor.productservice.jpa.entity

import com.github.reomor.productservice.events.command.ProductId
import java.io.Serializable
import java.math.BigDecimal
import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.Id
import javax.persistence.Table

@Entity
@Table()
class ProductEntity(
  @Id
  @Column(unique = true)
  val productId: ProductId,
  val name: String,
  val price: BigDecimal,
  val quantity: Int
) : Serializable
