package com.example.birdwatcher

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.location.*
import android.os.Bundle
import android.view.MenuItem
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
    private lateinit var newLatLng: LatLng
    private var address: String = ""

    private fun centerMapOnLocation(location: Location?, title: String?) {
        //add user location on map
        if (intent.getStringExtra("latLng").isNullOrEmpty() && location != null) {
            val userLocation = LatLng(location.latitude, location.longitude)
            mMap.addMarker(MarkerOptions().position(userLocation).title(title))
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(userLocation, 10f))

        } else {
            try {
                //get user location on map
                val latLngStr = intent.getStringExtra("latLng")!!.split(" ", ",")
                val latitude = latLngStr[1].substring(1, latLngStr[1].length).toDouble()
                val longitude = latLngStr[2].substring(0, latLngStr[2].length - 1).toDouble()

                val newLocation = LatLng(latitude, longitude)
                val address = intent.getStringExtra("address")

                mMap.addMarker(MarkerOptions().position(newLocation).title(address))
                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(newLocation, 10f))
            }catch (e:java.lang.Exception){

            }
        }
    }


    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String?>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == 1
            && grantResults.isNotEmpty()
            && grantResults[0] == PackageManager.PERMISSION_GRANTED
            && (ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED)
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


    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            val intent = Intent(this, PromptFormActivity::class.java)
            intent.putExtra("latLng", newLatLng.toString())
            intent.putExtra("address", address)
            setResult(RESULT_OK, intent)
            finish()
        }
        return super.onOptionsItemSelected(item)
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.marklocationofbird)

        val mapFragment =
            supportFragmentManager.findFragmentById(R.id.frag_map) as SupportMapFragment

        mapFragment.getMapAsync(this)
    }


    private fun setMapLongClick(map: GoogleMap) {
        map.setOnMapLongClickListener { latLng ->
            val geocoder = Geocoder(applicationContext, Locale.getDefault())

            address = ""
            //extract the address to use as title for marker
            try {
                val addressList: List<Address> =
                    geocoder.getFromLocation(latLng.latitude, latLng.longitude, 1)
                if (addressList.isNotEmpty() && addressList[0].thoroughfare != null) {
                    if (addressList[0].subThoroughfare != null) {
                        address += addressList[0].subThoroughfare + " "
                    }
                    address += addressList[0].thoroughfare
                }

            } catch (e: Exception) {
                e.printStackTrace()
            }

            //add time next to address so it appears next to marker
            val simpleDateFormat = SimpleDateFormat(" HH:mm dd-MM-yyyy", Locale.US)
            address += simpleDateFormat.format(Date())

            map.addMarker(MarkerOptions().position(latLng).title(address))
            newLatLng = latLng
        }
    }


    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap

        //zoom in on the user's location
        locationManager = getSystemService(LOCATION_SERVICE) as LocationManager
        locationListener =
            LocationListener { location -> centerMapOnLocation(location, "Your location.") }

        if (ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            locationManager!!.requestLocationUpdates(
                LocationManager.GPS_PROVIDER,
                0, 10f, locationListener as LocationListener
            )

            //use user's last known location if need be
            val lastLocation = locationManager!!.getLastKnownLocation(LocationManager.GPS_PROVIDER)
            centerMapOnLocation(lastLocation, "Your Location")

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