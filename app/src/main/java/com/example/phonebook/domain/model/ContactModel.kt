package com.example.phonebook.domain.model

const val NEW_CONTACT_ID = -1L

data class ContactModel(
    val id: Long = NEW_CONTACT_ID, // This value is used for new contacts
    val name: String = "",
    val number: String = "",
    val isCheckedOff: Boolean? = null, // null represents that the contact can't be checked off
    val colorTag: ColorTagModel = ColorTagModel.DEFAULT
)