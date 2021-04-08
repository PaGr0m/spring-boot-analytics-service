package com.pagrom.internship.services

import com.pagrom.internship.entities.Template
import com.pagrom.internship.entities.TemplateDTO
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable

interface TemplateService {
    fun create(templateDTO: TemplateDTO): Template

    fun list(pageable: Pageable): Page<Template>

    fun findByTemplateId(templateId: String): Template
}