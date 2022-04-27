package com.example.birdwatcher

import android.content.Context
import android.graphics.BitmapFactory
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageView
import android.widget.TextView

class CustomAdapter(
    private val context: Context,
    private val birdList: ArrayList<Bird>,
    private val rarityTypes: Map<Int, String> = mapOf(
        Pair(0, "Common"),
        Pair(1, "Rare"),
        Pair(2, "Extremely rare")
    )

) : BaseAdapter() {
    private val inflater: LayoutInflater =
        this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater

    override fun getItem(position: Int): Any {
        return birdList[position]
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getCount(): Int {
        return birdList.size
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {

        Log.i("VIEW", convertView.toString())
        val dataitem = birdList[position]

        val rowView = inflater.inflate(R.layout.list_row, parent, false)

        rowView.findViewById<TextView>(R.id.row_name).text = dataitem.name
        rowView.findViewById<TextView>(R.id.row_rarity).text = "(${rarityTypes[dataitem.rarity]})"
        rowView.findViewById<TextView>(R.id.row_notes).text = dataitem.notes
        rowView.findViewById<TextView>(R.id.row_date).text = dataitem.date
        rowView.findViewById<TextView>(R.id.row_address).text = dataitem.address

        if (dataitem.image != null) {
            val bitmap = BitmapFactory.decodeByteArray(dataitem.image, 0, dataitem.image!!.size)

            rowView.findViewById<ImageView>(R.id.row_image).setImageBitmap(bitmap)
        }

        rowView.tag = position
        return rowView
    }
}