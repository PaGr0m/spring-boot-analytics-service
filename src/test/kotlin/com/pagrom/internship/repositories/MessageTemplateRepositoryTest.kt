package com.pagrom.internship.repositories

import com.pagrom.internship.entities.MessageTemplate
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageRequest
import java.util.*

@DataJpaTest
class MessageTemplateRepositoryTest @Autowired constructor(
    private val messageTemplateRepository: MessageTemplateRepository
) {
    private val messageTemplate1 = MessageTemplate(
        "templateId1",
        "templateText1",
        emptyList()
    )
    private val messageTemplate2 = MessageTemplate(
        "templateId2",
        "templateText2",
        listOf("url")
    )
    private val messageTemplate3 = MessageTemplate(
        "templateId3",
        "templateText3",
        listOf("url1", "url2", "url3")
    )

    @BeforeEach
    fun setUp() {
        messageTemplateRepository.save(messageTemplate1)
        messageTemplateRepository.save(messageTemplate2)
        messageTemplateRepository.save(messageTemplate3)
    }

    @AfterEach
    fun tearDown() {
        messageTemplateRepository.delete(messageTemplate1)
        messageTemplateRepository.delete(messageTemplate2)
        messageTemplateRepository.delete(messageTemplate3)
    }

    @Test
    fun `test find all from database`() {
        // Arrange
        val expected = listOf(messageTemplate1, messageTemplate2, messageTemplate3)

        // Act
        val templates: Page<MessageTemplate> = messageTemplateRepository.findAll(PageRequest.of(0, 10))

        // Assert
        templates.content.zip(expected)
            .forEach { (actual, expected) ->
                assertThat(actual.templateId).isEqualTo(expected.templateId)
                assertThat(actual.template).isEqualTo(expected.template)
                assertThat(actual.recipients).containsAll(expected.recipients)
            }
    }

    @Test
    fun `test find by id when entity does not exist`() {
        // Act
        val emptyTemplate: Optional<MessageTemplate> = messageTemplateRepository.findById("incorrectId")
        val template: Optional<MessageTemplate> = messageTemplateRepository.findById("templateId1")

        // Assert
        assertThat(emptyTemplate.isEmpty).isTrue
        assertThat(template.isPresent).isTrue
    }
}