package com.pagrom.internship.controllers

import com.pagrom.internship.entities.Message
import com.pagrom.internship.entities.Template
import com.pagrom.internship.entities.TemplateDTO
import com.pagrom.internship.entities.TemplateSubstitution
import kotlinx.serialization.json.*
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
class TemplateControllerTest {
    @Autowired
    private lateinit var mockMvc: MockMvc

    private var expectedTemplate = Template(
        "templateId1",
        "Hello, \$year\$ year!",
        listOf("https://httpbin.org/post"),
        emptyMap()
    )

    private val templateDTO = TemplateDTO(
        "templateId1",
        "Hello, \$year\$ year!",
        listOf("https://httpbin.org/post")
    )

    @Test
    fun `test api template-list`() {
        val mvcResult: MvcResult = mockMvc.perform(MockMvcRequestBuilders.get("/template/list"))
            .andExpect(MockMvcResultMatchers.status().isOk)
            .andReturn()

        assertThat(mvcResult.request.contentAsString).isNull()
    }

    @Test
    fun `test api template-create`() {
        val body = Json.encodeToJsonElement(templateDTO).toString()

        val request = MockMvcRequestBuilders.post("/template/create")
            .contentType(MediaType.APPLICATION_JSON)
            .accept(MediaType.APPLICATION_JSON)
            .content(body)

        val mvcResult: MvcResult = mockMvc
            .perform(request)
            .andExpect(MockMvcResultMatchers.status().isOk)
            .andReturn()

        assertThat(mvcResult.request.contentAsString).isEqualTo(body)
        assertThat(mvcResult.response.contentAsString).isEqualTo(body)
    }

    @Test
    fun `test api list after create`() {
        `test api template-create`()

        val request = MockMvcRequestBuilders.get("/template/list")

        val mvcResult: MvcResult = mockMvc.perform(request)
            .andExpect(MockMvcResultMatchers.status().isOk)
            .andReturn()

        val messageTemplateJson = Json.parseToJsonElement(mvcResult.response.contentAsString)

        val actualTemplate = messageTemplateJson.jsonObject["content"]?.jsonArray?.get(0)

        assertThat(actualTemplate!!).isNotNull
        assertThat(Json.decodeFromJsonElement<Template>(actualTemplate))
            .isEqualTo(expectedTemplate)
    }

    @Test
    fun `test substitution and send template`() {
        `test api template-create`()

        val messageSender = TemplateSubstitution("templateId1", listOf(mapOf("year" to "2020")))
        val body = Json.encodeToJsonElement(messageSender).toString()

        val request = MockMvcRequestBuilders.post("/template/send")
            .contentType(MediaType.APPLICATION_JSON)
            .accept(MediaType.APPLICATION_JSON)
            .content(body)

        val mvcResult: MvcResult = mockMvc.perform(request)
            .andExpect(MockMvcResultMatchers.status().isOk)
            .andReturn()

        val message = Message("Hello, 2020 year!")
        assertThat(mvcResult.request.contentAsString).isEqualTo(body)
        assertThat(mvcResult.response.contentAsString)
            .isEqualTo(Json.encodeToJsonElement(message).toString())
    }

    @Test
    fun `test substitution exception`() {
        `test api template-create`()

        val messageSender = TemplateSubstitution("incorrectId", listOf(mapOf("year" to "2020")))
        val body = Json.encodeToJsonElement(messageSender).toString()

        val request = MockMvcRequestBuilders.post("/template/send")
            .contentType(MediaType.APPLICATION_JSON)
            .accept(MediaType.APPLICATION_JSON)
            .content(body)

        val mvcResult: MvcResult = mockMvc.perform(request)
            .andExpect(MockMvcResultMatchers.status().isBadRequest)
            .andReturn()

        assertThat(mvcResult.request.contentAsString).isEqualTo(body)
        assertThat(mvcResult.response.contentAsString)
            .isEqualTo(Json.encodeToJsonElement(Message("Template not found")).toString())
    }

    @Test
    fun `test substitution without value`() {
        `test api template-create`()

        val messageSender = TemplateSubstitution("templateId1", listOf(mapOf("incorrectValue" to "2020")))
        val body = Json.encodeToJsonElement(messageSender).toString()

        val request = MockMvcRequestBuilders.post("/template/send")
            .contentType(MediaType.APPLICATION_JSON)
            .accept(MediaType.APPLICATION_JSON)
            .content(body)

        val mvcResult: MvcResult = mockMvc.perform(request)
            .andExpect(MockMvcResultMatchers.status().isOk)
            .andReturn()

        val message = Message(templateDTO.template)
        assertThat(mvcResult.request.contentAsString).isEqualTo(body)
        assertThat(mvcResult.response.contentAsString)
            .isEqualTo(Json.encodeToJsonElement(message).toString())
    }
}