package me.chipnesh.accops.domain.account

import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional
import java.math.BigDecimal
import java.util.*

/**
 * Служба управления счетами.
 */
@Component
class AccountService(val accountRepo: AccountRepository) {
    /**
     * Создаёт новый счет.
     */
    @Transactional
    fun createNew(id: String = UUID.randomUUID().toString(),
                  amount: BigDecimal = BigDecimal.ZERO): Account {
        if (amount < BigDecimal.ZERO) throw IllegalArgumentException("Amount should be greater then zero.")
        val newAccount = Account(AccountId(id))

        val accountToSave = if (amount > BigDecimal.ZERO)
            newAccount.depositCash(Money(amount))
        else newAccount

        return accountRepo.save(accountToSave)
    }

    /**
     * Перечисляет средства с одного счёта на другой.
     */
    @Transactional
    fun transfer(from: String, to: String, amount: BigDecimal) {
        if (amount < BigDecimal.ZERO) throw IllegalArgumentException("Amount should be greater then zero.")

        val fromAccount = accountRepo.findOne(AccountId(from)) ?: throw NotFoundException(from)
        val toAccount = accountRepo.findOne(AccountId(to)) ?: throw NotFoundException(to)

        val balance = fromAccount.balance()
        val amountToTransfer = Money(amount)
        if (balance < amountToTransfer) throw NotEnoughMoney(from, amountToTransfer - balance)

        accountRepo.save(fromAccount.withdrawTo(toAccount, amountToTransfer))
        accountRepo.save(toAccount.depositFrom(fromAccount, amountToTransfer))
    }

    /**
     * Пополняет счёт наличными.
     */
    @Transactional
    fun depositCash(to: String, amount: BigDecimal): Account {
        if (amount < BigDecimal.ZERO) throw IllegalArgumentException("Amount should be greater then zero.")
        val account = accountRepo.findOne(AccountId(to)) ?: throw NotFoundException(to)
        return accountRepo.save(account.depositCash(Money(amount)))
    }

    /**
     * Снимает наличные со счёта.
     */
    @Transactional
    fun withdrawCash(from: String, amount: BigDecimal): Account {
        if (amount < BigDecimal.ZERO) throw IllegalArgumentException("Amount should be greater then zero.")
        val account = accountRepo.findOne(AccountId(from)) ?: throw NotFoundException(from)

        val balance = account.balance()
        val amountToWithdraw = Money(amount)
        if (balance < amountToWithdraw) throw NotEnoughMoney(from, amountToWithdraw - balance)

        return accountRepo.save(account.withdrawCash(amountToWithdraw))
    }

    /**
     * Возвращает баланс.
     */
    @Transactional(readOnly = true)
    fun balance(from: String): Money {
        val account = accountRepo.findOne(AccountId(from)) ?: throw NotFoundException(from)
        return account.balance()
    }
}