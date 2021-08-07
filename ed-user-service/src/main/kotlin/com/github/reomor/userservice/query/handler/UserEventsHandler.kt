package com.github.reomor.userservice.query.handler

import com.github.reomor.core.domain.PaymentDetails
import com.github.reomor.core.domain.User
import com.github.reomor.core.query.FetchUserPaymentDetailsQuery
import org.axonframework.queryhandling.QueryHandler
import org.springframework.stereotype.Component


@Component
class UserEventsHandler {

  @QueryHandler
  fun find(query: FetchUserPaymentDetailsQuery): User {
    return User(
      firstName = "Alex",
      lastName = "Alexeev",
      userId = query.userId.asString(),
      paymentDetails = PaymentDetails(
        cardNumber = "4276XXXXYYYY0000",
        cvv = "123",
        name = "ALEX ALEXEEV",
        validUntilMonth = 12,
        validUntilYear = 2023,
      )
    )
  }
}
