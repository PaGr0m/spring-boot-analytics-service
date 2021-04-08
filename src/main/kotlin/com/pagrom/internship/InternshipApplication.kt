package com.pagrom.internship

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.scheduling.annotation.EnableScheduling

@SpringBootApplication
@EnableScheduling
class InternshipApplication

fun main(args: Array<String>) {
    runApplication<InternshipApplication>(*args)
}