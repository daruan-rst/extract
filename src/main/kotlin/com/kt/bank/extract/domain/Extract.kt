package com.kt.bank.extract.domain

import lombok.Getter
import lombok.Setter
import java.math.BigDecimal


@Getter
data class Extract(val userId:String ,val balance:BigDecimal) {

}