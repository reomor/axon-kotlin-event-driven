package com.github.reomor.productservice.core.error.handler

import org.axonframework.eventhandling.EventMessage
import org.axonframework.eventhandling.EventMessageHandler
import org.axonframework.eventhandling.ListenerInvocationErrorHandler
import java.lang.Exception

class ProductServiceEventErrorHandler : ListenerInvocationErrorHandler {
  // here we can determine the rollback actions
  override fun onError(
    exception: Exception?,
    event: EventMessage<*>?,
    eventHandler: EventMessageHandler?
  ) {
    if(exception != null) throw exception
  }
}