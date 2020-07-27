package com.task.codegamma

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.gms.maps.model.LatLng
import com.task.codegamma.adapters.RecentPlacesAdapter
import com.task.codegamma.database.RecentPlace
import com.task.codegamma.viewmodel.RecentPlaceViewModel
import kotlinx.android.synthetic.main.activity_main.*
import java.util.ArrayList

class RecentHistoryActivity : AppCompatActivity(), RecentPlacesAdapter.PlaceOnClickListener {

    //
    lateinit var viewModel: RecentPlaceViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        viewModel = ViewModelProviders.of(this).get(RecentPlaceViewModel::class.java)
        recyclerview.layoutManager = LinearLayoutManager(this)

        viewModel.recentPlaceLiveData?.observe(this, Observer {

            if (it.isEmpty()){
                record_empty_message_layout.visibility = View.VISIBLE
            } else{
                record_empty_message_layout.visibility = View.GONE
            }

            val adapter = RecentPlacesAdapter(it as ArrayList<RecentPlace>,this)
            adapter.setClickListener(this)
            recyclerview.adapter = adapter
        })

    }


    override fun placeClicked(place: RecentPlace) {
        val intent = Intent()

        intent.putExtra(Const.LATITUDE_KEY,place.latLng?.latitude)
        intent.putExtra(Const.LONGITUDE_KEY,place.latLng?.longitude)

        setResult(Const.REDIRECT_LOCATION_REQUEST,intent)
        finish()
    }
}