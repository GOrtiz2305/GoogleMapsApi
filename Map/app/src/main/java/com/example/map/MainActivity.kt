package com.example.map

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions

class MainActivity : AppCompatActivity(), OnMapReadyCallback,
    GoogleMap.OnMarkerClickListener {

    private lateinit var map: GoogleMap
    private lateinit var latitudeInput: EditText
    private lateinit var longitudeInput: EditText
    private lateinit var addMarkerButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)

        val mapFragment = supportFragmentManager.findFragmentById(R.id.id_map) as? SupportMapFragment
        latitudeInput = findViewById(R.id.latitude_input)
        longitudeInput = findViewById(R.id.longitude_input)
        addMarkerButton = findViewById(R.id.add_marker_button)

        mapFragment?.getMapAsync(this)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        addMarkerButton.setOnClickListener {
            placeMarkerFromInput()
        }
    }

    override fun onMapReady(googleMap: GoogleMap) {
        map = googleMap
        map.setOnMarkerClickListener(this)

        // Set initial location
        val latLng = LatLng(14.8446, -91.5232)
        map.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 19f))
    }

    override fun onMarkerClick(marker: Marker): Boolean {
        // Remove the clicked marker from the map
        marker.remove()
        return true // Consume the click event
    }

    private fun placeMarkerFromInput() {
        val latitudeText = latitudeInput.text.toString().trim()
        val longitudeText = longitudeInput.text.toString().trim()

        if (latitudeText.isEmpty() || longitudeText.isEmpty()) {
            Toast.makeText(this, "Please enter both latitude and longitude values", Toast.LENGTH_SHORT).show()
            return
        }

        try {
            val latitude = latitudeText.toDouble()
            val longitude = longitudeText.toDouble()
            val markerLatLng = LatLng(latitude, longitude)

            // Add marker to the map
            map.addMarker(MarkerOptions().position(markerLatLng))

            // Move camera to the marker location
            map.moveCamera(CameraUpdateFactory.newLatLng(markerLatLng))
        } catch (e: NumberFormatException) {
            Toast.makeText(this, "Invalid latitude or longitude format", Toast.LENGTH_SHORT).show()
        }
    }
}