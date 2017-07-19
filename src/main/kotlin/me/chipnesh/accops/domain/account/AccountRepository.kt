package me.chipnesh.accops.domain.account

import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository

/**
 * Репозиторий счетов.
 */
@Repository
interface AccountRepository : CrudRepository<Account, AccountId>