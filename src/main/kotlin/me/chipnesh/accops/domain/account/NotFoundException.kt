package me.chipnesh.accops.domain.account

class NotFoundException(id: String)
    : RuntimeException("Account [$id] not found.")