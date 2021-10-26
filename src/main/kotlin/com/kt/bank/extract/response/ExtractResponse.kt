package com.kt.bank.extract.response


import com.kt.bank.extract.domain.Extract
import java.math.BigDecimal


data class ExtractResponse(var accountId : String, var balance : BigDecimal) {


    fun ExtractResponse(extract : Extract){
        this.accountId = extract.accountId
        this.balance = extract.money
    }


}