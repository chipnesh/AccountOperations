package me.chipnesh.accops.web

import me.chipnesh.accops.domain.account.NotEnoughMoney
import me.chipnesh.accops.domain.account.NotFoundException
import org.springframework.http.HttpStatus
import org.springframework.orm.ObjectOptimisticLockingFailureException
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler
import javax.servlet.http.HttpServletResponse

@ControllerAdvice
class ExceptionMapperAdvice {
    @ExceptionHandler(IllegalArgumentException::class)
    fun handleIllegalArguments(res: HttpServletResponse, e: IllegalArgumentException) {
        res.sendError(HttpStatus.BAD_REQUEST.value(), e.message)
    }

    @ExceptionHandler(NotEnoughMoney::class)
    fun handleNotEnoughMoney(res: HttpServletResponse, e: NotEnoughMoney) {
        res.sendError(HttpStatus.BAD_REQUEST.value(), e.message)
    }

    @ExceptionHandler(NotFoundException::class)
    fun handleNotFound(res: HttpServletResponse, e: NotFoundException) {
        res.sendError(HttpStatus.NOT_FOUND.value(), e.message)
    }

    @ExceptionHandler(ObjectOptimisticLockingFailureException::class)
    fun handleNotFound(res: HttpServletResponse, e: ObjectOptimisticLockingFailureException) {
        res.sendError(HttpStatus.CONFLICT.value(), e.message)
    }
}