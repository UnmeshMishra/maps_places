package com.task.codegamma.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.task.codegamma.R
import com.task.codegamma.database.RecentPlace

import java.util.ArrayList

class RecentPlacesAdapter(
    private val items: ArrayList<RecentPlace>,
    private val context: Context
) : RecyclerView.Adapter<RecentPlacesAdapter.RecentPlaceViewHolder>()
{

    private var listener:PlaceOnClickListener? = null
    
    override fun getItemCount(): Int {
        return items?.size
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecentPlaceViewHolder {
        return RecentPlaceViewHolder(
            LayoutInflater.from(context).inflate(
                R.layout.recent_place_items,
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: RecentPlaceViewHolder, position: Int) {

        val data = items.get(position)

        holder.placeNameText.text = data.name

        holder.placeAddressText.text = data.address
        holder.layout.setOnClickListener {
            listener?.placeClicked(items[position])
        }
        }


    fun setClickListener(listener: PlaceOnClickListener){
        this.listener = listener
    }

    interface PlaceOnClickListener{
        open fun placeClicked(place:RecentPlace)
    }

    class RecentPlaceViewHolder (view: View) : RecyclerView.ViewHolder(view) {

        val placeNameText = view.findViewById<TextView>(R.id.place_name_txt)
        val placeAddressText = view.findViewById<TextView>(R.id.place_address_txt)
        val layout = view.findViewById<LinearLayout>(R.id.recent_place_layout)
    }

}
