package com.kt.bank.extract.service

import com.kt.bank.extract.client.AccountClient
import com.kt.bank.extract.domain.*
import com.kt.bank.extract.exception.InvalidAccountException
import com.kt.bank.extract.repository.ExtractRepository
import com.kt.bank.extract.repository.OperationHistoryRepository
import com.kt.bank.extract.repository.OperationRepository
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Service
import java.math.BigDecimal
import java.time.LocalDateTime

@Service
class OperationService(private val operationRepository: OperationRepository,
                       private val extractRepository: ExtractRepository,
                       private val accountClient : AccountClient,
                       private val operationHistoryRepository: OperationHistoryRepository
) {

    private fun newOperation(accountId: String, operationType: OperationType, operationMoney: BigDecimal): Operation {
        val operation = Operation()
        operation.money = operationMoney
        operation.date = LocalDateTime.now()
        try {
            //TODO verify connection, add a circuit breaker or something that would actually break this sh!t
            accountClient.findById(accountId)
            operation.accountId = accountId
            operation.operationType = operationType
            operation.operationStatus = OperationStatus.OK
            var extract : Extract = extractRepository.findById(accountId).get()
            extract.money = operationMoney.add(extract.money)
            extractRepository.save(extract)
        } catch (e: InvalidAccountException) {
            operation.operationStatus = OperationStatus.ERROR
            operation.operationType = OperationType.BLANK
        } finally {
            return operation
        }
    }

    fun deposit(accountId: String, depositMoney: BigDecimal): ResponseEntity<Operation> {
        var extract = extractRepository.findById(accountId).orElse(null)
        val deposit = newOperation(accountId, OperationType.DEPOSIT, depositMoney)
        if(extract == null){
            deposit.message = "Usuário não encontrado, crie o usuário no endpoint /new-extract com primeiro depósito"
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(deposit)
        }
        extract.money = extract.money + depositMoney
        extractRepository.save(extract)
        deposit.message = "Deposito solicitado com sucesso"
        operationRepository.save(deposit)
        return ResponseEntity.status(HttpStatus.OK).body(deposit)
    }

    fun withdraw(accountId: String, withdrawMoney: BigDecimal): ResponseEntity<Operation> {
        var withdrawMoney = withdrawMoney.negate()
        val withdraw = newOperation(accountId, OperationType.WITHDRAW, withdrawMoney)
        var extract = extractRepository.findById(accountId).orElse(null)
        if (extract == null){
            withdraw.message = "Usuário não encontrado"
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(withdraw)
        } else if (withdrawMoney > extract.money){
            withdraw.message = "Saldo insuficiente para saque"
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(withdraw)
        }
            extract.money = extract.money - withdrawMoney
        extractRepository.save(extract)
        withdraw.message = "saque solicitado com sucesso"
        operationRepository.save(withdraw)
        return ResponseEntity.status(HttpStatus.OK).body(withdraw)
    }

    fun confirmTransaction(operationId: String): ResponseEntity<String> {
        var operation = operationRepository.findById(operationId).orElse(null)

        if(operation == null){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Operação não encontrada")
        }
        else if (operation.date.plusMinutes(6).isAfter(LocalDateTime.now())) {
            var extract = rollbackOperation(operation)
            extractRepository.save(extract)
            operationRepository.deleteById(operationId)
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Prazo de confirmação de operação excedido")
        }
        var operationHistory = operationToOperationHistory(operation)
        operationHistoryRepository.save(operationHistory)
        operationRepository.deleteById(operationId)
        return ResponseEntity.status(HttpStatus.OK).body("Transação de id: ${operation.id} confirmada}")
    }

    private fun rollbackOperation(operation: Operation) : Extract {
        var extract = extractRepository.findById(operation.accountId).orElse(null)
        if(operation.operationType == OperationType.DEPOSIT){
            extract.money = extract.money - operation.money
            return extract
        }
            extract.money = extract.money + operation.money
            return extract
    }

    fun operationToOperationHistory(operation: Operation): OperationHistory{
        val operationHistory = OperationHistory()
        operationHistory.operationId = operation.id
        operationHistory.accountId = operation.accountId
        operationHistory.money = operation.money
        operationHistory.operationType = operation.operationType
        operationHistory.operationStatus = operation.operationStatus
        operationHistory.date = operation.date
        operationHistory.message = operation.message
        operationHistory.confirmationDate = LocalDateTime.now()

        return operationHistory
    }
}