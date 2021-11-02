package com.kt.bank.extract.controller

import com.kt.bank.extract.domain.Extract
import com.kt.bank.extract.domain.Operation
import com.kt.bank.extract.service.OperationService
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.util.UriComponentsBuilder
import java.math.BigDecimal

@RestController("/operation")
class OperationRestController (private val operationService: OperationService) {

    @PostMapping("/deposit")
    fun deposit(@RequestBody extract: Extract): ResponseEntity<Operation> {
        return operationService.deposit(extract)
    }

    @PostMapping("/withdraw")
    fun withdraw(@RequestBody extract: Extract): ResponseEntity<Operation> {
        return operationService.withdraw(extract)
    }

    @PostMapping("/confirmTransaction/{operationId}")
    fun confirmTransaction(@PathVariable ("operationId") operationId: String): ResponseEntity<String>{
        return operationService.confirmTransaction(operationId)
    }
}