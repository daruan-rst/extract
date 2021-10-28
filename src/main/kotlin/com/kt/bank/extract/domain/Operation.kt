package com.kt.bank.extract.domain

import java.math.BigDecimal
import java.time.LocalDateTime
import javax.persistence.*

@Entity
@Table
data class Operation(@Id @GeneratedValue(strategy = GenerationType.AUTO) val id: String ="",
                     val accountId: String = "",
                     val money: BigDecimal = BigDecimal.ZERO,
                     val operationType: OperationType = OperationType.BLANK,
                     val operationStatus: OperationStatus = OperationStatus.BLANK,
                     val date: LocalDateTime = LocalDateTime.now())