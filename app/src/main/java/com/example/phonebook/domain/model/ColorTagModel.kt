package com.example.phonebook.domain.model

import com.example.phonebook.database.ColorTagDbModel

data class ColorTagModel(
    val id: Long,
    val name: String,
    val hex: String
) {
    companion object {
        val DEFAULT = with(ColorTagDbModel.DEFAULT_COLOR_TAG) { ColorTagModel(id, name, hex) }
    }
}
