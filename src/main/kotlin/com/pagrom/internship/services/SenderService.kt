package com.pagrom.internship.services

interface SenderService {
    fun substitution(template: String, variables: Map<String, String>): String
    fun send(template: String, urls: List<String>)
}