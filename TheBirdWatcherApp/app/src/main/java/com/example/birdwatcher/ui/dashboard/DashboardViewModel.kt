package com.example.birdwatcher.ui.dashboard

import androidx.fragment.app.FragmentContainerView
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.android.gms.maps.GoogleMap

class DashboardViewModel : ViewModel() {

    private val _text = MutableLiveData<String>().apply {
//        value = "This is dashboard Fragment, here users can see pins on a map made by other users
    }
    val text: LiveData<String> = _text
}