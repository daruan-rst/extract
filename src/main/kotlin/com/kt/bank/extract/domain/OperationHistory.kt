package com.kt.bank.extract.domain

import java.math.BigDecimal
import java.time.LocalDateTime
import javax.persistence.Entity
import javax.persistence.Id
import javax.persistence.Table

@Entity
@Table
class OperationHistory {
    @Id
    var operationId: String = ""
    var accountId: String = ""
    var money: BigDecimal = BigDecimal.ZERO
    var operationType: OperationType = OperationType.BLANK
    var operationStatus: OperationStatus = OperationStatus.BLANK
    var date: LocalDateTime = LocalDateTime.now()
    var message: String = ""
    var confirmationDate: LocalDateTime = LocalDateTime.now()

}