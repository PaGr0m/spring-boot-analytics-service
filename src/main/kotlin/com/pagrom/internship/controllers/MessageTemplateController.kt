package com.pagrom.internship.controllers

import com.pagrom.internship.entities.Message
import com.pagrom.internship.entities.MessageSender
import com.pagrom.internship.entities.MessageTemplate
import com.pagrom.internship.services.impl.HttpSenderService
import com.pagrom.internship.services.impl.MessageTemplateServiceImpl
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
class MessageTemplateController(
    val messageTemplateService: MessageTemplateServiceImpl,
    val httpServiceService: HttpSenderService
) {
    @PostMapping("/template/create")
    fun loadTemplate(@RequestBody messageTemplate: MessageTemplate): MessageTemplate {
        return messageTemplateService.create(messageTemplate)
    }

    @PostMapping("/template/send")
    fun sendTemplate(@RequestBody messageSender: MessageSender): ResponseEntity<Message> {
        try {
            val messageTemplate = messageTemplateService.findByTemplateId(messageSender.templateId)

            val variables = mutableMapOf<String, String>()
            for (variable in messageSender.variables) {
                for ((name, value) in variable) {
                    variables[name] = value
                }
            }

            val template = httpServiceService.substitution(messageTemplate.template, variables)
            val message = httpServiceService.send(template, messageTemplate.recipients)

            return ResponseEntity.status(HttpStatus.OK).body(message)
        } catch (e: EntityNotFoundException) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Message(e.message!!))
        }
    }

    @GetMapping("/template/list")
    fun templates(pageable: Pageable): Page<MessageTemplate> {
        return messageTemplateService.list(pageable)
    }
}