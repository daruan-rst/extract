package com.kt.bank.extract.repository

import com.kt.bank.extract.domain.OperationHistory
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface OperationHistoryRepository: JpaRepository <OperationHistory, String> {

    fun findByAccountId(accountId: String):List<OperationHistory>
}