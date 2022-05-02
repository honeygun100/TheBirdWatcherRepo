package com.example.birdwatcher.ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class HomeViewModel : ViewModel() {

    private val _text = MutableLiveData<String>().apply {
        value = "Use \"AddBird\" button to add a bird to data. Use \"Glossary\" button to update data of bird or delete bird."
    }
    val text: LiveData<String> = _text










}