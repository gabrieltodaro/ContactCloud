package tech.gapps.contactcloud.model

data class Contact(
        val id: Long,
        var fullName: String,
        var nickname: String,
        var phoneNumber: Long,
        var email: String,
        var imageUrl: String
)