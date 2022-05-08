package com.example.birdwatcher.ui.notifications

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.birdwatcher.NotifHelper

class NotificationsViewModel : ViewModel() {


    val _text = MutableLiveData<MutableList<String>>().apply {
        value = mutableListOf("Welcome to bird watching")
    }
    var text: MutableLiveData<MutableList<String>> = _text

}
