package com.kt.bank.extract.service

import com.kt.bank.extract.client.Account
import com.kt.bank.extract.client.AccountClient
import com.kt.bank.extract.domain.Extract
import com.kt.bank.extract.exception.DuplicateAccountIdException
import com.kt.bank.extract.exception.InvalidAccountException
import com.kt.bank.extract.repository.ExtractRepository
import feign.FeignException
import org.springframework.stereotype.Service
import java.math.BigDecimal

@Service
class ExtractService(val extractRepository : ExtractRepository, val accountClient : AccountClient) {

    fun findExtractById(accountId: String) : Extract {
        var extract = Extract()
        try {
            accountClient.findById(accountId) // this is where some shit could go wrong
            return extractRepository.findById(accountId).get()
        } catch (e: FeignException){
            return extract.copy(accountId = "INVALID", money = BigDecimal.ZERO)
        }
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

        public fun newExtract (accountId: String , money:BigDecimal):Extract {
            val extract = Extract(accountId, money)
            if(extractRepository.findById(accountId).isPresent){
                throw DuplicateAccountIdException()
                }
            extractRepository.save(extract)
            return extract
            }

        }

