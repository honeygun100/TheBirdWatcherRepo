package com.example.birdwatcher

import android.database.Cursor
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.ListView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import java.util.*

class NotificationActivity : AppCompatActivity() {

    private var nList = ArrayList<String>()
    private val nHandler = NotifHelper(this, null)
    //private val nHandlerm = NotifHelper(null, null)
    private lateinit var textView: TextView
    private lateinit var listView: ListView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(com.example.birdwatcher.R.layout.fragment_notifications)
        textView = findViewById(R.id.text_notifications)
        listView = findViewById(R.id.list)

    }


    fun loadIntoList(orderBy: String) {
        nList.clear()
        val cursor: Cursor? = nHandler.getAllRow(orderBy)
        cursor!!.moveToFirst()

        while (!cursor.isAfterLast) {


            val text = cursor.getString(1)

            nList.add(text)
            cursor.moveToNext()
        }

        val sortNames = resources.getStringArray(R.array.sort_types)

        listView.adapter = ArrayAdapter(this, android.R.layout.simple_list_item_activated_1,
            sortNames
        )





    }}
