package com.kt.bank.extract.domain

import java.math.BigDecimal
import java.time.LocalDateTime
import javax.persistence.*

@Entity
@Table
class Operation {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    var id: Int = 0
    var accountId: String = ""
    var money: BigDecimal = BigDecimal.ZERO
    var operationType: OperationType = OperationType.BLANK
    var operationStatus: OperationStatus = OperationStatus.BLANK
    var date: LocalDateTime = LocalDateTime.now()
    var message: String = ""
}