package com.pagrom.internship.entities

data class MessageSender(
    val templateId: String,
    val variables: List<Map<String, String>>,
)