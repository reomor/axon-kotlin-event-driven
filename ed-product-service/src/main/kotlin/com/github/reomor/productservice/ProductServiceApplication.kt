package com.github.reomor.productservice

import com.github.reomor.productservice.command.interceptor.CreateProductCommandInterceptor
import com.github.reomor.productservice.core.error.handler.ProductServiceEventErrorHandler
import com.github.reomor.productservice.core.event.PRODUCT_EVENTS_GROUP
import org.axonframework.commandhandling.CommandBus
import org.axonframework.config.EventProcessingConfigurer
import org.axonframework.eventhandling.PropagatingErrorHandler
import org.axonframework.eventsourcing.EventCountSnapshotTriggerDefinition
import org.axonframework.eventsourcing.SnapshotTriggerDefinition
import org.axonframework.eventsourcing.Snapshotter
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.cloud.client.discovery.EnableDiscoveryClient
import org.springframework.context.ApplicationContext
import org.springframework.context.annotation.Bean
import java.util.function.Function

const val PRODUCT_SNAPSHOT_TRIGGER = "productShapshotDefinitionTrigger"

@EnableDiscoveryClient
@SpringBootApplication
class ProductServiceApplication {

    @Autowired
    fun registerCreateProductCommandInterceptor(context: ApplicationContext, commandBus: CommandBus) {
        commandBus.registerDispatchInterceptor(context.getBean(CreateProductCommandInterceptor::class.java))
    }

    @Autowired
    fun configure(configurer: EventProcessingConfigurer) {
        configurer.registerListenerInvocationErrorHandler(
            PRODUCT_EVENTS_GROUP
        ) {
            ProductServiceEventErrorHandler()
            //PropagatingErrorHandler.INSTANCE
        }
    }

  // make a snapshot every 3 events
  @Bean(name = [PRODUCT_SNAPSHOT_TRIGGER])
  fun snapshotTriggerDefinition(snapshotter: Snapshotter): SnapshotTriggerDefinition {
    return EventCountSnapshotTriggerDefinition(snapshotter, 3)
  }
}

fun main(args: Array<String>) {
    runApplication<ProductServiceApplication>(*args)
}
