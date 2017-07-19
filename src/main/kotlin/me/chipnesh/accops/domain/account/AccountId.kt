package me.chipnesh.accops.domain.account

import java.io.Serializable
import java.util.*
import javax.persistence.Embeddable

/**
 * Идентификатор счета.
 */
@Embeddable
data class AccountId(
        val value: String = UUID.randomUUID().toString()
) : Serializable {
    companion object {
        /**
         * Идентификатор наличных средств.
         */
        val CASH = AccountId("CASH")
    }
}