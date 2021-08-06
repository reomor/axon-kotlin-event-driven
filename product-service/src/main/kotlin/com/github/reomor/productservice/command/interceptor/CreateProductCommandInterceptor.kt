package com.github.reomor.productservice.command.interceptor

import com.github.reomor.productservice.command.event.command.CreateProductCommand
import com.github.reomor.productservice.core.jpa.repository.ProductLookupRepository
import org.axonframework.commandhandling.CommandMessage
import org.axonframework.messaging.MessageDispatchInterceptor
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Component
import java.util.function.BiFunction

@Component
class CreateProductCommandInterceptor(
  private val productLookupRepository: ProductLookupRepository
) : MessageDispatchInterceptor<CommandMessage<*>> {

  private val logger = LoggerFactory.getLogger(javaClass)

  override fun handle(messages: MutableList<out CommandMessage<*>>?): BiFunction<Int, CommandMessage<*>, CommandMessage<*>> {
    return BiFunction { _, command ->

      logger.info("Intercepted command: {}", command.payload)

      if (CreateProductCommand::class.java == command.payloadType) {
        val createProductCommand = command.payload
        if (createProductCommand is CreateProductCommand) {

          val lookupEntity = productLookupRepository.findByProductIdOrName(
            createProductCommand.productId.asString(),
            createProductCommand.name
          )

          if (lookupEntity != null) {
            throw IllegalStateException(
              "The product with id ${lookupEntity.productId} and name ${lookupEntity.name} already exists"
            )
          }
        }
      }

      return@BiFunction command
    }
  }
}