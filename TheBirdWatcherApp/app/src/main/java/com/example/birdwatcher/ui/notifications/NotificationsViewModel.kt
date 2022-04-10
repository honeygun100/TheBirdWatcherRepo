package com.example.birdwatcher.ui.notifications

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class NotificationsViewModel : ViewModel() {

    private val _text = MutableLiveData<String>().apply {
        value = "This is notifications Fragment, this will notify users when a new pin has been made in the users radius"
    }
    val text: LiveData<String> = _text
}