package com.pagrom.internship.controllers

import com.pagrom.internship.entities.Message
import com.pagrom.internship.entities.Template
import com.pagrom.internship.entities.TemplateDTO
import com.pagrom.internship.entities.TemplateSubstitution
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.encodeToJsonElement
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.MvcResult
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders
import org.springframework.test.web.servlet.result.MockMvcResultMatchers

@SpringBootTest
@AutoConfigureMockMvc
class TemplateWithValidationControllerTest {
    @Autowired
    private lateinit var mockMvc: MockMvc

    private val templateWithValidationDTO = TemplateDTO(
        "templateIdWithValidation1",
        "1. \$name\$" +
        "2. \$year\$" +
        "3. \$number\$",
        listOf("https://httpbin.org/post"),
        listOf(
            mapOf(
                "name" to "string",
                "year" to "int",
            ),
            mapOf(
                "number" to "double"
            )
        )
    )

    @Test
    fun `test create template with validation`() {
        val body = Json.encodeToJsonElement(templateWithValidationDTO).toString()

        val request = MockMvcRequestBuilders.post("/template/create")
            .contentType(MediaType.APPLICATION_JSON)
            .accept(MediaType.APPLICATION_JSON)
            .content(body)

        val mvcResult: MvcResult = mockMvc
            .perform(request)
            .andExpect(MockMvcResultMatchers.status().isOk)
            .andReturn()

        val template = Template(
            "templateIdWithValidation1",
            "1. \$name\$" +
            "2. \$year\$" +
            "3. \$number\$",
            listOf("https://httpbin.org/post"),
            mapOf(
                "name" to "string",
                "year" to "int",
                "number" to "double"
            )
        )

        assertThat(mvcResult.request.contentAsString).isEqualTo(body)
        assertThat(Json.decodeFromString<Template>(mvcResult.response.contentAsString)).isEqualTo(template)
    }

    @Test
    fun `test substitution and send template with validation`() {
        `test create template with validation`()

        val messageSender = TemplateSubstitution(
            "templateIdWithValidation1",
            listOf(
                mapOf("name" to "someString"),
                mapOf("year" to "2021"),
                mapOf("number" to "3.14"),
            )
        )
        val body = Json.encodeToJsonElement(messageSender).toString()

        val request = MockMvcRequestBuilders.post("/template/send")
            .contentType(MediaType.APPLICATION_JSON)
            .accept(MediaType.APPLICATION_JSON)
            .content(body)

        val mvcResult: MvcResult = mockMvc.perform(request)
            .andExpect(MockMvcResultMatchers.status().isOk)
            .andReturn()

        val message = Message(
            "1. someString" +
            "2. 2021" +
            "3. 3.14"
        )

        assertThat(mvcResult.request.contentAsString).isEqualTo(body)
        assertThat(Json.decodeFromString<Message>(mvcResult.response.contentAsString)).isEqualTo(message)
    }

    @Test
    fun `test send template with validation error`() {
        `test create template with validation`()

        val messageSender = TemplateSubstitution(
            "templateIdWithValidation1",
            listOf(
                mapOf("year" to "someInt"),
            )
        )
        val body = Json.encodeToJsonElement(messageSender).toString()

        val request = MockMvcRequestBuilders.post("/template/send")
            .contentType(MediaType.APPLICATION_JSON)
            .accept(MediaType.APPLICATION_JSON)
            .content(body)

        val mvcResult: MvcResult = mockMvc.perform(request)
            .andExpect(MockMvcResultMatchers.status().isBadRequest)
            .andReturn()

        val errorMessage = Message("Substitution type error [year : int = \"someInt\"]")

        assertThat(mvcResult.request.contentAsString).isEqualTo(body)
        assertThat(Json.decodeFromString<Message>(mvcResult.response.contentAsString)).isEqualTo(errorMessage)
    }
}