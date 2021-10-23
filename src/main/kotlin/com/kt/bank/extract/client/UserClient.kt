package com.kt.bank.extract.client

import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestMethod
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.cloud.openfeign.FeignClient


@FeignClient(value="users", url="http://localhost:8080") // TODO colocar a url final do sistema de usuários
interface UserClient {

    @RequestMapping(value = ["/user/find-by-user-id/"], method = [RequestMethod.GET])
    fun findByUserId(@RequestParam userId: String) : User

    //TODO o método acima é um placeholder -> deve ser substituido pelo método findById que for criado no microsserviço de criar contas
}