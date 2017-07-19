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
        val account = Account(AccountId("1")).depositCash(1.1.money())
        val repository = mock<AccountRepository> {
            on { save(any<Account>())} doReturn account
        }
        val service = AccountService(repository)

        val result = service.createNew("1", 1.1.bigdec())
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
                .depositCash(25.money())
                .withdrawCash(7.money())
                .depositCash(2.money())
        val repository = mock<AccountRepository> {
            on { findOne(AccountId("1")) } doReturn account
        }
        val service = AccountService(repository)

        val balance = service.balance("1")
        assertThat(balance).isEqualTo(20.money())
    }

    @Test
    fun shouldDepositCash() {
        val account = Account(AccountId("1"))
        val repository = mock<AccountRepository> {
            on { findOne(AccountId("1")) } doReturn account
            on { save(any<Account>())} doAnswer { it.arguments[0] as Account }
        }
        val service = AccountService(repository)

        val modified = service.depositCash("1", 10.bigdec())
        val balance = modified.balance()
        assertThat(balance).isEqualTo(10.money())
    }

    @Test
    fun shouldWithdrawCash() {
        val account = Account(AccountId("1")).depositCash(11.money())
        val repository = mock<AccountRepository> {
            on { findOne(AccountId("1")) } doReturn account
            on { save(any<Account>())} doAnswer { it.arguments[0] as Account }
        }
        val service = AccountService(repository)

        val modified = service.withdrawCash("1", 10.bigdec())
        val balance = modified.balance()
        assertThat(balance).isEqualTo(1.money())
    }

    @Test
    fun shouldTransferMoney() {
        var accountFirst = Account(AccountId("1")).depositCash(10.money())
        var accountSecond = Account(AccountId("2")).depositCash(10.money())

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
        service.transfer("1", "2", 5.bigdec())

        assertThat(accountFirst.balance()).isEqualTo(5.money())
        assertThat(accountSecond.balance()).isEqualTo(15.money())
    }
}