package com.firstteamout.farmershopping.Adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.firstteamout.farmershopping.POKO.Service
import com.firstteamout.farmershopping.POKO.services
import com.firstteamout.farmershopping.R
import com.firstteamout.farmershopping.SingleServiceActivity

class ServicesAdapter(var servicesList : ArrayList<services>, var singleServiceClick : View.OnClickListener) : RecyclerView.Adapter<ServicesAdapter.ViewHolder>(){
    var serviceId = ""
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.service_ticket, parent, false)
        return ViewHolder(itemView)
    }

    override fun getItemCount(): Int {
        return servicesList.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.serviceName!!.setText(servicesList.get(position).service_name)
        holder.servicePrice!!.setText(servicesList.get(position).service_points)
        holder.serviceDetails!!.setText(servicesList.get(position).service_description)
        holder.serviceCard!!.setOnClickListener {
            serviceId = servicesList.get(position).id.toString()
            singleServiceClick.onClick(it)
        }
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var serviceName: TextView? = null
        var serviceDetails: TextView? = null
        var servicePrice: TextView? = null
        var serviceCard : CardView? = null
        init {
            this.serviceCard = itemView.findViewById(R.id.service_card)
            this.serviceName = itemView.findViewById(R.id.service_name)
            this.serviceDetails = itemView.findViewById(R.id.service_desc)
            this.servicePrice = itemView.findViewById(R.id.service_price)
        }
    }
}