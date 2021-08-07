package com.github.reomor.orderservice

import org.axonframework.config.Configuration
import org.axonframework.config.ConfigurationScopeAwareProvider
import org.axonframework.deadline.DeadlineManager
import org.axonframework.deadline.SimpleDeadlineManager
import org.axonframework.spring.messaging.unitofwork.SpringTransactionManager
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.context.annotation.Bean

@SpringBootApplication
class OrderServiceApplication {
  @Bean
  fun deadlineManager(
    configuration: Configuration,
    springTransactionManager: SpringTransactionManager
  ): DeadlineManager {
    return SimpleDeadlineManager.builder()
      .scopeAwareProvider(ConfigurationScopeAwareProvider(configuration))
      .transactionManager(springTransactionManager)
      .build()
  }
}

fun main(args: Array<String>) {
    runApplication<OrderServiceApplication>(*args)
}
