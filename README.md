# AccountOperations
Simple bank account management application.

 The application has written in kotlin and exposes simple 
 REST API for the bank account management. The spring boot is used. 
 The spring boot is used.

## Build instructions
Just execute the `gradle build` command

## Public API

When application is started then 3 test accounts will be created. Accounts ids are: `1, 2, 3`

 `GET /balance/{id}` - Will return account balance.
 
 `POST /transfer/{from}/{to}/{amount}` - Will transfer specified amount of money from `{from}` to `{to}` account id.
 
 `POST /deposit/{to}/{amount}` - Will deposit cash to account.
 
 `POST /withdraw/{from}/{amount}` - Will withdraw cash from account.
