package com.example.phonebook.ui.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.phonebook.domain.model.ContactModel
import com.example.phonebook.util.fromHex

@ExperimentalMaterialApi
@Composable
fun Contact(
    modifier: Modifier = Modifier,
    contact: ContactModel,
    onContactClick: (ContactModel) -> Unit = {},
    onContactCheckedChange: (ContactModel) -> Unit = {},
    isSelected: Boolean
) {
    val background = if (isSelected)
        Color.LightGray
    else
        MaterialTheme.colors.surface

    Card(
        shape = RoundedCornerShape(4.dp),
        modifier = modifier
            .padding(8.dp)
            .fillMaxWidth(),
        backgroundColor = background
    ) {
        ListItem(
            text = { Text(text = contact.name, maxLines = 1) },
            secondaryText = {
                Text(text = contact.number, maxLines = 1)
            },
            icon = {
                ContactColorTag(
                    colorTag = Color.fromHex(contact.colorTag.hex),
                    name = contact.name,
                    size = 40.dp,
                    border = 3.dp,
                )
            },
            trailing = {
                if (contact.isCheckedOff != null) {
                    Checkbox(
                        checked = contact.isCheckedOff,
                        onCheckedChange = { isChecked ->
                            val newContact = contact.copy(isCheckedOff = isChecked)
                            onContactCheckedChange.invoke(newContact)
                        },
                        modifier = Modifier.padding(start = 8.dp)
                    )
                }
            },
            modifier = Modifier.clickable {
                onContactClick.invoke(contact)
            }
        )
    }
}

@ExperimentalMaterialApi
@Preview
@Composable
private fun ContactPreview() {
    Contact(contact = ContactModel(1, "Contact 1", "Number", false), isSelected = true)
}