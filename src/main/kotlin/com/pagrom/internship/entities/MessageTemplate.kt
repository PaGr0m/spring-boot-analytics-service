package com.pagrom.internship.entities

import kotlinx.serialization.Serializable
import javax.persistence.Column
import javax.persistence.ElementCollection
import javax.persistence.Entity
import javax.persistence.Id

@Serializable
@Entity
data class MessageTemplate(
    @Id
    @Column(nullable = false)
    val templateId: String = "",

    @Column(nullable = false)
    val template: String = "",

    @ElementCollection
    @Column(nullable = false)
    val recipients: List<String> = mutableListOf()
)