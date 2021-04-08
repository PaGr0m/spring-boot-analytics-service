package com.pagrom.internship.entities

import kotlinx.serialization.Serializable

@Serializable
data class TemplateDTO(
    val templateId: String,
    val template: String,
    val recipients: List<String> = mutableListOf(),
    val validation: List<Map<String, String>>? = null
)

