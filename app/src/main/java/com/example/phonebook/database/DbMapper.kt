package com.example.phonebook.database

import com.example.phonebook.domain.model.ColorTagModel
import com.example.phonebook.domain.model.NEW_CONTACT_ID
import com.example.phonebook.domain.model.ContactModel

class DbMapper {
    // Create list of ContactModels by pairing each note with a color
    fun mapContacts(
        contactDbModels: List<ContactDbModel>,
        colorTagDbModels: Map<Long, ColorTagDbModel>
    ): List<ContactModel> = contactDbModels.map {
        val colorTagDbModel = colorTagDbModels[it.colorTagId]
            ?: throw RuntimeException("Color Tag for colorTagId: ${it.colorTagId} was not found. Make sure that all color tags are passed to this method")

        mapContact(it, colorTagDbModel)
    }

    // convert ContactDbModel to ContactModel
    fun mapContact(contactDbModel: ContactDbModel, colorTagDbModel: ColorTagDbModel): ContactModel {
        val colorTag = mapColorTag(colorTagDbModel)
        val isCheckedOff = with(contactDbModel) { if (canBeCheckedOff) isCheckedOff else null }
        return with(contactDbModel) { ContactModel(id, name, number, isCheckedOff, colorTag) }
    }

    // convert list of ColorTagDdModels to list of ColorTagModels
    fun mapColorTags(colorTagDbModels: List<ColorTagDbModel>): List<ColorTagModel> =
        colorTagDbModels.map { mapColorTag(it) }

    // convert ColorTagDbModel to ColorTagModel
    fun mapColorTag(colorTagDbModel: ColorTagDbModel): ColorTagModel =
        with(colorTagDbModel) { ColorTagModel(id, name, hex) }

    // convert ContactModel back to ContactDbModel
    fun mapDbContact(contact: ContactModel): ContactDbModel =
        with(contact) {
            val canBeCheckedOff = isCheckedOff != null
            val isCheckedOff = isCheckedOff ?: false
            if (id == NEW_CONTACT_ID)
                ContactDbModel(
                    name = name,
                    number = number,
                    canBeCheckedOff = canBeCheckedOff,
                    isCheckedOff = isCheckedOff,
                    colorTagId = colorTag.id,
                    isInTrash = false
                )
            else
                ContactDbModel(id, name, number, canBeCheckedOff, isCheckedOff, colorTag.id, false)
        }
}