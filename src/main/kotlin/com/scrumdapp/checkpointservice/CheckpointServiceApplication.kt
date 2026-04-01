package com.scrumdapp.checkpointservice

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class CheckpointServiceApplication

fun main(args: Array<String>) {
    runApplication<CheckpointServiceApplication>(*args)
}
