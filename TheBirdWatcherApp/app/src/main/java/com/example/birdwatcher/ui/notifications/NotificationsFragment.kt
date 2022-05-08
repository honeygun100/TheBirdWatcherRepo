package com.example.birdwatcher.ui.notifications

import android.R
import android.content.Intent
import android.database.Cursor
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.core.content.ContentProviderCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.ListFragment
import androidx.lifecycle.ViewModelProvider
import com.example.birdwatcher.databinding.FragmentNotificationsBinding
import androidx.core.text.HtmlCompat;
import androidx.core.view.get
import com.example.birdwatcher.NotifHelper
import com.example.birdwatcher.NotificationActivity
import java.util.ArrayList

class NotificationsFragment : ListFragment() {

    private var _binding: FragmentNotificationsBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!


    private val nHandler = NotifHelper(context, null)
    private var nList = ArrayList<String>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        //val factory = NotificationsViewModelFactory()
        val notificationsViewModel =
            ViewModelProvider(this).get(NotificationsViewModel::class.java)

        _binding = FragmentNotificationsBinding.inflate(inflater, container, false)
        val root: View = binding.root


        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
