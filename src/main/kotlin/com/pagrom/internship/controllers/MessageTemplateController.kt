package com.pagrom.internship.controllers

import com.pagrom.internship.entities.MessageSender
import com.pagrom.internship.entities.MessageTemplate
import com.pagrom.internship.services.impl.HttpSenderService
import com.pagrom.internship.services.impl.MessageTemplateServiceImpl
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController

@RestController
class MessageTemplateController(
    val messageTemplateService: MessageTemplateServiceImpl,
    val httpServiceService: HttpSenderService
) {
    @PostMapping("/template/load")
    fun loadTemplate(@RequestBody messageTemplate: MessageTemplate): MessageTemplate {
        return messageTemplateService.create(messageTemplate)
    }

    @PostMapping("/template/send")
    fun sendTemplate(@RequestBody messageSender: MessageSender) {
        // TODO: fix
        // Get template
        val messageTemplate = messageTemplateService.findByTemplateId(messageSender.templateId)

        // Prepare to replace
        val variables = mutableMapOf<String, String>()
        for (variable in messageSender.variables) {
            for ((name, value) in variable) {
                variables[name] = value
            }
        }

        // Get new template
        val template = httpServiceService.substitution(messageTemplate.template, variables)

        // Get urls
        httpServiceService.send(template, messageTemplate.recipients)
    }

    // TODO: remove
    @GetMapping("/template/list")
    fun templates(pageable: Pageable): Page<MessageTemplate> {
        return messageTemplateService.list(pageable)
    }
}