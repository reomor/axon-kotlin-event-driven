package com.github.reomor.productservice.query.rest

import com.github.reomor.productservice.query.FindProductQuery
import com.github.reomor.productservice.query.rest.dto.ProductDto
import org.axonframework.messaging.responsetypes.ResponseTypes
import org.axonframework.queryhandling.QueryGateway
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/v1/products/query")
class ProductQueryController(
  private val queryGateway: QueryGateway
) {

  @GetMapping
  fun getAll(): Collection<ProductDto> {
    return queryGateway.query(
      FindProductQuery(),
      ResponseTypes.multipleInstancesOf(ProductDto::class.java)
    ).join()
  }
}
