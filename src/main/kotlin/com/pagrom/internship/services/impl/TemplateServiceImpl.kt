package com.pagrom.internship.services.impl

import com.pagrom.internship.entities.Template
import com.pagrom.internship.entities.TemplateDTO
import com.pagrom.internship.repositories.TemplateRepository
import com.pagrom.internship.services.TemplateService
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service
import javax.persistence.EntityNotFoundException

@Service
class TemplateServiceImpl(
    private val templateRepository: TemplateRepository
) : TemplateService {

    override fun create(templateDTO: TemplateDTO): Template {
        // Return template if exist
        val templateOptional = templateRepository.findById(templateDTO.templateId)
        if (templateOptional.isPresent) {
            return templateOptional.get()
        }

        // Create new template
        val validation: MutableMap<String, String> = mutableMapOf()
        if (templateDTO.validation != null) {
            for (valid in templateDTO.validation) {
                for ((name, value) in valid) {
                    validation[name] = value
                }
            }
        }

        val template = Template(
            templateDTO.templateId,
            templateDTO.template,
            templateDTO.recipients,
            validation
        )

        return templateRepository.save(template)
    }

    override fun list(pageable: Pageable): Page<Template> {
        return templateRepository.findAll(pageable)
    }

    override fun findByTemplateId(templateId: String): Template {
        val messageTemplate = templateRepository.findById(templateId)

        if (!messageTemplate.isPresent) {
            throw EntityNotFoundException("Template not found")
        }

        return messageTemplate.get()
    }
}