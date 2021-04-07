package com.pagrom.internship.services

import com.pagrom.internship.entities.MessageTemplate
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable

interface MessageTemplateService {
    fun create(messageTemplate: MessageTemplate): MessageTemplate

    fun list(pageable: Pageable): Page<MessageTemplate>

    fun findByTemplateId(templateId: String): MessageTemplate
}