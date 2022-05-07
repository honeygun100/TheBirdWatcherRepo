package com.example.birdwatcher.ui.notifications

import android.R
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

class NotificationsFragment : ListFragment() {

    private var _binding: FragmentNotificationsBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!


    private val nHandler = NotifHelper(context, null)
    private var nList = java.util.ArrayList<String>()

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


        nList.clear()
        /*val cursor: Cursor? = nHandler!!.getAllRow("name")
        cursor!!.moveToFirst()

        while (!cursor.isAfterLast) {


            val text = cursor.getString(1)

            nList.add(text)
            cursor.moveToNext()
        }*/

        Log.i("loading", nList.toString())

        val listView: ListView = binding.list
        notificationsViewModel.text.observe(viewLifecycleOwner) {
            listView.adapter =
                ArrayAdapter(requireActivity(), R.layout.simple_list_item_activated_1, nList)
        }



        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}