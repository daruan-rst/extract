package com.kt.bank.extract.repository

import com.kt.bank.extract.domain.Operation
import org.springframework.data.jpa.repository.JpaRepository

interface OperationRepository : JpaRepository<Operation, String> {
}