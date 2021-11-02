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

    fun deposit(extract: Extract): ResponseEntity<Operation> {
        var extract1 = extractRepository.findById(extract.accountId).orElse(null)
        var deposit = newOperation(extract1.accountId, OperationType.DEPOSIT, extract.money)
        if(extract1 == null){
            deposit.message = "Usuário não encontrado, crie o usuário no endpoint /new-extract com primeiro depósito"
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(deposit)
        }
        extract1.money = extract1.money + extract.money
        extractRepository.save(extract1)
        deposit.message = "Depósito solicitado com sucesso"
        deposit.operationType = OperationType.DEPOSIT
        deposit.operationStatus = OperationStatus.PENDING
        deposit.accountId = extract.accountId
        operationRepository.save(deposit)
        return ResponseEntity.status(HttpStatus.OK).body(deposit)
    }

    fun withdraw(extractRequest: Extract): ResponseEntity<Operation> {
        var withdraw = newOperation(extractRequest.accountId, OperationType.WITHDRAW, extractRequest.money)
        var extract = extractRepository.findById(extractRequest.accountId).orElse(null)
        if (extract == null){
            withdraw.message = "Usuário não encontrado"
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(withdraw)
        } else if (extractRequest.money > extract.money){
            withdraw.message = "Saldo insuficiente para saque"
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(withdraw)
        }
            extract.money = extract.money - extractRequest.money
        extractRepository.save(extract)
        withdraw.message = "saque solicitado com sucesso"
        withdraw.operationStatus = OperationStatus.PENDING
        withdraw.accountId = extract.accountId
        withdraw.operationType = OperationType.WITHDRAW
        operationRepository.save(withdraw)
        return ResponseEntity.status(HttpStatus.OK).body(withdraw)
    }

    fun confirmTransaction(operationId: Int): ResponseEntity<String> {
        var operation = operationRepository.findById(operationId).orElse(null)

        if(operation == null){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Operação não encontrada")
        }
        else if (operation.date.plusMinutes(6).isBefore(LocalDateTime.now())) {
            var extract = rollbackOperation(operation)
            extractRepository.save(extract)
            operationRepository.deleteById(operationId)
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Prazo de confirmação de operação excedido")
        }
        var operationHistory = operationToOperationHistory(operation)
        operationHistoryRepository.save(operationHistory)
        operationRepository.deleteById(operationId)
        return ResponseEntity.status(HttpStatus.OK).body("Transação de id: ${operation.id} confirmada")
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
        operationHistory.operationStatus = OperationStatus.OK
        operationHistory.date = operation.date
        operationHistory.message = operation.message
        operationHistory.confirmationDate = LocalDateTime.now()

        return operationHistory
    }
}