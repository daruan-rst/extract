package com.kt.bank.extract.domain

import lombok.Getter
import lombok.Setter
import java.math.BigDecimal
import javax.persistence.*


@Entity
@Table
class Extract() {

    @Id
    var accountId:String = ""
    var money:BigDecimal = BigDecimal.ZERO

}