package me.chipnesh.accops.domain.account

import kotlinx.collections.immutable.toImmutableList
import java.time.ZonedDateTime
import javax.persistence.*

/**
 * Счет клиента.
 */
@Entity
@Table(name = "accounts")
data class Account(
        /**
         * Идентификатор счета.
         */
        @EmbeddedId
        private val id: AccountId = AccountId(),
        /**
         * События по счёту.
         */
        @OneToMany(cascade = arrayOf(CascadeType.ALL))
        private val events: List<AccountEvent> = mutableListOf(),

        @Version
        private val version: Int = 0
) {
    /**
     * Добавляет событие.
     */
    private fun addEvent(event: AccountEvent) = Account(id, events.toImmutableList().add(event))

    /**
     * Добавляет наличные.
     */
    fun depositCash(amount: Money) = addEvent(AccountEvent(AccountId.CASH, id, amount, ZonedDateTime.now()))

    /**
     * Снимает наличные.
     */
    fun withdrawCash(amount: Money) = addEvent(AccountEvent(id, AccountId.CASH, -amount, ZonedDateTime.now()))

    /**
     * Добавляет со счёта на текущий счёт.
     */
    fun depositFrom(from: Account, amount: Money) = addEvent(AccountEvent(from.id, id, amount, ZonedDateTime.now()))

    /**
     * Снимает со счёта на текущий счёт.
     */
    fun withdrawTo(to: Account, amount: Money) = addEvent(AccountEvent(id, to.id, -amount, ZonedDateTime.now()))

    /**
     * Возвращает баланс текущего счёта.
     */
    fun balance() = events.fold(Money()) { sum, event -> sum + event.money }

    override fun equals(other: Any?): Boolean {
        if (other !is Account) return false
        if (other.id == id) return true
        return false
    }
}