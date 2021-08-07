package com.github.reomor.orderservice.core.jpa.repository

import com.github.reomor.core.OrderId
import com.github.reomor.orderservice.core.jpa.entity.OrderEntity
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface OrderRepository : JpaRepository<OrderEntity, OrderId>
