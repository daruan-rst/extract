package com.kt.bank.extract.controller

import com.kt.bank.extract.client.AccountClient
import com.kt.bank.extract.domain.Extract
import com.kt.bank.extract.domain.OperationHistory
import com.kt.bank.extract.dto.AccountIdDto
import com.kt.bank.extract.service.ExtractService
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import reactor.core.publisher.Flux
import java.math.BigDecimal
import java.time.Duration

@RestController
@RequestMapping("/extract")
class ExtractRestcontroller(val accountClient : AccountClient, val extractService : ExtractService ){


    @GetMapping
    fun extract(@RequestBody accountIdDto: AccountIdDto ) : ResponseEntity<Extract> = run {
         extractService.findExtractById(accountIdDto.accountId)
    }

    @PostMapping("/new-extract")
    fun createExtract(@RequestBody extract: Extract) : ResponseEntity<Extract>{
        return extractService.newExtract(extract)}

    @GetMapping("/extractHistory")
    fun extractHistory(@RequestBody accountIdDto: AccountIdDto) :ResponseEntity<List<OperationHistory>>{
        return extractService.findExtractHistoryByAccountId(accountIdDto.accountId)
    }
}