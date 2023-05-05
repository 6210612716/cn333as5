package com.example.phonebook.screens

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Build
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.rounded.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.phonebook.routing.PhoneBookRouter
import com.example.phonebook.routing.Screen
import com.example.phonebook.viewmodel.MainViewModel
import com.example.phonebook.R
import com.example.phonebook.domain.model.ColorTagModel
import com.example.phonebook.domain.model.NEW_CONTACT_ID
import com.example.phonebook.domain.model.ContactModel
import com.example.phonebook.ui.components.BigContactColorTag
import com.example.phonebook.util.fromHex
import kotlinx.coroutines.launch

@ExperimentalMaterialApi
@Composable
fun SaveContactScreen(viewModel: MainViewModel) {
    val contactEntry by viewModel.contactEntry.observeAsState(ContactModel())

    val colorTags: List<ColorTagModel> by viewModel.colors.observeAsState(listOf())

    val bottomDrawerState = rememberBottomDrawerState(BottomDrawerValue.Closed)

    val coroutineScope = rememberCoroutineScope()

    val moveContactToTrashDialogShownState = rememberSaveable { mutableStateOf(false) }

    BackHandler {
        if (bottomDrawerState.isOpen) {
            coroutineScope.launch { bottomDrawerState.close() }
        } else {
            PhoneBookRouter.navigateTo(Screen.PhoneBook)
        }
    }

    Scaffold(
        topBar = {
            val isEditingMode: Boolean = contactEntry.id != NEW_CONTACT_ID
            SaveContactTopAppBar(
                isEditingMode = isEditingMode,
                onBackClick = {
                    if (isEditingMode) {
                        PhoneBookRouter.navigateTo(Screen.ViewContact)
                    } else {
                        PhoneBookRouter.navigateTo(Screen.PhoneBook)
                    }
                },
                onSaveContactClick = {
                    viewModel.saveContact(contactEntry)
                 },
                onOpenColorTagPickerClick = {
                    coroutineScope.launch { bottomDrawerState.open() }
                },
                onDeleteContactClick = {
                    moveContactToTrashDialogShownState.value = true
                }
            )
        }
    ) {
        BottomDrawer(
            drawerState = bottomDrawerState,
            drawerContent = {
                ColorTagPicker(
                    colorTags = colorTags,
                    onColorTagSelect = { color ->
                        viewModel.onContactEntryChange(contactEntry.copy(colorTag = color))
                    }
                )
            }
        ) {
            SaveContactContent(
                contact = contactEntry,
                onContactChange = { updateContactEntry ->
                    viewModel.onContactEntryChange(updateContactEntry)
                },
                onOpenColorTagPickerClick = {
                    coroutineScope.launch { bottomDrawerState.open() }
                },
            )
        }

        if (moveContactToTrashDialogShownState.value) {
            AlertDialog(
                onDismissRequest = {
                    moveContactToTrashDialogShownState.value = false
                },
                title = {
                    Text("Move contact to the trash?")
                },
                text = {
                    Text(
                        "Are you sure you want to " +
                                "move this contact to the trash?"
                    )
                },
                confirmButton = {
                    TextButton(onClick = {
                        viewModel.moveContactToTrash(contactEntry)
                    }) {
                        Text("Confirm")
                    }
                },
                dismissButton = {
                    TextButton(onClick = {
                        moveContactToTrashDialogShownState.value = false
                    }) {
                        Text("Dismiss")
                    }
                }
            )
        }
    }
}

@Composable
fun SaveContactTopAppBar(
    isEditingMode: Boolean,
    onBackClick: () -> Unit,
    onSaveContactClick: () -> Unit,
    onOpenColorTagPickerClick: () -> Unit,
    onDeleteContactClick: () -> Unit
) {
    TopAppBar(
        title = {
            if (isEditingMode) {
                Text(
                    text = "Edit Contact",
                    color = MaterialTheme.colors.onPrimary
                )
            } else {
                Text(
                    text = "Add New Contact",
                    color = MaterialTheme.colors.onPrimary
                )
            }
        },
        navigationIcon = {
            IconButton(onClick = onBackClick) {
                Icon(
                    imageVector = Icons.Rounded.ArrowBack,
                    contentDescription = "Back Button",
                    tint = MaterialTheme.colors.onPrimary
                )
            }
        },
        actions = {
            IconButton(
                onClick = onSaveContactClick
            ) {
                if (isEditingMode) {
                    Icon(
                        imageVector = Icons.Rounded.Check,
                        contentDescription = "Save Contact Button",
                        tint = MaterialTheme.colors.onPrimary
                    )
                } else {
                    Icon(
                        imageVector = Icons.Rounded.Add,
                        contentDescription = "Add New Contact Button",
                        tint = MaterialTheme.colors.onPrimary
                    )
                }
            }
            
            IconButton(onClick = onOpenColorTagPickerClick) {
                Icon(
                    painter = painterResource(id = R.drawable.label_tag),
                    modifier = Modifier.size(18.dp),
                    contentDescription = "Open Color Tag Picker Button",
                    tint = MaterialTheme.colors.onPrimary,

                )
            }

            if (isEditingMode) {
                IconButton(onClick = onDeleteContactClick) {
                    Icon(
                        imageVector = Icons.Rounded.Delete,
                        contentDescription = "Delete Contact Button",
                        tint = MaterialTheme.colors.onPrimary
                    )
                }
            }
        }
    )
}

@Composable
private fun SaveContactContent(
    contact: ContactModel,
    onContactChange: (ContactModel) -> Unit,
    onOpenColorTagPickerClick: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxHeight()
            .padding(16.dp),
//        verticalArrangement = Arrangement.Center,
//        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Spacer(modifier = Modifier.height(80.dp))

        NumberTextField(
            label = "Name",
            text = contact.name,
            onTextChange = { newName ->
                onContactChange.invoke(contact.copy(name = newName))
            },
            isNumber = false
        )

        NumberTextField(
            modifier = Modifier
                .heightIn(max = 240.dp)
                .padding(top = 16.dp),
            label = "Number",
            text = contact.number,
            onTextChange = { newContent ->
                onContactChange.invoke(contact.copy(number = newContent))
            },
            isNumber = true
        )

        PickedColorTag(colorTag = contact.colorTag, onOpenColorTagPickerClick)
    }
}

@Composable
private fun NumberTextField(
    modifier: Modifier = Modifier,
    label: String,
    text: String,
    onTextChange: (String) -> Unit,
    isNumber: Boolean
) {
    if (!isNumber) {
        TextField(
            value = text,
            onValueChange = onTextChange,
            label = { Text(label) },
            modifier = modifier
                .fillMaxWidth()
                .padding(horizontal = 8.dp),
            colors = TextFieldDefaults.textFieldColors(
                backgroundColor = MaterialTheme.colors.surface
            ),
            textStyle = TextStyle(fontSize = 20.sp),
            singleLine = true,
            maxLines = 1,
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Text,
                imeAction = ImeAction.Done
            )
        )
    } else {
        TextField(
            value = text,
            onValueChange = onTextChange,
            label = { Text(label) },
            modifier = modifier
                .fillMaxWidth()
                .padding(horizontal = 8.dp),
            colors = TextFieldDefaults.textFieldColors(
                backgroundColor = MaterialTheme.colors.surface
            ),
            textStyle = TextStyle(fontSize = 20.sp),
            singleLine = true,
            maxLines = 1,
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Phone,
                imeAction = ImeAction.Done
            )
        )
    }
}

@Composable
private fun ContactCheckOption(
    isChecked: Boolean,
    onCheckedChange: (Boolean) -> Unit
) {
    Row(
        Modifier
            .padding(8.dp)
            .padding(top = 16.dp)
    ) {
        Text(
            text = "Can contact be checked off?",
            modifier = Modifier.weight(1f)
        )
        Switch(
            checked = isChecked,
            onCheckedChange = onCheckedChange,
            modifier = Modifier.padding(start = 8.dp)
        )
    }
}

@Composable
private fun PickedColorTag(colorTag: ColorTagModel, onOpenColorTagPickerClick: () -> Unit) {
    Row(
        Modifier
            .padding(8.dp)
            .padding(top = 16.dp)
    ) {
        IconButton(onClick = onOpenColorTagPickerClick) {
            Icon(
                painter = painterResource(id = R.drawable.label_tag),
                modifier = Modifier.size(24.dp),
                contentDescription = "Open Color Tag Picker",
                tint = Color.fromHex(colorTag.hex)
            )
        }
        Text(
            text = colorTag.name,
            modifier = Modifier
                .weight(1f)
                .align(Alignment.CenterVertically),
            fontSize = 20.sp
        )
    }
}

@Composable
private fun ColorTagPicker(
    colorTags: List<ColorTagModel>,
    onColorTagSelect: (ColorTagModel) -> Unit
) {
    Column(modifier = Modifier.fillMaxWidth()) {
        Text(
            text = "Choose Tag",
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(8.dp)
        )

        Divider(color = MaterialTheme.colors.onSurface.copy(alpha = .2f))
        Spacer(modifier = Modifier.size(16.dp))

        LazyColumn(modifier = Modifier
            .fillMaxWidth()
            .padding(start = 16.dp, bottom = 8.dp)
        ) {
            items(colorTags.size) { itemIndex ->
                val color = colorTags[itemIndex]
                ColorTagItem(
                    colorTag = color,
                    onColorTagSelect = onColorTagSelect
                )
            }
        }
    }
}

@Composable
fun ColorTagItem(
    colorTag: ColorTagModel,
    onColorTagSelect: (ColorTagModel) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(
                onClick = {
                    onColorTagSelect(colorTag)
                }
            )
    ) {
        Icon(
            painter = painterResource(id = R.drawable.label_tag),
            modifier = Modifier.size(24.dp),
            contentDescription = "Color Tag",
            tint = Color.fromHex(colorTag.hex)
        )
        Text(
            text = colorTag.name,
            fontSize = 20.sp,
            modifier = Modifier
                .padding(start = 14.dp, bottom = 16.dp)
                .align(Alignment.CenterVertically)
        )
    }
}

@Preview
@Composable
fun ColorTagItemPreview() {
    ColorTagItem(ColorTagModel.DEFAULT) {}
}

@Preview
@Composable
fun ColorTagPickerPreview() {
    ColorTagPicker(
        colorTags = listOf(
            ColorTagModel.DEFAULT,
            ColorTagModel.DEFAULT,
            ColorTagModel.DEFAULT
        )
    ) { }
}
