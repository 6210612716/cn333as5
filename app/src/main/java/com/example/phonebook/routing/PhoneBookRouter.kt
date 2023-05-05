package com.example.phonebook.routing

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue

sealed class Screen {
    object PhoneBook: Screen()
    object ViewContact: Screen()
    object SaveContact: Screen()
    object Trash: Screen()
}

object PhoneBookRouter {
    var currentScreen: Screen by mutableStateOf(Screen.PhoneBook)

    fun navigateTo(destination: Screen) {
        currentScreen = destination
    }
}