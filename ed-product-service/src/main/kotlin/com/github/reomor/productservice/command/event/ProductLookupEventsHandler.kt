package com.github.reomor.productservice.command.event

import com.github.reomor.productservice.PRODUCT_EVENTS_GROUP
import com.github.reomor.productservice.core.domain.event.ProductCreatedEvent
import com.github.reomor.productservice.core.jpa.entity.ProductLookupEntity
import com.github.reomor.productservice.core.jpa.repository.ProductLookupRepository
import org.axonframework.config.ProcessingGroup
import org.axonframework.eventhandling.EventHandler
import org.axonframework.eventhandling.ResetHandler
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
        productId = event.productId.asString(),
        name = event.name
      )
    )
  }

  @ResetHandler
  fun reset() {
    // empty product table to replay events
    productLookupRepository.deleteAll()
  }
}
