package com.kt.bank.extract.controller

import com.kt.bank.extract.domain.Operation
import com.kt.bank.extract.service.OperationService
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RestController
import java.math.BigDecimal

@RestController("/operation")
class OperationRestController (private val operationService: OperationService) {


    @PostMapping("/deposit")
    fun deposit(accountId: String, money: BigDecimal): Operation {
        return operationService.deposit(accountId, money)
    }

    @PostMapping("/withdraw")
    fun withdraw(accountId: String, money: BigDecimal): Operation {
        return operationService.withdraw(accountId, money)
    }
}