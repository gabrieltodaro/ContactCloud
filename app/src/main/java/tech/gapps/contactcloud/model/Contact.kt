package tech.gapps.contactcloud.model

data class Contact(
    var fullName: String,
    var nickname: String,
    var phoneNumber: Long,
    var email: String
)