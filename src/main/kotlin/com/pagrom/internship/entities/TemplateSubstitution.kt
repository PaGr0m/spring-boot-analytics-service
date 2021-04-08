package com.pagrom.internship.entities

import kotlinx.serialization.Serializable

@Serializable
data class TemplateSubstitution(
    val templateId: String,
    val variables: List<Map<String, String>>,
)