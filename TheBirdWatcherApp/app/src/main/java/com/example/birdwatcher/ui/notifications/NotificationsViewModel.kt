package com.example.birdwatcher.ui.notifications

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.birdwatcher.NotifHelper

class NotificationsViewModel : ViewModel() {

    // private val nHandler = NotifHelper(, null)

    val _text = MutableLiveData<MutableList<String>>().apply {
        //default notifications
        value = mutableListOf("welcome to bird watching")
    }
    var text: MutableLiveData<MutableList<String>> = _text


    //add notification(s) in arraylist form
    fun addnotif(n: ArrayList<String>) {
        _text.value = n.toMutableList()
        text = _text
    }

}