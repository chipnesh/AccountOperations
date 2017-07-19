package me.chipnesh.accops.domain.account

class NotEnoughMoney(from: String, needed: Money)
    : RuntimeException("Not enough money for account [$from]. You need $needed for the transfer to complete.")