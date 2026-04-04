package com.scrumdapp.checkpointservice.controllers

import com.scrumdapp.checkpointservice.ApiException
import com.scrumdapp.checkpointservice.NotFoundException
import jakarta.servlet.RequestDispatcher
import jakarta.servlet.http.HttpServletRequest
import org.springframework.boot.webmvc.error.ErrorController
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController

@RestController
class ExceptionController: ErrorController {

    @GetMapping("/error")
    fun handleError(req: HttpServletRequest): ApiException {
        val status = Integer.valueOf(req.getAttribute(RequestDispatcher.ERROR_STATUS_CODE).toString())
        val message = req.getAttribute(RequestDispatcher.ERROR_MESSAGE).toString()

        when (status) {
            404 -> throw NotFoundException()
            else -> throw ApiException(status, message)
        }
    }
}