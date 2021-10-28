package com.kt.bank.extract.service

import com.kt.bank.extract.domain.Extract
import com.kt.bank.extract.domain.Operation
import com.kt.bank.extract.domain.OperationStatus
import com.kt.bank.extract.domain.OperationType
import com.kt.bank.extract.exception.OperationException
import com.kt.bank.extract.repository.ExtractRepository
import com.kt.bank.extract.repository.OperationRepository
import org.springframework.stereotype.Service
import java.math.BigDecimal

@Service
class OperationService(private val operationRepository: OperationRepository,
                       private val extractRepository: ExtractRepository) {

    private fun newOperation(accountId: String, operationType: OperationType, money: BigDecimal): Operation {
        val operation = Operation()
        operation.operationType = operationType
        operation.money = money
        operation.accountId = accountId
        try {
            //TODO verify connection, add a circuit breaker or something that would actually break this sh!t
            operation.operationStatus = OperationStatus.OK
        } catch (e: OperationException) {
            operation.operationStatus = OperationStatus.ERROR
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