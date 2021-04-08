package com.pagrom.internship.services.impl

import com.fasterxml.jackson.databind.ObjectMapper
import com.pagrom.internship.entities.Message
import com.pagrom.internship.services.SenderService
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Service
import java.net.URI
import java.net.http.HttpClient
import java.net.http.HttpRequest
import java.net.http.HttpResponse

@Service
class HttpSenderService : SenderService {
    // Temporary storage for ScheduledMethod
    private var template: String? = null
    private var urls: List<String> = emptyList()

    override fun substitution(
        template: String,
        variables: Map<String, String>,
        validations: Map<String, String>
    ): String {
        var substitutionTemplate = template

        if (validations.isEmpty()) {
            variables.forEach { (varName, varValue) ->
                substitutionTemplate = substitutionTemplate.replace("$$varName$", varValue)
            }

            this.template = substitutionTemplate
            return substitutionTemplate
        } else {
            for ((varName, varValue) in variables) {
                substitutionTemplate = if (validations.containsKey(varName)) {
                    val type = validations[varName]!!
                    if (!validate(type, varValue)) {
                        throw IllegalArgumentException("Substitution type error [$varName : $type = \"$varValue\"]")
                    }
                    substitutionTemplate.replace("$$varName$", varValue)
                } else {
                    substitutionTemplate.replace("$$varName$", varValue)
                }
            }
            this.template = substitutionTemplate
        }

        return substitutionTemplate
    }

    override fun send(template: String, urls: List<String>): Message {
        this.urls = urls

        val message = Message(template)

        val objectMapper = ObjectMapper()
        val requestBody: String = objectMapper.writeValueAsString(message)
        val client = HttpClient.newBuilder().build()

        for (url in urls) {
            val request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .POST(HttpRequest.BodyPublishers.ofString(requestBody))
                .build()
            client.send(request, HttpResponse.BodyHandlers.ofString())
        }

        return message
    }

    // Scheduled method to send template
    @Scheduled(initialDelay = 5_000, fixedDelay = 10 * 60 * 1_000)
    fun scheduleSender() {
        if (urls.isNotEmpty()) {
            template?.let { send(it, urls) }
        }
    }

    // Only primitive types
    private fun validate(type: String, value: String): Boolean {
        try {
            when (type) {
                "byte" -> value.toByte()
                "short" -> value.toShort()
                "int" -> value.toInt()
                "long" -> value.toLong()

                "float" -> value.toFloat()
                "double" -> value.toDouble()

                "string" -> return true

                else -> return false
            }
        } catch (ignored: NumberFormatException) {
            return false
        } catch (ignored: IllegalArgumentException) {
            return false
        }

        return true
    }
}