## Event-Driven

[Event-Driven Microservices, CQRS, SAGA, Axon, Spring Boot](https://www.udemy.com/course/spring-boot-microservices-cqrs-saga-axon-framework/)

### Stack

- Kotlin 1.5.20
- Spring Boot 2.5.2
- Axon Framework 4.5.3

### Run order for debug

- Discovery Service
- Product Service
- Api gateway

### [Axon](https://axoniq.io)

```yml
# C:\AxonServer-4.5.5\config\axonserver.yml
server:
  port: 8024
axoniq:
  axonserver:
    name: My Axon
    hostname: localhost
    devmode:
      enabled: true
```

### H2

[h2-console product-service](http://localhost:8082/product-service/h2-console)
[h2-console order-service](http://localhost:8082/order-service/h2-console)
