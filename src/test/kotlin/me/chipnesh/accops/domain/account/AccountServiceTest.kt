package me.chipnesh.accops.domain.account

import com.nhaarman.mockito_kotlin.any
import com.nhaarman.mockito_kotlin.doAnswer
import com.nhaarman.mockito_kotlin.doReturn
import com.nhaarman.mockito_kotlin.mock
import org.assertj.core.api.Assertions.assertThat
import org.junit.Test
import java.math.BigDecimal

class AccountServiceTest {

    @Test
    fun shouldCreateNewAccount() {
        val account = Account(AccountId("1")).depositCash(Money(BigDecimal(1.1)))
        val repository = mock<AccountRepository> {
            on { save(any<Account>())} doAnswer { it.arguments[0] as Account}
        }
        val service = AccountService(repository)

        val result = service.createNew("1", BigDecimal(1.1))
        assertThat(account).isEqualTo(result)
    }

    @Test(expected = IllegalArgumentException::class)
    fun shouldThrowExceptionIfAmountIsNegative() {
        val service = AccountService(mock<AccountRepository>())

        service.createNew("1", BigDecimal(-1.1))
    }

    @Test
    fun shouldReturnCorrectBalance() {
        val account = Account(AccountId("1"))
                .depositCash(Money(BigDecimal(25)))
                .withdrawCash(Money(BigDecimal(7)))
                .depositCash(Money(BigDecimal(2)))
        val repository = mock<AccountRepository> {
            on { findOne(AccountId("1")) } doReturn account
        }
        val service = AccountService(repository)

        val balance = service.balance("1")
        assertThat(balance).isEqualTo(Money(BigDecimal(20)))
    }

    @Test
    fun shouldDepositCash() {
        val account = Account(AccountId("1"))
        val repository = mock<AccountRepository> {
            on { findOne(AccountId("1")) } doReturn account
            on { save(any<Account>())} doAnswer { it.arguments[0] as Account }
        }
        val service = AccountService(repository)

        val modified = service.depositCash("1", BigDecimal(10))
        val balance = modified.balance()
        assertThat(balance).isEqualTo(Money(BigDecimal(10)))
    }

    @Test
    fun shouldWithdrawCash() {
        val account = Account(AccountId("1")).depositCash(Money(BigDecimal(11)))
        val repository = mock<AccountRepository> {
            on { findOne(AccountId("1")) } doReturn account
            on { save(any<Account>())} doAnswer { it.arguments[0] as Account }
        }
        val service = AccountService(repository)

        val modified = service.withdrawCash("1", BigDecimal(10))
        val balance = modified.balance()
        assertThat(balance).isEqualTo(Money(BigDecimal(1)))
    }

    @Test(expected = NotEnoughMoney::class)
    fun shouldTrowNotEnoughMoney() {
        val account = Account(AccountId("1")).depositCash(Money(BigDecimal(1)))
        val repository = mock<AccountRepository> {
            on { findOne(AccountId("1")) } doReturn account
            on { save(any<Account>())} doAnswer { it.arguments[0] as Account }
        }
        val service = AccountService(repository)

        service.withdrawCash("1", BigDecimal(10))
    }

    @Test
    fun shouldTransferMoney() {
        var accountFirst = Account(AccountId("1")).depositCash(Money(BigDecimal(10)))
        var accountSecond = Account(AccountId("2")).depositCash(Money(BigDecimal(10)))

        val repository = mock<AccountRepository> {
            on { findOne(any<AccountId>()) } doAnswer {
                val accountId = it.arguments[0] as AccountId
                when (accountId.value) {
                    "1" -> accountFirst
                    "2" -> accountSecond
                    else -> throw NotFoundException(accountId.value)
                }
            }
            on { save(any<Account>())} doAnswer {
                val account = it.arguments[0] as Account
                when(account) {
                    accountFirst -> { accountFirst = account; accountFirst }
                    accountSecond -> { accountSecond = account; accountSecond }
                    else -> throw IllegalArgumentException()
                }
            }
        }

        val service = AccountService(repository)
        service.transfer("1", "2", BigDecimal(5))

        assertThat(accountFirst.balance()).isEqualTo(Money(BigDecimal(5)))
        assertThat(accountSecond.balance()).isEqualTo(Money(BigDecimal(15)))
    }
}