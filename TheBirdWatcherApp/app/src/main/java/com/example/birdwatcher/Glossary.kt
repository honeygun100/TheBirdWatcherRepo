package com.example.birdwatcher

import android.content.Intent
import android.database.Cursor
import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.example.birdwatcher.Utils.Utils


class Glossary : AppCompatActivity() {

    private val dbHandler = DBHelper(this, null)
    private var birdList = ArrayList<Bird>()
    private lateinit var spinner: Spinner
    private lateinit var textView: TextView
    private var customAdapter: CustomAdapter? = null
    private lateinit var listView: ListView

    private var rarityTypes = mapOf(Pair(0, "Common"), Pair(1, "Rare"), Pair(2, "Extremely rare"))


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.glossary)

        textView = findViewById(R.id.textView)
        spinner = findViewById(R.id.sortSpinner)
        listView = findViewById(R.id.listView)

        val sortNames = resources.getStringArray(R.array.sort_types)

        val myAdapter = ArrayAdapter<String>(
            applicationContext,
            android.R.layout.simple_list_item_1,
            sortNames
        )

        myAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinner.adapter = myAdapter

        spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) {

            }

            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {

                when (sortNames[position]) {
                    "SORT BY NAME" -> loadIntoList("ORDER BY LENGTH(name) DESC")
                    "SORT BY RARITY" -> loadIntoList("ORDER BY rarity DESC")
                    "SORT BY NOTES" -> loadIntoList("ORDER BY LENGTH(notes) DESC")
                    "SORT BY DATE" -> loadIntoList("ORDER BY date DESC")
                }
                Toast.makeText(
                    this@Glossary,
                    "" + sortNames[position], Toast.LENGTH_SHORT
                ).show()

            }
        }

    }


    fun loadIntoList(orderBy: String) {
        birdList.clear()
        val cursor: Cursor? = dbHandler.getAllRow(orderBy)
        cursor!!.moveToFirst()

        while (!cursor.isAfterLast) {

            val id = cursor.getString(0)
            val name = cursor.getString(1)
            val rarity = cursor.getInt(2)
            val notes = cursor.getString(3)
            val image = cursor.getBlob(4)
            val latLng = cursor.getString(5)
            val address = cursor.getString(6)
            val date = cursor.getString(7)

            birdList.add(Bird(id, name, rarity, notes, image, latLng, address, date))
            cursor.moveToNext()
        }

        customAdapter?.notifyDataSetChanged()

        if (birdList.count() == 0) {
            textView.text = "List of birds you have added will show here!"
        } else {
            textView.visibility = View.GONE

            customAdapter = CustomAdapter(this@Glossary, birdList)
            listView.adapter = customAdapter

            findViewById<ListView>(R.id.listView).setOnItemClickListener { _, _, i, _ ->
                val intent = Intent(this, PromptFormActivity::class.java)

                intent.putExtra("id", birdList[+i].id)
                intent.putExtra("name", birdList[+i].name)
                intent.putExtra("notes", birdList[+i].notes)
                intent.putExtra("rarity", rarityTypes[birdList[+i].rarity])
                intent.putExtra("latLng", birdList[+i].latLng)
                intent.putExtra("address", birdList[+i].address)

                Utils.addBitmapToMemoryCache(birdList[+i].id, Utils.getImage(birdList[+i].image))

                startActivity(intent)
            }
        }
    }

    public override fun onResume() {
        super.onResume()
        loadIntoList("ORDER BY date DESC")
    }
}