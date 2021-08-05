package com.github.reomor.productservice.core.jpa.entity

import com.github.reomor.productservice.core.ProductId
import java.io.Serializable
import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.Id
import javax.persistence.Table

@Entity
@Table(name = "PRODUCT_LOOKUP")
data class ProductLookupEntity(
  @Id
  @Column(unique = true)
  val productId: ProductId,
  @Column(unique = true)
  val name: String,
) : Serializable
