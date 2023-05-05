package com.example.phonebook.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class ColorTagDbModel(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    @ColumnInfo(name = "hex") val hex: String,
    @ColumnInfo(name = "name") val name: String
) {
    companion object {
        val DEFAULT_COLOR_TAGS = listOf(
            ColorTagDbModel(1, "#9E9E9E", "Mobile"),
            ColorTagDbModel(2, "#00ACC1", "Home"),
            ColorTagDbModel(3, "#4CAF50", "Work"),
            ColorTagDbModel(4, "#FF9800", "Emergency"),
            ColorTagDbModel(5, "#78453A", "Other"),
        )
        val DEFAULT_COLOR_TAG = DEFAULT_COLOR_TAGS[0]
    }
}
