package com.kt.bank.extract.domain


import java.math.BigDecimal
import javax.persistence.*


@Entity
@Table
data class Extract(@Id val accountId:String = "", val money:BigDecimal = BigDecimal.ZERO)