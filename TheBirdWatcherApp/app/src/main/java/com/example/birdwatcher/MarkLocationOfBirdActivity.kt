package com.example.birdwatcher

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.location.*
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.widget.Toast
import android.widget.Toolbar
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import java.text.SimpleDateFormat
import java.util.*




class MarkBirdLocationActivity : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var mMap: GoogleMap
    private var locationManager: LocationManager? = null
    private var locationListener: LocationListener? = null
//    private var userLocation: LatLng? = null
    private lateinit var newLatLng: LatLng
    private var address: String = ""


    fun centerMapOnLocation(location: Location?, title: String?) {

        if (intent.getStringExtra("latLng").isNullOrEmpty()) {
            if (location != null) {
                val userLocation = LatLng(location.latitude, location.longitude)
                Log.i("userLocation", "$userLocation")

                mMap.addMarker(MarkerOptions().position(userLocation).title(title))
                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(userLocation, 10f))
            }
        } else {
            val latLngArray = intent.getStringExtra("latLng")!!.split(" ", ",")

            Log.i("latlngarr", "$latLngArray")

            val latitude = latLngArray[1].substring(1, latLngArray[1].length).toDouble()
            val longitude = latLngArray[2].substring(0, latLngArray[2].length - 1).toDouble()

            Log.i("latitude", "$latitude")
            Log.i("longitude", "$longitude")

            val location = LatLng(latitude, longitude)
            val address = intent.getStringExtra("address")

            Toast.makeText(this, location.toString(), Toast.LENGTH_SHORT).show()

            mMap.addMarker(MarkerOptions().position(location).title(address))
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(location, 10f))

        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String?>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == 1) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                if (ContextCompat.checkSelfPermission(
                        this,
                        Manifest.permission.ACCESS_FINE_LOCATION
                    ) != PackageManager.PERMISSION_GRANTED
                ) {
                    locationListener?.let {
                        locationManager!!.requestLocationUpdates(
                            LocationManager.GPS_PROVIDER,
                            0,
                            0f,
                            it
                        )
                    }
                }
            }
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId === android.R.id.home) {

            val resultIntent = Intent(this, PromptFormActivity::class.java)

            resultIntent.putExtra("latLng", newLatLng.toString())
            resultIntent.putExtra("address", address)

            Log.i("LATLNG", "onOptionsItemSelected: $address")
            Log.i("LATLNG", "onOptionsItemSelected: $newLatLng")
            setResult(RESULT_OK, resultIntent)
            finish()
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.marklocationofbird)

        // Setting up Toolbar
//        setSupportActionBar(findViewById(com.example.birdwatcher.R.id.toolBar))

        supportActionBar?.title = ""
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.frag_map) as SupportMapFragment
        mapFragment.getMapAsync(this)
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */

    private fun setMapLongClick(map: GoogleMap) {
        map.setOnMapLongClickListener { latLng ->

            val geocoder = Geocoder(applicationContext, Locale.getDefault())
            address = ""

            try {
                val listAddresses: List<Address> =
                    geocoder.getFromLocation(latLng.latitude, latLng.longitude, 1)

                if (listAddresses.isNotEmpty()) {
                    if (listAddresses[0].thoroughfare != null) {
                        if (listAddresses[0].subThoroughfare != null) {
                            address += listAddresses[0].subThoroughfare + " "
                        }

                        address += listAddresses[0].thoroughfare
                    }
                }
                //       var sharedPreferences = getSharedPreferences()
            } catch (e: Exception) {
                e.printStackTrace()
            }

            if (address == "") {
                val sdf = SimpleDateFormat(" HH:mm dd-MM-yyyy")
                address += sdf.format(Date())
            }

            map.clear()
            map.addMarker(MarkerOptions().position(latLng).title(address))
            newLatLng = latLng
        }
    }

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap

        // Zoom in on user location
        locationManager = getSystemService(LOCATION_SERVICE) as LocationManager
        locationListener =
            object : LocationListener {
                override fun onLocationChanged(location: Location) {
                    centerMapOnLocation(location, "Your location.")
                }

                override fun onStatusChanged(provider: String?, status: Int, extras: Bundle?) {}
//                override fun onProviderEnabled(provider: String?) {}
//                override fun onProviderDisabled(provider: String?) {}
            }

        if (ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            locationManager!!.requestLocationUpdates(
                LocationManager.GPS_PROVIDER,
                0, 10f, locationListener as LocationListener
            )
            val lastKnownLocation =
                locationManager!!.getLastKnownLocation(LocationManager.GPS_PROVIDER)
            centerMapOnLocation(lastKnownLocation, "Your Location")
        } else {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                1
            )
        }
        setMapLongClick(mMap)
    }
}