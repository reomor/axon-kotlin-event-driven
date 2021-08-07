package com.github.reomor.productservice.core.event

import com.github.reomor.core.domain.event.ProductReservationCancelEvent
import com.github.reomor.core.domain.event.ProductReservedEvent
import com.github.reomor.productservice.core.domain.event.ProductCreatedEvent
import com.github.reomor.productservice.core.jpa.entity.ProductEntity
import com.github.reomor.productservice.core.jpa.repository.ProductRepository
import org.axonframework.config.ProcessingGroup
import org.axonframework.eventhandling.EventHandler
import org.axonframework.messaging.interceptors.ExceptionHandler
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Component

const val PRODUCT_EVENTS_GROUP = "product-group"

@Component
@ProcessingGroup(PRODUCT_EVENTS_GROUP) // to process the same event once for this group also rollback for all
class ProductEventsHandler(
  private val productRepository: ProductRepository
) {

  //  catches exceptions in @EventHandler and only in this class
  // `subscribing` nature of processor allows us to handle exceptions in one transaction
  @ExceptionHandler(resultType = Exception::class)
  fun handle(e: Exception) {
    throw e
  }

  @ExceptionHandler(resultType = IllegalArgumentException::class)
  fun handle(e: IllegalArgumentException) {
    throw e
  }

  @EventHandler
  fun on(event: ProductCreatedEvent) {
    productRepository.save(
      ProductEntity(
        productId = event.productId.asString(),
        name = event.name,
        price = event.price,
        quantity = event.quantity
      )
    )
  }

  @EventHandler
  fun on(event: ProductReservedEvent) {

    log.info("Handle ProductReservedEvent: {}", event)

    val productEntity = productRepository.findByProductId(event.productId)
    if (productEntity != null) {
      productRepository.save(
        productEntity.copy(quantity = productEntity.quantity - event.quantity)
      )
    } else {
      throw IllegalArgumentException("Product(id=${event.productId} is not found")
    }
  }

  @EventHandler
  fun on(event: ProductReservationCancelEvent) {

    log.info("Handle ProductReservationCancelEvent: {}", event)

    val productEntity = productRepository.findByProductId(event.productId)
    if (productEntity != null) {
      productRepository.save(
        productEntity.copy(quantity = productEntity.quantity + event.quantity)
      )
    } else{
      throw IllegalArgumentException("Product(id=${event.productId} is not found")
    }
  }

  companion object {
    private val log: Logger = LoggerFactory.getLogger(ProductEventsHandler::class.java)
  }
}
