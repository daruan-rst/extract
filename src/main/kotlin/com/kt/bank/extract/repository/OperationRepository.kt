package com.kt.bank.extract.repository

import com.kt.bank.extract.domain.Operation
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface OperationRepository : JpaRepository<Operation, Int> {
}