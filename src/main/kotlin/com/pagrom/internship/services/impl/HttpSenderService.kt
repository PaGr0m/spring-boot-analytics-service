package com.pagrom.internship.services.impl

import com.fasterxml.jackson.databind.ObjectMapper
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

        print("Before: ")
        println(template)
        variables.forEach { (name, value) -> finalTemplate = template.replace("$$name$", value) }
        print("After: ")
        println(template)

        return finalTemplate
    }

    override fun send(template: String, urls: List<String>) {
        val objectMapper = ObjectMapper()
        val requestBody: String = objectMapper.writeValueAsString(template)
        val client = HttpClient.newBuilder().build();

        for (url in urls) {
            val request = HttpRequest.newBuilder()
//                .uri(URI.create("https://httpbin.org/post"))
                .uri(URI.create(url))
                .POST(HttpRequest.BodyPublishers.ofString(requestBody))
                .build()
            val response = client.send(request, HttpResponse.BodyHandlers.ofString())

            println(response.body())
        }
    }
}