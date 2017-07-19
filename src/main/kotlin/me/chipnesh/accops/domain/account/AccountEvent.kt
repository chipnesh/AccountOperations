package me.chipnesh.accops.domain.account

import java.time.ZonedDateTime
import java.util.*
import javax.persistence.*

/**
 * Событие по счёту
 */
@Entity
@Table(name = "events")
data class AccountEvent(
        /**
         * С какого счёта просходит списание.
         */
        @Embedded @AttributeOverride(name = "value", column = Column(name = "from_id"))
        val from: AccountId = AccountId("empty"),
        /**
         * На какой счёт переходят средства.
         */
        @Embedded @AttributeOverride(name = "value", column = Column(name = "to_id"))
        val to: AccountId = AccountId("empty"),
        /**
         * Перечисленные средства.
         */
        @Embedded
        val money: Money = Money(),
        /**
         * Время, когда событие произошло.
         */
        val occurred: ZonedDateTime = ZonedDateTime.now()
) {
    /**
     * Идентификатор события.
     */
    @Id val id: String = UUID.randomUUID().toString()
}