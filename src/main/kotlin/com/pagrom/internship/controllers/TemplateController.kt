package com.pagrom.internship.controllers

import com.pagrom.internship.entities.Message
import com.pagrom.internship.entities.Template
import com.pagrom.internship.entities.TemplateDTO
import com.pagrom.internship.entities.TemplateSubstitution
import com.pagrom.internship.services.TemplateService
import com.pagrom.internship.services.impl.HttpSenderService
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController
import javax.persistence.EntityNotFoundException

@RestController
class TemplateController(
    val templateService: TemplateService,
    val httpServiceService: HttpSenderService
) {
    @PostMapping("/template/create")
    fun loadTemplate(@RequestBody templateDTO: TemplateDTO): Template {
        return templateService.create(templateDTO)
    }

    @PostMapping("/template/send")
    fun sendTemplate(@RequestBody templateSubstitution: TemplateSubstitution): ResponseEntity<Message> {
        return try {
            val messageTemplate = templateService.findByTemplateId(templateSubstitution.templateId)
            val variables = templateSubstitution.variables.flatten()
            val validations = messageTemplate.validation
            val substitutedTemplate = httpServiceService.substitution(
                messageTemplate.template,
                variables,
                validations
            )
            val message = httpServiceService.send(substitutedTemplate, messageTemplate.recipients)

            ResponseEntity.status(HttpStatus.OK).body(message)
        } catch (e: EntityNotFoundException) {
            ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Message(e.message!!))
        } catch (e: IllegalArgumentException) {
            ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Message(e.message!!))
        }
    }

    @GetMapping("/template/list")
    fun templates(pageable: Pageable): Page<Template> {
        return templateService.list(pageable)
    }

    private fun <T, R> List<Map<T, R>>.flatten(): Map<T, R> {
        val map = mutableMapOf<T, R>()

        for (variable in this) {
            for ((name, value) in variable) {
                map[name] = value
            }
        }

        return map
    }
}