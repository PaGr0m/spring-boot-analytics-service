package com.pagrom.internship.repositories

import com.pagrom.internship.entities.Template
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface TemplateRepository : JpaRepository<Template, String>