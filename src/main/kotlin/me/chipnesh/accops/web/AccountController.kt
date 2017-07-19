package me.chipnesh.accops.web

import me.chipnesh.accops.domain.account.AccountService
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RestController
import java.math.BigDecimal

@RestController("/")
class AccountController(val accountService: AccountService) {

    @PostMapping("/transfer/{from}/{to}/{amount:.+}")
    fun transfer(@PathVariable from: String,
                 @PathVariable to: String,
                 @PathVariable amount: String) = accountService.transfer(from, to, BigDecimal(amount))

    @PostMapping("/deposit/{to}/{amount:.+}")
    fun depositCash(@PathVariable to: String,
                    @PathVariable amount: String) = accountService.depositCash(to, BigDecimal(amount))

    @PostMapping("/withdraw/{from}/{amount:.+}")
    fun withdrawCash(@PathVariable from: String,
                     @PathVariable amount: String) = accountService.withdrawCash(from, BigDecimal(amount))

    @GetMapping("/balance/{accountId}")
    fun balance(@PathVariable accountId: String) = accountService.balance(accountId)
}