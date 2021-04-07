package com.pagrom.internship.services.impl

import com.fasterxml.jackson.databind.ObjectMapper
import com.pagrom.internship.entities.Message
import com.pagrom.internship.services.SenderService
import org.springframework.stereotype.Service
import java.net.URI
import java.net.http.HttpClient
import java.net.http.HttpRequest
import java.net.http.HttpResponse

@Service
class HttpSenderService : SenderService {
    override fun substitution(template: String, variables: Map<String, String>): String {
        var finalTemplate = ""
        variables.forEach { (name, value) ->
            finalTemplate = template.replace("$$name$", value)
        }

        return finalTemplate
    }

    override fun send(template: String, urls: List<String>): Message {
        val message = Message(template)

        val objectMapper = ObjectMapper()
        val requestBody: String = objectMapper.writeValueAsString(message)
        val client = HttpClient.newBuilder().build()

        for (url in urls) {
            val request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .POST(HttpRequest.BodyPublishers.ofString(requestBody))
                .build()
            val response = client.send(request, HttpResponse.BodyHandlers.ofString())
        }

        return message
    }
}