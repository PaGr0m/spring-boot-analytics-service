package com.pagrom.internship.controllers

import com.fasterxml.jackson.databind.ObjectMapper
import com.pagrom.internship.entities.Message
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.decodeFromJsonElement
import kotlinx.serialization.json.jsonObject
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.springframework.http.HttpMethod
import org.springframework.http.HttpStatus
import java.net.URI
import java.net.http.HttpClient
import java.net.http.HttpRequest
import java.net.http.HttpResponse

/**
 * Test REST API with {@link https://httpbin.org/#/HTTP_Methods}
 */
class EchoServerRestTest {
    private val url = "https://httpbin.org"
    private val postUrl = "$url/post"
    private val getUrl = "$url/get"

    /**
     * Test post request with echo server
     */
    @Test
    fun `test post request`() {
        val message = Message("empty")

        val objectMapper = ObjectMapper()
        val requestBody: String = objectMapper.writeValueAsString(message)
        val client = HttpClient.newBuilder().build()

        val request = HttpRequest.newBuilder()
            .uri(URI.create(postUrl))
            .POST(HttpRequest.BodyPublishers.ofString(requestBody))
            .build()
        val response = client.send(request, HttpResponse.BodyHandlers.ofString())

        assertThat(request.uri().toString()).isEqualTo(postUrl)
        assertThat(request.method()).isEqualTo(HttpMethod.POST.toString())

        assertThat(response.uri().toString()).isEqualTo(postUrl)
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value())

        val data = Json.parseToJsonElement(response.body()).jsonObject["data"]
        assertThat(data!!).isNotNull

        val stringData = Json.decodeFromJsonElement<String>(data)
        val echoMessage = Json.decodeFromString<Message>(stringData)

        assertThat(echoMessage).isEqualTo(message)
    }

    /**
     * Test get request with echo server
     */
    @Test
    fun `test get request`() {
        val client = HttpClient.newBuilder().build()

        val request = HttpRequest.newBuilder()
            .uri(URI.create(getUrl))
            .GET()
            .build()
        val response = client.send(request, HttpResponse.BodyHandlers.ofString())

        assertThat(request.uri().toString()).isEqualTo(getUrl)
        assertThat(request.method()).isEqualTo(HttpMethod.GET.toString())

        assertThat(response.uri().toString()).isEqualTo(getUrl)
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value())
    }
}