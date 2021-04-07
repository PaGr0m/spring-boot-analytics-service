package com.pagrom.internship.services.impl

import com.pagrom.internship.entities.MessageTemplate
import com.pagrom.internship.repositories.MessageTemplateRepository
import com.pagrom.internship.services.MessageTemplateService
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.mockito.Mockito.mock
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageImpl
import org.springframework.data.domain.Pageable
import java.util.*
import javax.persistence.EntityNotFoundException
import org.mockito.Mockito.`when` as mockitoWhen


internal class MessageTemplateServiceImplTest {

    private val messageTemplateRepository: MessageTemplateRepository = mock(MessageTemplateRepository::class.java)
    private val messageTemplateService: MessageTemplateService = MessageTemplateServiceImpl(messageTemplateRepository)

    @Test
    fun `test create single messageTemplate`() {
        // Arrange
        val messageTemplate = MessageTemplate("", "", emptyList())
        mockitoWhen(messageTemplateRepository.save(messageTemplate)).thenReturn(messageTemplate)

        // Act
        val actual = messageTemplateService.create(messageTemplate)

        // Assert
        assertThat(actual).isEqualTo(messageTemplate)
    }

    @Test
    fun `test find all templates`() {
        // Arrange
        val templates = listOf<MessageTemplate>(
            MessageTemplate("1", "aaa", emptyList()),
            MessageTemplate("2", "bbb", listOf("")),
            MessageTemplate("3", "ccc", listOf("firstUrl", "secondUrl")),
        )

        val pageable = mock(Pageable::class.java)
        val templatePage: Page<MessageTemplate> = PageImpl(templates)
        mockitoWhen(messageTemplateRepository.findAll(pageable)).thenReturn(templatePage)

        // Act
        val actual: Page<MessageTemplate> = messageTemplateService.list(pageable)

        // Assert
        assertThat(actual).isEqualTo(templatePage)
    }

    @Test
    fun `test find messageTemplate by id`() {
        // Arrange
        val messageTemplate = MessageTemplate("", "", emptyList())
        mockitoWhen(messageTemplateRepository.findById(messageTemplate.templateId))
            .thenReturn(Optional.of(messageTemplate))

        // Act
        val actual = messageTemplateService.findByTemplateId(messageTemplate.templateId)

        // Assert
        assertThat(actual).isEqualTo(messageTemplate)
    }

    @Test
    fun `test find messageTemplate when does not exist`() {
        // Arrange
        val templateId = "templateId"
        mockitoWhen(messageTemplateRepository.findById(templateId))
            .thenReturn(Optional.empty())

        // Assert
        assertThrows<EntityNotFoundException> { messageTemplateService.findByTemplateId(templateId) }
    }
}