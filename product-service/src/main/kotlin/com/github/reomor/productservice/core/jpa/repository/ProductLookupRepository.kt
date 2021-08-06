package com.github.reomor.productservice.core.jpa.repository

import com.github.reomor.productservice.core.jpa.entity.ProductLookupEntity
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface ProductLookupRepository : JpaRepository<ProductLookupEntity, String> {
  fun findByProductIdOrName(productId: String, name: String): ProductLookupEntity?
}
