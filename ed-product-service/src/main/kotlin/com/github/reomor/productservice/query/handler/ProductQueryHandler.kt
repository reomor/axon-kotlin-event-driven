package com.github.reomor.productservice.query.handler

import com.github.reomor.productservice.core.jpa.repository.ProductRepository
import com.github.reomor.productservice.query.FindProductQuery
import com.github.reomor.productservice.query.rest.dto.ProductDto
import org.axonframework.queryhandling.QueryHandler
import org.springframework.stereotype.Component

@Component
class ProductQueryHandler(
  private val productRepository: ProductRepository
) {

  @QueryHandler
  fun findProducts(query: FindProductQuery): List<ProductDto> =
    productRepository.findAll()
      .map { product ->
        ProductDto(
          product.productId,
          product.name,
          product.price,
          product.quantity
        )
      }
}