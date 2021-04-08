package com.pagrom.internship

import org.junit.jupiter.api.Test
import org.springframework.boot.test.context.SpringBootTest

@SpringBootTest
class InternshipApplicationTests {

    @Test
    fun contextLoads() {
        val name = "asd"
        println(Regex.escapeReplacement("$$name$"))
        println("$$name$")

        println(Regex.escapeReplacement("$$$"))
        println("$$$")
    }
}
