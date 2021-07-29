package com.github.reomor.productservice.command.interceptor

import com.github.reomor.productservice.command.event.command.CreateProductCommand
import org.axonframework.commandhandling.CommandMessage
import org.axonframework.messaging.MessageDispatchInterceptor
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Component
import java.math.BigDecimal
import java.util.function.BiFunction

@Component
class CreateProductCommandInterceptor : MessageDispatchInterceptor<CommandMessage<*>> {

  private val logger = LoggerFactory.getLogger(javaClass)

  override fun handle(messages: MutableList<out CommandMessage<*>>?): BiFunction<Int, CommandMessage<*>, CommandMessage<*>> {
    return BiFunction { index, command ->

      logger.info("Intercepted command: {}", command.payload)

      if (CreateProductCommand::class.java == command.payloadType) {
        val createProductCommand = command.payload
        if (createProductCommand is CreateProductCommand) {
          if (createProductCommand.price <= BigDecimal.ZERO) throw IllegalArgumentException("The price cannot be less than 0")
          if (createProductCommand.name.isBlank()) throw IllegalArgumentException("The name cannot be blank")
        }
      }

      return@BiFunction command
    }
  }
}