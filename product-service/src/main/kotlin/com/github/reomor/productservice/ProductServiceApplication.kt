package com.github.reomor.productservice

import com.github.reomor.productservice.command.interceptor.CreateProductCommandInterceptor
import org.axonframework.commandhandling.CommandBus
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.context.ApplicationContext

@SpringBootApplication
class ProductServiceApplication {

    @Autowired
    fun registerCreateProductCommandInterceptor(context: ApplicationContext, commandBus: CommandBus) {
        commandBus.registerDispatchInterceptor(context.getBean(CreateProductCommandInterceptor::class.java))
    }
}

fun main(args: Array<String>) {
    runApplication<ProductServiceApplication>(*args)
}
