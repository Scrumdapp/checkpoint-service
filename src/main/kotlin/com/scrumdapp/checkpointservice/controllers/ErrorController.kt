package com.scrumdapp.checkpointservice.controllers

import com.scrumdapp.checkpointservice.ApiException
import jakarta.servlet.RequestDispatcher
import jakarta.servlet.http.HttpServletRequest
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController

@RestController
class ErrorController {

    @GetMapping("/error")
    fun handleError(req: HttpServletRequest) {
        val status = Integer.valueOf(req.getAttribute(RequestDispatcher.ERROR_STATUS_CODE).toString())
        val message = req.getAttribute(RequestDispatcher.ERROR_MESSAGE).toString()
        throw ApiException(status, message)
    }
}