package com.firstteamout.farmershopping.Adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.firstteamout.farmershopping.POKO.User
import com.firstteamout.farmershopping.R

class UserAdapter(var usersArray: ArrayList<User>): RecyclerView.Adapter<UserAdapter.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.single_user_data, parent, false)
        return ViewHolder(itemView)
    }

    override fun getItemCount(): Int {
        return usersArray.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.userName!!.setText(usersArray.get(position).name)
        holder.userPoints!!.setText(usersArray.get(position).user_points.toString()+" Points")
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        var userName : TextView? = null
        var userPoints : TextView? = null
        init {
            this.userPoints = itemView.findViewById(R.id.user_points)
            this.userName = itemView.findViewById(R.id.user_name)
        }
    }
}