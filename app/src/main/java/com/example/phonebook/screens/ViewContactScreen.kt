package com.example.phonebook.screens

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.phonebook.routing.PhoneBookRouter
import com.example.phonebook.routing.Screen
import com.example.phonebook.viewmodel.MainViewModel
import com.example.phonebook.domain.model.ColorTagModel
import com.example.phonebook.domain.model.ContactModel
import com.example.phonebook.ui.components.BigContactColorTag
import com.example.phonebook.util.fromHex
import kotlinx.coroutines.launch

@ExperimentalMaterialApi
@Composable
fun ViewContactScreen(viewModel: MainViewModel) {
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
            ViewContactTopAppBar(
                contact = contactEntry,
                onBackClick = { PhoneBookRouter.navigateTo(Screen.PhoneBook) },
                onEditContactClick = { PhoneBookRouter.navigateTo(Screen.SaveContact) },
                onDeleteContactClick = {
                    moveContactToTrashDialogShownState.value = true
                }
            )
        }
    ) {
        ViewContactContent(
            contact = contactEntry,
        )

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
fun ViewContactTopAppBar(
    contact: ContactModel,
    onBackClick: () -> Unit,
    onEditContactClick: () -> Unit,
    onDeleteContactClick: () -> Unit
) {
    TopAppBar(
        title = {
            Text(
                text = contact.name,
                color = MaterialTheme.colors.onPrimary
            )
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
            IconButton(onClick = onEditContactClick) {
                Icon(
                    imageVector = Icons.Rounded.Edit,
                    contentDescription = "Edit Contact Button",
                    tint = MaterialTheme.colors.onPrimary
                )
            }

            IconButton(onClick = onDeleteContactClick) {
                Icon(
                    imageVector = Icons.Rounded.Delete,
                    contentDescription = "Delete Contact Button",
                    tint = MaterialTheme.colors.onPrimary
                )
            }
        }
    )
}

@Composable
private fun ViewContactContent(
    contact: ContactModel,
) {
    Column(
        modifier = Modifier
            .fillMaxHeight()
            .padding(12.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        BigContactColorTag(
            colorTag = Color.fromHex(contact.colorTag.hex),
            name = contact.name.split(" ").joinToString("") { it.first().toString() },
            size = 120.dp,
            border = 2.dp,

        )

        Spacer(modifier = Modifier.height(30.dp))

        Text(
            text = contact.name,
            fontSize = 30.sp
        )

        Divider(
            color = MaterialTheme.colors.onSurface.copy(alpha = .2f),
            modifier = Modifier
                .padding(16.dp)
        )

        Text(
            modifier = Modifier
                .heightIn(max = 240.dp)
                .padding(10.dp),
            text = contact.number,
            fontSize = 28.sp
        )

        Text(
            text = "(" + contact.colorTag.name + ")",
            fontSize = 26.sp
        )

    }
}
