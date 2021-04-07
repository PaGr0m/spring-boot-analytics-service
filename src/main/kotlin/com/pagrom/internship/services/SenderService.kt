package com.pagrom.internship.services

import com.pagrom.internship.entities.Message

interface SenderService {
    fun substitution(template: String, variables: Map<String, String>): String

    fun send(template: String, urls: List<String>): Message
}