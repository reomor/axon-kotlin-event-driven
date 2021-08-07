package com.github.reomor.productservice.core.event

import com.github.reomor.core.domain.event.PaymentProcessedEvent
import com.github.reomor.productservice.core.jpa.entity.PaymentEntity
import com.github.reomor.productservice.core.jpa.repository.PaymentRepository
import org.axonframework.eventhandling.EventHandler
import org.springframework.stereotype.Component

@Component
class PaymentEventsHandler(
  private val paymentRepository: PaymentRepository
) {

  @EventHandler
  fun on(event: PaymentProcessedEvent) {
    paymentRepository.save(
      PaymentEntity(
        paymentId = event.paymentId,
        orderId = event.orderId
      )
    )
  }
}
