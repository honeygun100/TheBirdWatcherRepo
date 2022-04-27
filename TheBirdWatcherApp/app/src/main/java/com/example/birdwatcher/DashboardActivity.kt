package com.example.birdwatcher

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.location.*
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.FragmentActivity
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.R
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import java.text.SimpleDateFormat
import java.util.*

class DashboardActivity : AppCompatActivity(), OnMapReadyCallback {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(com.example.birdwatcher.R.layout.marklocationofbird)

        // The GoogleMap instance underlying the GoogleMapFragment defined in main.xml
        val mapFragment = supportFragmentManager.findFragmentById(com.example.birdwatcher.R.id.frag_map) as SupportMapFragment
        mapFragment.getMapAsync(this)
    }

    override fun onMapReady(map: GoogleMap?) {
        if (map != null) {

            // Set the map position
            map.moveCamera(
                CameraUpdateFactory.newLatLngZoom(
                    LatLng(
                        29.0,
                        -88.0
                    ), 3.0f
                )
            )

            // Add a marker on Washington, DC, USA
//            map.addMarker(
//                MarkerOptions().position(
//                    LatLng(38.8895, -77.0352)
//                ).title("inwashington"))
//
//            // Add a marker on Mexico City, Mexico
//            map.addMarker(
//                MarkerOptions().position(
//                    LatLng(19.13, -99.4)
//                ).title("inmexico"))
        }

    }
}
