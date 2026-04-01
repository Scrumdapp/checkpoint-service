package com.scrumdapp.checkinservice

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class CheckinServiceApplication

fun main(args: Array<String>) {
    runApplication<CheckinServiceApplication>(*args)
}
