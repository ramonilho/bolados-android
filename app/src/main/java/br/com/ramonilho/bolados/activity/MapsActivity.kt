package br.com.ramonilho.bolados.activity

import android.support.v4.app.FragmentActivity
import android.os.Bundle

import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions

import br.com.ramonilho.bolados.R

class MapsActivity : FragmentActivity(), OnMapReadyCallback {

    private var mMap: GoogleMap? = null
    var location: LatLng? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_maps)

        // Default location: Av Paulista
        val latitude = intent.extras.getDouble("latitude", -23.5644)
        val longitude = intent.extras.getDouble("longitude", -46.6526)
        location = LatLng(latitude, longitude)

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        val mapFragment = supportFragmentManager
                .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
    }

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap

        val storeName = intent.extras.getString("storeName", "Store")

        // Add a marker in store location and move the camera
        mMap!!.addMarker(MarkerOptions().position(location!!).title(storeName))
        mMap!!.moveCamera(CameraUpdateFactory.newLatLng(location!!))
        mMap!!.animateCamera(CameraUpdateFactory.zoomTo(16F), 2000, null)
    }
}
