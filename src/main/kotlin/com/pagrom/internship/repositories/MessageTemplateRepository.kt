package com.pagrom.internship.repositories

import com.pagrom.internship.entities.MessageTemplate
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface MessageTemplateRepository : JpaRepository<MessageTemplate, String>