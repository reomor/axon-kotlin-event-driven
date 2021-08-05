package com.github.reomor.productservice.core.jpa.repository

import com.github.reomor.productservice.core.ProductId
import com.github.reomor.productservice.core.jpa.entity.ProductEntity
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface ProductRepository : JpaRepository<ProductEntity, ProductId> {
  fun findByProductId(productId: ProductId): ProductEntity?
}
