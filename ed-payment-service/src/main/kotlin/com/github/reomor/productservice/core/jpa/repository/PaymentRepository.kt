package com.github.reomor.productservice.core.jpa.repository

import com.github.reomor.productservice.core.jpa.entity.PaymentEntity
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface PaymentRepository : JpaRepository<PaymentEntity, String>
