package com.kt.bank.extract.controller

import com.kt.bank.extract.client.AccountClient
import com.kt.bank.extract.domain.Extract
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
    fun extract(accountId :String ) : Flux<String>{
        val user = extractService.findExtractById(accountId)
        var interval : Long = 1L
        var errorOccured : Boolean = false
        if(errorOccured){
            interval = 5000L
            //TODO map the error
        }
        return Flux.interval(Duration.ofSeconds(interval)).map {extractService.convert(user)  }
    }

    @PostMapping("/new-extract")
    fun createExtract(accountId: String , money : BigDecimal) : ResponseEntity<Extract>{
        return ResponseEntity.ok(extractService.newExtract(accountId, money))}

}


data class Person (val name:String, val age: Int)

val person: Person = Person("desiree", 54)

val person2: Person = Person("teste", 20)

fun main(){
    println("$person /n $person2")
}