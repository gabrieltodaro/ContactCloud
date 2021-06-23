package tech.gapps.contactcloud.model

import java.io.Serializable
import kotlin.random.Random

data class Contact(
        val id: Long = Random.nextLong(),
        var fullName: String,
        var nickname: String,
        var phoneNumber: Long,
        var email: String,
        var imageUrl: String
) : Serializable