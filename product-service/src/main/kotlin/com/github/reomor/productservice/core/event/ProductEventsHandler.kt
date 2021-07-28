package com.github.reomor.productservice.core.event

import com.github.reomor.productservice.core.event.domain.ProductCreatedEvent
import com.github.reomor.productservice.core.jpa.entity.ProductEntity
import com.github.reomor.productservice.core.jpa.repository.ProductRepository
import org.axonframework.eventhandling.EventHandler
import org.springframework.stereotype.Component

@Component
class ProductEventsHandler(
  private val productRepository: ProductRepository
) {

  @EventHandler
  fun on(event: ProductCreatedEvent) {
    productRepository.save(
      ProductEntity(
        event.id,
        event.name,
        event.price,
        event.quantity
      )
    )
  }
}