package com.kt.bank.extract.repository

import com.kt.bank.extract.domain.Extract
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface ExtractRepository : JpaRepository<Extract, String> {
}