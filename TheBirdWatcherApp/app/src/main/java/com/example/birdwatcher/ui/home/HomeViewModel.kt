package com.example.birdwatcher.ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class HomeViewModel : ViewModel() {

    private val _text = MutableLiveData<String>().apply {
        value = "This is home Fragment, there will be two options here to add a pin or remove a pin of a bird sighting"
    }
    val text: LiveData<String> = _text
}