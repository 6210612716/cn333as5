package com.example.phonebook.screens

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.List
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.phonebook.domain.model.ContactModel
import com.example.phonebook.routing.Screen
import com.example.phonebook.ui.components.AppDrawer
import com.example.phonebook.ui.components.Contact
import com.example.phonebook.viewmodel.MainViewModel
import kotlinx.coroutines.launch

@ExperimentalMaterialApi
@Composable
fun PhoneBookScreen(viewModel: MainViewModel) {
    val contacts by viewModel.contactsNotInTrash.observeAsState(listOf())
    val scaffoldState: ScaffoldState = rememberScaffoldState()
    val coroutineScope = rememberCoroutineScope()

    Scaffold(
        scaffoldState = scaffoldState,
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "My Contacts",
                        color = MaterialTheme.colors.onPrimary
                    )
                },
                navigationIcon = {
                    IconButton(onClick = {
                        coroutineScope.launch { scaffoldState.drawerState.open() }
                    }) {
                        Icon(
                            imageVector = Icons.Filled.List,
                            contentDescription = "Drawer Button"
                        )
                    }
                }
            )
        },
        drawerContent = {
            AppDrawer(
                currentScreen = Screen.PhoneBook,
                closeDrawerAction = {
                    coroutineScope.launch {
                        scaffoldState.drawerState.close()
                    }
                }
            )
        },
        floatingActionButtonPosition = FabPosition.End,
        floatingActionButton = {
            FloatingActionButton(
                onClick = { viewModel.onCreateNewContactClick() },
                contentColor = MaterialTheme.colors.background,
                content = {
                    Icon(
                        imageVector = Icons.Filled.Add,
                        contentDescription = "Add Contact Button"
                    )
                }
            )
        }
    ) {
        if (contacts.isNotEmpty()) {
            ContactsList(
                contacts = contacts,
                onContactCheckedChange = {
                    viewModel.onContactCheckedChange(it)
                },
                onContactClick = { viewModel.onContactClick(it) }
            )
        }
    }
}

@ExperimentalMaterialApi
@Composable
private fun ContactsList(
    contacts: List<ContactModel>,
    onContactCheckedChange: (ContactModel) -> Unit,
    onContactClick: (ContactModel) -> Unit
) {
    val sortedContacts = contacts.sortedBy { it.name.trim() }
    LazyColumn(modifier = Modifier.padding(vertical = 10.dp, horizontal = 6.dp)) {
        items(count = sortedContacts.size) { contactIndex ->
            val contact = sortedContacts[contactIndex]
            Contact(
                contact = contact,
                onContactClick = onContactClick,
                onContactCheckedChange = onContactCheckedChange,
                isSelected = false
            )
        }
    }
}

@ExperimentalMaterialApi
@Preview
@Composable
private fun ContactsListPreview() {
    ContactsList(
        contacts = listOf(
            ContactModel(1, "Contact 1", "1111", null),
            ContactModel(2, "Contact 2", "2222", false),
            ContactModel(3, "Contact 3", "3333", true)
        ),
        onContactCheckedChange = {},
        onContactClick = {}
    )
}