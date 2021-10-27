package com.kt.bank.extract.domain

import java.math.BigDecimal
import javax.persistence.*

@Entity
@Table
class Operation {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    var id: String = ""
    var accountId: String = ""
    var money: BigDecimal = BigDecimal.ZERO
    var operationType: OperationType = OperationType.BLANK
    var operationStatus: OperationStatus = OperationStatus.BLANK
}