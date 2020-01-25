package com.firstteamout.farmershopping.Adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.firstteamout.farmershopping.POKO.Product
import com.firstteamout.farmershopping.R
import com.squareup.picasso.Picasso

class StoreAdapter(var storeList: ArrayList<Product>, var productClick: View.OnClickListener) : RecyclerView.Adapter<StoreAdapter.ViewHolder>() {
    var storeId = ""
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.single_shop_item, parent, false)
        return ViewHolder(itemView)
    }

    override fun getItemCount(): Int {
        return storeList.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        Picasso.get().load(storeList.get(position).image_url).resize(200, 200).centerCrop().into(holder.storeImage)
        holder.title!!.setText(storeList.get(position).product_name)
        holder.price!!.setText(storeList.get(position).product_price.toString()+" ₹")
        holder.storeCardView!!.setOnClickListener {
            storeId = storeList.get(position).id.toString()
            productClick.onClick(it)
        }
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        var storeImage : ImageView? = null
        var title: TextView? = null
        var price: TextView? = null
        var storeCardView : CardView? = null
        init {
            this.storeImage = itemView.findViewById(R.id.post_img)
            this.title = itemView.findViewById(R.id.product_name_Txt)
            this.price = itemView.findViewById(R.id.store_price_txt)
            this.storeCardView = itemView.findViewById(R.id.single_shop_card)
        }
    }
}