package com.task.codegamma

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import com.google.android.gms.common.api.Status
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.libraries.places.api.Places
import com.google.android.libraries.places.api.model.Place
import com.google.android.libraries.places.widget.AutocompleteSupportFragment
import com.google.android.libraries.places.widget.listener.PlaceSelectionListener
import com.task.codegamma.database.RecentPlace
import com.task.codegamma.model.MyLocation
import com.task.codegamma.respository.Repository
import com.task.codegamma.util.PermissionUtils
import com.task.codegamma.util.PermissionUtils.LOCATION_PERMISSION_REQUEST_CODE
import kotlinx.android.synthetic.main.activity_maps.*
import java.util.*


class MapsActivity : AppCompatActivity(), OnMapReadyCallback {

    private var mMap: GoogleMap? = null
    private var mRepository: Repository? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_maps)

        mRepository = Repository(application)

        initMap()
        initPlaces()

        recent_place_btn_layout.setOnClickListener {
            startActivityForResult(Intent(this,RecentHistoryActivity::class.java),
            Const.REDIRECT_LOCATION_REQUEST) }
    }


    private fun initMap(){
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)

    }

    private fun initPlaces(){

        val apiKey = getString(R.string.api_key)

        if (!Places.isInitialized()) {
            Places.initialize(applicationContext, apiKey)
        }


        val autocompleteFragment = supportFragmentManager
            .findFragmentById(R.id.autocomplete_fragment) as AutocompleteSupportFragment

        autocompleteFragment.setPlaceFields(Arrays.asList(Place.Field.ID, Place.Field.NAME,
            Place.Field.ADDRESS, Place.Field.LAT_LNG))

        autocompleteFragment.setOnPlaceSelectedListener(object:PlaceSelectionListener{
            override fun onPlaceSelected(place: Place) {

                val myLocation = MyLocation(
                    place.latLng!!.latitude,
                    place.latLng?.longitude!!
                )
                mRepository?.insertPlace(RecentPlace(place.id!!,place.name,place.address,myLocation))

                place.latLng?.let { updateMap(it) }

            }

            override fun onError(error: Status) {
                Log.e("ERROR", "An error occurred: " +error.statusMessage );
            }
        })
    }





    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap

        if (PermissionUtils.isAccessFineLocationGranted(this)){
            setUpLocationListener()
        }

    }


    override fun onResume() {
        super.onResume()

        if (PermissionUtils.isLocationEnabled(this)){

            mMap?.let {
                //determines if cameras zoom is changed
                // if it has been changed we will not update current location on map

                if (it.cameraPosition.zoom <= 3.0){
                    setUpLocationListener()
                }
                else{
                    // do nothing
                }
            }
        }
        // prompt user to enable location
        else{
            PermissionUtils.showGPSNotEnabledDialog(this)
        }
    }


    private fun setUpLocationListener() {

        val fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this)

        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {

            PermissionUtils.requestAccessFineLocationPermission(this,
                LOCATION_PERMISSION_REQUEST_CODE)

            return

        }

        // enable current location option on Map
        mMap?.isMyLocationEnabled = true

        fusedLocationProviderClient.lastLocation.addOnSuccessListener {
            it?.let {
                updateMap(LatLng(it.latitude,it.longitude))
            }
        }

    }


    fun updateMap(latLng: LatLng){

        val markerOptions = MarkerOptions()
        markerOptions.position(latLng)

        markerOptions.title("")
        markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_MAGENTA))
        mMap?.clear()
        mMap?.addMarker(markerOptions)
        mMap?.run { moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 16F)) }
    }



    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            LOCATION_PERMISSION_REQUEST_CODE -> {
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    when {
                        PermissionUtils.isLocationEnabled(this) -> {
                            setUpLocationListener()
                        }
                        else -> {
                            PermissionUtils.showGPSNotEnabledDialog(this)
                        }
                    }
                } else {
                    Toast.makeText(
                        this,
                        getString(R.string.location_permission_not_granted),
                        Toast.LENGTH_LONG
                    ).show()
                }
            }
        }
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

            data?.let {

                if (it.hasExtra(Const.LONGITUDE_KEY)) {
                    val lat: Double? = it.getDoubleExtra(Const.LATITUDE_KEY,0.0)
                    val long: Double? = it.getDoubleExtra(Const.LONGITUDE_KEY,0.0)
                    val latLng = LatLng(lat!!, long!!)

                    updateMap(latLng)
                }

            }
        }





}