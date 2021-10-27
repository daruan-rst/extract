package com.kt.bank.extract.service

import com.kt.bank.extract.client.Account
import org.springframework.stereotype.Service

@Service
class ExtractService {

    fun convert(account: Account): String {

        var box = "╔"
        val message = """
                        ║O saldo da sua conta ${account.accountId} é de R${"$"}${account.balance}║
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


}