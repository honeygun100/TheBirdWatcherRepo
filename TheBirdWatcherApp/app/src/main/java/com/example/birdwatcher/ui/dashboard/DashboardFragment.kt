package com.example.birdwatcher.ui.dashboard

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentContainerView
import androidx.fragment.app.findFragment
import androidx.lifecycle.ViewModelProvider
import com.example.birdwatcher.R
import com.example.birdwatcher.databinding.FragmentDashboardBinding
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.Dash
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions

class DashboardFragment : Fragment() {

    //text_dashboard is our map

    private var _binding: FragmentDashboardBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    private val callback = OnMapReadyCallback { googleMap ->
        val sydney = LatLng(-34.0, 151.0)
        googleMap.addMarker(MarkerOptions().position(sydney).title("Marker in Sydney"))
        googleMap.moveCamera(CameraUpdateFactory.newLatLng(sydney))
    }



    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val dashboardViewModel =
            ViewModelProvider(this).get(DashboardViewModel::class.java)

        _binding = FragmentDashboardBinding.inflate(inflater, container, false)
        val root: View = binding.root

        val textView: FragmentContainerView = binding.textDashboard
        dashboardViewModel.text.observe(viewLifecycleOwner) {
//            textView.getFragment<DashboardFragment as Fragment>() = it
            textView.findFragment<DashboardFragment>();
        }

        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val mapFragment = childFragmentManager.findFragmentById(R.id.text_dashboard) as SupportMapFragment?
        mapFragment?.getMapAsync(callback)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}

//package com.example.birdwatcher.ui.dashboard
//
//import android.os.Bundle
//import androidx.fragment.app.FragmentActivity
//import com.example.birdwatcher.databinding.FragmentDashboardBinding
//import com.google.android.gms.maps.CameraUpdateFactory
//import com.google.android.gms.maps.GoogleMap
//import com.google.android.gms.maps.OnMapReadyCallback
//import com.google.android.gms.maps.SupportMapFragment
//import com.google.android.gms.maps.model.LatLng
//import com.google.android.gms.maps.model.MarkerOptions
//
//class DashboardFragment : FragmentActivity(), OnMapReadyCallback {
//
//    private var _binding: FragmentDashboardBinding? = null
//
//    // This property is only valid between onCreateView and
//    // onDestroyView.
//    private val binding get() = _binding!!
//
//    public override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//
//        setContentView(binding.root)
//
//        // The GoogleMap instance underlying the GoogleMapFragment defined in main.xml
//        val map = supportFragmentManager.findFragmentById(com.example.birdwatcher.R.id.text_dashboard) as SupportMapFragment
//        map.getMapAsync(this)
//        //T
//
//    }
//
//    override fun onMapReady(map: GoogleMap?) {
//        if (map != null) {
//
//            // Set the map position
//            map.moveCamera(
//                CameraUpdateFactory.newLatLngZoom(
//                    LatLng(
//                        29.0,
//                        -88.0
//                    ), 3.0f
//                )
//            )
//
//            // Add a marker on Washington, DC, USA
//            map.addMarker(
//                MarkerOptions().position(
//                    LatLng(38.8895, -77.0352)
//                ).title("dc")
//            )
//
//            // Add a marker on Mexico City, Mexico
//            map.addMarker(
//                MarkerOptions().position(
//                    LatLng(19.13, -99.4)
//                ).title("mexico")
//            )
//        }
//
//    }
//}

