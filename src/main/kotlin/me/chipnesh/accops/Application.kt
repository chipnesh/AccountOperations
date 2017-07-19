package me.chipnesh.accops

import me.chipnesh.accops.domain.account.AccountService
import org.springframework.boot.CommandLineRunner
import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.context.annotation.Bean
import java.math.BigDecimal

@SpringBootApplication
class Application {
    @Bean
    fun populateDatabase(accountService: AccountService) = CommandLineRunner {
        accountService.createNew(id = "1", amount = BigDecimal(10000))
        accountService.createNew(id = "2", amount = BigDecimal(20000))
        accountService.createNew(id = "3", amount = BigDecimal(30000))
    }
}

fun main(args: Array<String>) {
    SpringApplication.run(Application::class.java, *args)
}
