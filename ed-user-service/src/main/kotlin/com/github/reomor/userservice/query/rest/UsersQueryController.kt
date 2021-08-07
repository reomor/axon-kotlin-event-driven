package com.github.reomor.userservice.query.rest

import com.github.reomor.core.UserId
import com.github.reomor.core.domain.User
import com.github.reomor.core.query.FetchUserPaymentDetailsQuery
import org.axonframework.messaging.responsetypes.ResponseTypes
import org.axonframework.queryhandling.QueryGateway
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/v1/users")
class UsersQueryController(
  private val queryGateway: QueryGateway
) {

  @GetMapping("/{userId}/payment-details")
  fun getUserPaymentDetails(@PathVariable userId: String): User {
    return queryGateway.query<User, FetchUserPaymentDetailsQuery>(
      FetchUserPaymentDetailsQuery(UserId(userId)),
      ResponseTypes.instanceOf(User::class.java)
    ).join()
  }
}
