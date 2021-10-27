package com.kt.bank.extract.controller

import com.kt.bank.extract.client.AccountClient
import com.kt.bank.extract.service.ExtractService
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import reactor.core.publisher.Flux
import java.time.Duration

@RestController
@RequestMapping("/extract")
class ExtractRestcontroller(val accountClient : AccountClient, val extractService : ExtractService ){


    @GetMapping
    fun extract(userId :String ) : Flux<String>{
        val user = accountClient.findByUserId(userId)
        var interval : Long = 1L
        var errorOccured : Boolean = false
        if(errorOccured){
            interval = 5000L
            //TODO map the error
        }
        return Flux.interval(Duration.ofSeconds(interval)).map {extractService.convert(user)  }

    }
}