package com.kt.bank.extract.controller

import com.kt.bank.extract.client.AccountClient
import com.kt.bank.extract.domain.Extract
import com.kt.bank.extract.domain.OperationHistory
import com.kt.bank.extract.service.ExtractService
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import reactor.core.publisher.Flux
import java.math.BigDecimal
import java.time.Duration

@RestController
@RequestMapping("/extract")
class ExtractRestcontroller(val accountClient : AccountClient, val extractService : ExtractService ){


    @GetMapping
    fun extract(accountId :String ) : ResponseEntity<Extract> {
        return extractService.findExtractById(accountId)
    }

    @PostMapping("/new-extract")
    fun createExtract(accountId: String , money : BigDecimal) : ResponseEntity<Extract>{
        return extractService.newExtract(accountId, money)}

    @GetMapping("/extractHistory")
    fun extractHistory(accountId: String) :ResponseEntity<List<OperationHistory>>{
        return extractService.findExtractHistoryByAccountId(accountId)
    }
}