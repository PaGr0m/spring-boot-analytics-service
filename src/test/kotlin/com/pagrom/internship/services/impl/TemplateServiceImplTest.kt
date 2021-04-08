package com.pagrom.internship.services.impl

import com.pagrom.internship.entities.Template
import com.pagrom.internship.repositories.TemplateRepository
import com.pagrom.internship.services.TemplateService
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


internal class TemplateServiceImplTest {

    private val templateRepository: TemplateRepository = mock(TemplateRepository::class.java)
    private val templateService: TemplateService = TemplateServiceImpl(templateRepository)

    @Test
    fun `test create single messageTemplate`() {
        // Arrange
        val messageTemplate = Template("", "", emptyList())
        mockitoWhen(templateRepository.save(messageTemplate)).thenReturn(messageTemplate)

        // Act
//        val actual = messageTemplateService.create(messageTemplate)

        // Assert
//        assertThat(actual).isEqualTo(messageTemplate)
    }

    @Test
    fun `test find all templates`() {
        // Arrange
        val templates = listOf<Template>(
            Template("1", "aaa", emptyList()),
            Template("2", "bbb", listOf("")),
            Template("3", "ccc", listOf("firstUrl", "secondUrl")),
        )

        val pageable = mock(Pageable::class.java)
        val templatePage: Page<Template> = PageImpl(templates)
        mockitoWhen(templateRepository.findAll(pageable)).thenReturn(templatePage)

        // Act
        val actual: Page<Template> = templateService.list(pageable)

        // Assert
        assertThat(actual).isEqualTo(templatePage)
    }

    @Test
    fun `test find messageTemplate by id`() {
        // Arrange
        val messageTemplate = Template("", "", emptyList())
        mockitoWhen(templateRepository.findById(messageTemplate.templateId))
            .thenReturn(Optional.of(messageTemplate))

        // Act
        val actual = templateService.findByTemplateId(messageTemplate.templateId)

        // Assert
        assertThat(actual).isEqualTo(messageTemplate)
    }

    @Test
    fun `test find messageTemplate when does not exist`() {
        // Arrange
        val templateId = "templateId"
        mockitoWhen(templateRepository.findById(templateId))
            .thenReturn(Optional.empty())

        // Assert
        assertThrows<EntityNotFoundException> { templateService.findByTemplateId(templateId) }
    }
}