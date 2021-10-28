package com.kt.bank.extract.service

import com.kt.bank.extract.client.AccountClient
import com.kt.bank.extract.domain.Extract
import com.kt.bank.extract.domain.Operation
import com.kt.bank.extract.domain.OperationStatus
import com.kt.bank.extract.domain.OperationType
import com.kt.bank.extract.exception.InvalidAccountException
import com.kt.bank.extract.repository.ExtractRepository
import com.kt.bank.extract.repository.OperationRepository
import org.springframework.stereotype.Service
import java.math.BigDecimal

@Service
class OperationService(private val operationRepository: OperationRepository,
                       private val extractRepository: ExtractRepository,
                       private val accountClient : AccountClient
) {

    private fun newOperation(accountId: String, operationType: OperationType, money: BigDecimal): Operation {
        val operation = Operation()
        operation.money = money
        try {
            //TODO verify connection, add a circuit breaker or something that would actually break this sh!t
            accountClient.findById(accountId)
            operation.accountId = accountId
            operation.operationType = operationType
            operation.operationStatus = OperationStatus.OK
        } catch (e: InvalidAccountException) {
            operation.operationStatus = OperationStatus.ERROR
            operation.operationType = OperationType.BLANK
        } finally {
            return operation
        }
    }

    public fun deposit(accountId: String, depositMoney: BigDecimal): Operation {
        val deposit = newOperation(accountId, OperationType.DEPOSIT, depositMoney)
        var extract : Extract = extractRepository.findById(accountId).get()
        extract.money = depositMoney.add(extract.money)
        extractRepository.save(extract)
        operationRepository.save(deposit)
        return deposit
    }

    public fun withdraw(accountId: String, withdrawMoney: BigDecimal): Operation {
        val withdraw = newOperation(accountId, OperationType.WITHDRAW, withdrawMoney)
        var extract : Extract = extractRepository.findById(accountId).get()
        extract.money = withdrawMoney.subtract(extract.money)
        extractRepository.save(extract)
        operationRepository.save(withdraw)
        return withdraw
    }
}