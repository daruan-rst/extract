package com.kt.bank.extract.controller

import com.kt.bank.extract.domain.Operation
import com.kt.bank.extract.service.OperationService
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.util.UriComponentsBuilder
import java.math.BigDecimal

@RestController("/operation")
class OperationRestController (private val operationService: OperationService) {


    @PostMapping("/deposit")
    fun deposit(accountId: String, money: BigDecimal
                , uriComponentsBuilder : UriComponentsBuilder)
    : ResponseEntity<Operation> {
        val operation = operationService.deposit(accountId, money)
        val uri = uriComponentsBuilder.path("/operation/{id}")
            .buildAndExpand(operation.id).toUri()
        return ResponseEntity.created(uri).body(operation)
    }

    @PostMapping("/withdraw")
    fun withdraw(accountId: String, money: BigDecimal,
        uriComponentsBuilder : UriComponentsBuilder)
            : ResponseEntity<Operation> {
        val operation = operationService.withdraw(accountId, money)
        val uri = uriComponentsBuilder.path("/operation/{id}")
            .buildAndExpand(operation.id).toUri()
        return ResponseEntity.created(uri).body(operation)
    }

}