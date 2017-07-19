# AccountOperations
Simple client accounts management application.

Project is written in kotlin. 
It exposes simple REST API for bank account management. 
The spring boot is used.

## Build instructions
Just execute `gradle build` command

## Public API

When application is started then 3 accounts will be created. Test accounts ids are: `1, 2, 3`

 `GET /balance/{id}` - Will return account balance.
 
 `POST /transfer/{from}/{to}/{amount}` - Will transfer specified amount of money from `{from}` to `{to}` account id.
 
 `POST /deposit/{to}/{amount}` - Will deposit money to account.
 
 `POST /withdraw/{from}/{amount}` - Will withdraw money from account.
