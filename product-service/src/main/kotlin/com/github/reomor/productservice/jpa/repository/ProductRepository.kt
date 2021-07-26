package com.github.reomor.productservice.jpa.repository

import com.github.reomor.productservice.events.command.ProductId
import com.github.reomor.productservice.jpa.entity.ProductEntity
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface ProductRepository : JpaRepository<ProductEntity, ProductId> {
  fun findByProductId(productId: ProductId)
}
