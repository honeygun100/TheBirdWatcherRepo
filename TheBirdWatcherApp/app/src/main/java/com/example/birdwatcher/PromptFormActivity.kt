package com.example.birdwatcher

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.MenuItem
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.drawToBitmap
import com.example.birdwatcher.Utils.Utils
import java.time.LocalDateTime


class PromptFormActivity : AppCompatActivity() {
    private val dbHandler = DBHelper(this, null)
    private val nHandler = NotifHelper(this, null)
    private lateinit var nameEditText: EditText
    private lateinit var notesEditText: EditText
    private lateinit var uploadImageButton: Button
    private lateinit var uploadImageView: ImageView
    private lateinit var raritySpinner: Spinner
    private lateinit var modifyId: String
    private var latLng: String = ""
    private var address: String = ""

    private var rarityTypes = mapOf(
        Pair("Common", 0),
        Pair("Rare", 1),
        Pair("Extremely Rare", 2),
        Pair("Un-Documented", 3)

    )


    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) finish()
        return super.onOptionsItemSelected(item)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.promptform)

        uploadImageButton = findViewById(R.id.uploadImageButton)
        uploadImageView = findViewById(R.id.uploadImageView)

        setSupportActionBar(findViewById(R.id.toolBar))

        supportActionBar?.title = ""
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)

        nameEditText = findViewById(R.id.nameEditText)
        notesEditText = findViewById(R.id.notesEditText)
        raritySpinner = findViewById(R.id.raritySpinner)

        raritySpinner.adapter =
            ArrayAdapter(
                applicationContext,
                android.R.layout.simple_list_item_1, ArrayList(rarityTypes.keys)
            )

        raritySpinner.setSelection(rarityTypes["Common"]!!)


        /* Check if activity opened from List Item Click */
        if (intent.hasExtra("id")) {
            modifyId = intent.getStringExtra("id").toString()
            nameEditText.setText(intent.getStringExtra("name"))
            notesEditText.setText(intent.getStringExtra("notes"))
            raritySpinner.setSelection(rarityTypes[intent.getStringExtra("rarity")]!!)

            latLng = intent.getStringExtra("latLng")!!
            address = intent.getStringExtra("address")!!
            uploadImageView.setImageBitmap(Utils.getBitmapFromMemCache(intent.getStringExtra("id").toString()))

            findViewById<Button>(R.id.btnAdd).visibility = View.GONE

        } else {
            findViewById<Button>(R.id.btnUpdate).visibility = View.GONE
            findViewById<Button>(R.id.btnDelete).visibility = View.GONE
        }

        uploadImageButton.setOnClickListener {
            if (checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE), 1)
            } else {
                getPhoto()
            }
        }
    }

    fun onSetMarker(view: View) {
        val intent = Intent(applicationContext, MarkBirdLocationActivity::class.java)
        intent.putExtra("address", address)
        intent.putExtra("latLng", latLng)

        startActivityForResult(intent, 1)
    }

    private fun getPhoto() {
        val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        startActivityForResult(intent, 1)
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        if (requestCode == 1 && grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            getPhoto()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        val selectedImage: Uri? = data?.data

        if (requestCode == 1 && resultCode == RESULT_OK && data != null && selectedImage != null) {
            try {
                val bitMap: Bitmap =
                    MediaStore.Images.Media.getBitmap(this.contentResolver, selectedImage)

                uploadImageView.setImageBitmap(bitMap)
                Utils.addBitmapToMemoryCache(intent.getStringExtra("id").toString(), bitMap)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }

        if (requestCode == 1 && resultCode == RESULT_OK && data!!.hasExtra("latLng")) {
            latLng = data.getStringExtra("latLng")!!
            address = data.getStringExtra("address")!!
        }
    }

    fun add(v: View) {
        val name = nameEditText.text.toString()
        val notes = notesEditText.text.toString()
        val rarity = rarityTypes[raritySpinner.selectedItem]
        val image = uploadImageView

        dbHandler.insertRow(
            name,
            rarity.toString(),
            notes,
            Utils.getBytes(image.drawToBitmap()),
            latLng,
            address
        )
        nHandler.insertRow(
            LocalDateTime.now().toString() + ": You added "+name+"."
        )
        Log.i("latlng after add in prompt: ", "$latLng.toString()")

        Toast.makeText(this, "Bird Added!", Toast.LENGTH_SHORT).show()
        finish()
    }

    fun update(v: View) {
        val name = nameEditText.text.toString()
        val notes = notesEditText.text.toString()
        val rarity = rarityTypes[raritySpinner.selectedItem]
        val image = uploadImageView

        dbHandler.updateRow(
            modifyId,
            name,
            rarity.toString(),
            notes,
            Utils.getBytes(image.drawToBitmap()),
            latLng, address
        )

        Toast.makeText(this, "Bird Updated!", Toast.LENGTH_SHORT).show()
        finish()
    }

    fun delete(v: View) {
        dbHandler.deleteRow(modifyId)
        nHandler.insertRow(
            LocalDateTime.now().toString() + ": You deleted "+nameEditText.text.toString()+"."
        )
        Toast.makeText(this, "Bird deleted", Toast.LENGTH_SHORT).show()
        finish()
    }
}
