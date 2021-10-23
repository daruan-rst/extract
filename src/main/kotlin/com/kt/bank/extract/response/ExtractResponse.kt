package com.kt.bank.extract.response


import com.kt.bank.extract.domain.Extract
import java.math.BigDecimal


data class ExtractResponse(var userId : String , var money : BigDecimal) {


    fun ExtractResponse(extract : Extract){
        this.userId = extract.userId
        this.money = extract.money
    }


}