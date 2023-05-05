package com.example.phonebook.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class ContactDbModel(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    @ColumnInfo(name = "name") val name: String,
    @ColumnInfo(name = "number") val number: String,
    @ColumnInfo(name = "can_be_checked_off") val canBeCheckedOff: Boolean,
    @ColumnInfo(name = "is_checked_off") val isCheckedOff: Boolean,
    @ColumnInfo(name = "color_id") val colorTagId: Long,
    @ColumnInfo(name = "in_trash") val isInTrash: Boolean
) {
    companion object {
        val DEFAULT_CONTACTS = listOf(
            ContactDbModel(1, "Bobby", "0123456789", false, false, 1, false),
            ContactDbModel(2, "Mom", "0887896541", false, false, 1, false),
            ContactDbModel(3, "Home", "035478988", false, false, 2, false),
            ContactDbModel(4, "service", "0999987123", false, false, 5, false),
            ContactDbModel(5, "Rick Grimes", "0841255666", false, false, 3, false),
            ContactDbModel(6, "Alisson Becker", "0124789639", false, false, 1, false),
            ContactDbModel(7, "Police", "191", false, false, 4, false),
            )
    }
}
