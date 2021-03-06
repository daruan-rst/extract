package com.kt.bank.extract.client

import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestMethod
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.cloud.openfeign.FeignClient


@FeignClient(value="users", url="http://localhost:8080") // TODO colocar a url final do sistema de usuários
interface AccountClient {

    @RequestMapping(value = ["/account/find-by-id/"], method = [RequestMethod.GET])
    fun findById(@RequestParam accountId: String) : Account

    //TODO o método acima é um placeholder -> deve ser substituido pelo método findById que for criado no microsserviço de criar contas
}