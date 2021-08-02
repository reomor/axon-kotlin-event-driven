package com.github.reomor.productservice.core.event

import com.github.reomor.productservice.core.event.domain.ProductCreatedEvent
import com.github.reomor.productservice.core.jpa.entity.ProductEntity
import com.github.reomor.productservice.core.jpa.repository.ProductRepository
import org.axonframework.config.ProcessingGroup
import org.axonframework.eventhandling.EventHandler
import org.springframework.stereotype.Component

const val PRODUCT_EVENTS_GROUP = "product-group"

@Component
@ProcessingGroup(PRODUCT_EVENTS_GROUP) // to process the same event once for this group also rollback for all
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