package me.chipnesh.accops.domain.account

import java.math.BigDecimal
import javax.persistence.Embeddable

/**
 * Средства на счете.
 */
@Embeddable
data class Money(val amount: BigDecimal = BigDecimal.ZERO) {
    operator fun plus(add: Money) = Money(amount + add.amount)
    operator fun minus(minus: Money) = Money(amount - minus.amount)
    operator fun compareTo(other: Money) = amount.compareTo(other.amount)
    operator fun unaryMinus(): Money = Money(BigDecimal(0) - amount)
}