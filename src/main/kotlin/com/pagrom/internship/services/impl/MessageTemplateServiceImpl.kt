package com.pagrom.internship.services.impl

import com.pagrom.internship.entities.MessageTemplate
import com.pagrom.internship.repositories.MessageTemplateRepository
import com.pagrom.internship.services.MessageTemplateService
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service

@Service
class MessageTemplateServiceImpl(
    private val messageTemplateRepository: MessageTemplateRepository
) : MessageTemplateService {

    override fun create(messageTemplate: MessageTemplate): MessageTemplate {
        return messageTemplateRepository.save(messageTemplate)
    }

    override fun list(pageable: Pageable): Page<MessageTemplate> {
        return messageTemplateRepository.findAll(pageable)
    }

    override fun findByTemplateId(templateId: String): MessageTemplate {
        val messageTemplate = messageTemplateRepository.findById(templateId)

        if (!messageTemplate.isPresent) {
            throw IllegalArgumentException("")
        }

        return messageTemplate.get()
    }
}