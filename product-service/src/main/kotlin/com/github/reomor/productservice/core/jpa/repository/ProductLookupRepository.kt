package com.github.reomor.productservice.core.jpa.repository

import com.github.reomor.productservice.core.ProductId
import com.github.reomor.productservice.core.jpa.entity.ProductLookupEntity
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface ProductLookupRepository : JpaRepository<ProductLookupEntity, ProductId> {
  fun findByProductIdOrName(productId: ProductId, name: String): ProductLookupEntity?
}
