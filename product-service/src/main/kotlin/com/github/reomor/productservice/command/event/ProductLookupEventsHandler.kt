package com.github.reomor.productservice.command.event

import com.github.reomor.productservice.core.event.PRODUCT_EVENTS_GROUP
import com.github.reomor.productservice.core.event.domain.ProductCreatedEvent
import com.github.reomor.productservice.core.jpa.entity.ProductLookupEntity
import com.github.reomor.productservice.core.jpa.repository.ProductLookupRepository
import org.axonframework.config.ProcessingGroup
import org.axonframework.eventhandling.EventHandler
import org.springframework.stereotype.Component

@Component
@ProcessingGroup(PRODUCT_EVENTS_GROUP)
class ProductLookupEventsHandler(
  private val productLookupRepository: ProductLookupRepository
) {

  @EventHandler
  fun on(event: ProductCreatedEvent) {
    productLookupRepository.save(
      ProductLookupEntity(
        productId = event.productId,
        name = event.name
      )
    )
  }
}