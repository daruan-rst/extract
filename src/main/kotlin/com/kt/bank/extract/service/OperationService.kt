package com.kt.bank.extract.service

import com.kt.bank.extract.client.AccountClient
import com.kt.bank.extract.domain.Extract
import com.kt.bank.extract.domain.Operation
import com.kt.bank.extract.domain.OperationStatus
import com.kt.bank.extract.domain.OperationType
import com.kt.bank.extract.exception.InvalidAccountException
import com.kt.bank.extract.repository.ExtractRepository
import com.kt.bank.extract.repository.OperationRepository
import feign.FeignException
import org.springframework.stereotype.Service
import java.math.BigDecimal
import java.time.LocalDateTime

@Service
class OperationService(private val operationRepository: OperationRepository,
                       private val extractRepository: ExtractRepository,
                       private val accountClient : AccountClient
) {

    private fun newOperation(accountId: String, operationType: OperationType, operationMoney: BigDecimal): Operation {
        val operation = Operation(money = operationMoney)
        try {
            //TODO verify connection, add a circuit breaker or something that would actually break this sh!t
            accountClient.findById(accountId)
            var extract : Extract = extractRepository.findById(accountId).get()
            extract.copy(money = operationMoney.add(extract.money))
            extractRepository.save(extract)
            return operation.copy(accountId = accountId, operationType = operationType, operationStatus = OperationStatus.OK)
        } catch (e: FeignException) {
            return operation.copy(operationStatus = OperationStatus.ERROR, operationType = OperationType.BLANK)

        }

    }

    fun deposit(accountId: String, depositMoney: BigDecimal): Operation {
        val deposit = newOperation(accountId, OperationType.DEPOSIT, depositMoney)
        operationRepository.save(deposit)
        return deposit
    }

    fun withdraw(accountId: String, withdrawMoney: BigDecimal): Operation {
        var withdrawMoney = withdrawMoney.negate()
        val withdraw = newOperation(accountId, OperationType.WITHDRAW, withdrawMoney)
        operationRepository.save(withdraw)
        return withdraw
    }
}