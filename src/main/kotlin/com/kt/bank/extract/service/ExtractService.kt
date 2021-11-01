package com.kt.bank.extract.service

import com.kt.bank.extract.client.Account
import com.kt.bank.extract.client.AccountClient
import com.kt.bank.extract.domain.Extract
import com.kt.bank.extract.domain.OperationHistory
import com.kt.bank.extract.exception.DuplicateAccountIdException
import com.kt.bank.extract.exception.InvalidAccountException
import com.kt.bank.extract.repository.ExtractRepository
import com.kt.bank.extract.repository.OperationHistoryRepository
import feign.FeignException
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Service
import java.math.BigDecimal

@Service
class ExtractService(val extractRepository : ExtractRepository, val accountClient : AccountClient, val operationHistoryRepository: OperationHistoryRepository) {

    fun findExtractById(accountId: String) : ResponseEntity<Extract> {
        val extract = extractRepository.findById(accountId).orElse(null)
        if(extract == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null)
        }
        return ResponseEntity.ok().body(extract)
    }


    fun convert(account: Extract): String {
        var box = "╔"
        var message = ""
        message = if (account.accountId.equals("INVALID")){
            """
                            ║          ERROR        ║
                            ╚"""
        }else {
            """
                            ║O saldo da sua conta ${account.accountId} é de R${"$"}${account.money}║
                            ╚"""
        }
        val messageLength = message.length
        for (i in 0..messageLength - 5) {
            val component = if (i != messageLength - 5) "═" else "╗"
            box = box + component
        }
        box = box + message
        for (i in 0..messageLength - 5) {
            val component = if (i != messageLength - 5) "═" else "╝"
            box = box + component
        }
        return box }

        fun newExtract (accountId: String , money:BigDecimal): ResponseEntity<Extract> {
            val extract = Extract(accountId, money)
            if(extractRepository.findById(accountId).isPresent){
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body(null)
                }
            extractRepository.save(extract)
            return ResponseEntity.status(HttpStatus.CREATED).body(extract)
            }

    fun findExtractHistoryByAccountId(accountId: String): ResponseEntity<List<OperationHistory>> {
        var operationHistoryList = operationHistoryRepository.findByAccountId(accountId)
        if (operationHistoryList.isEmpty() ){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null)
        }
        return ResponseEntity.status(HttpStatus.OK).body(operationHistoryList)
    }
}

