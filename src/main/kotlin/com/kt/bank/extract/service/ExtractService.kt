package com.kt.bank.extract.service

import com.kt.bank.extract.client.Account
import com.kt.bank.extract.domain.Extract
import com.kt.bank.extract.exception.DuplicateAccountIdException
import com.kt.bank.extract.repository.ExtractRepository
import org.springframework.stereotype.Service
import java.math.BigDecimal

@Service
class ExtractService(val extractRepository : ExtractRepository) {

    fun findExtractById(accountId: String) : Extract {
        return extractRepository.findById(accountId).get()
    }

    fun convert(account: Extract): String {

        var box = "╔"
        val message = """
                        ║O saldo da sua conta ${account.accountId} é de R${"$"}${account.money}║
                        ╚"""
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
            val extract = Extract()
            extract.accountId = accountId
            extract.money = money
            if(extractRepository.findById(accountId).isPresent){
                throw DuplicateAccountIdException()
                }
            extractRepository.save(extract)
            return extract
            }

        }

}