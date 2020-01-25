package com.firstteamout.farmershopping

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.drawerlayout.widget.DrawerLayout
import androidx.recyclerview.widget.LinearLayoutManager
import com.firstteamout.farmershopping.Adapters.StoreAdapter
import com.firstteamout.farmershopping.MainApp.Mainapp
import com.firstteamout.farmershopping.POKO.Product
import com.firstteamout.farmershopping.POKO.Products
import com.firstteamout.farmershopping.POKO.SingleProduct
import com.google.android.material.navigation.NavigationView
import com.google.firebase.auth.FirebaseAuth
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.activity_single_product.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class MainActivity : AppCompatActivity() {
    private var dl: DrawerLayout? = null
    private var menuitems: Menu? = null
    private var t: ActionBarDrawerToggle? = null
    private var nv: NavigationView? = null
    lateinit var storeAdapter: StoreAdapter
    lateinit var productClick : View.OnClickListener
    val currentUser = FirebaseAuth.getInstance().currentUser
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        dl = findViewById(R.id.activity_main_drawable) as DrawerLayout
        t = ActionBarDrawerToggle(this, dl, R.string.open, R.string.close)
        dl!!.addDrawerListener(t!!)
        t!!.syncState()
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        shoppinglist_rcv.layoutManager = LinearLayoutManager(this)
        productClick = View.OnClickListener {
            getMainApp().farmerApi.getSingleProduct(storeAdapter.storeId).enqueue(object : Callback<SingleProduct>{
                override fun onFailure(call: Call<SingleProduct>, t: Throwable) {
                    Log.e("data", t.message.toString())
                }

                override fun onResponse(
                    call: Call<SingleProduct>,
                    response: Response<SingleProduct>
                ) {
                    if (response.isSuccessful){
                        val intent = Intent(this@MainActivity, SingleProductActivity::class.java)
                        intent.putExtra("storeid", storeAdapter.storeId)
                        intent.putExtra("url", response.body()!!.Product.image_url)
                        intent.putExtra("price", response.body()!!.Product.product_price.toString())
                        intent.putExtra("name", response.body()!!.Product.product_name)
                        intent.putExtra("desc", response.body()!!.Product.description)
                        startActivity(intent)
                    }
                }
            })
        }
        storeAdapter = StoreAdapter(arrayListOf(), productClick)
        shoppinglist_rcv.adapter = storeAdapter
        nv = findViewById(R.id.nv) as NavigationView
        nv!!.setNavigationItemSelectedListener(object :
            NavigationView.OnNavigationItemSelectedListener {
            override fun onNavigationItemSelected(item: MenuItem): Boolean {
                val id: Int = item.getItemId()
                when (id) {
                    R.id.account -> {
                        if (currentUser != null) {
                            startActivity(Intent(this@MainActivity, MyAccountActivity::class.java))
                        } else {
                            startActivity(Intent(this@MainActivity, LoginActivity::class.java))
                        }
                    }
                    R.id.topUsers -> {
                        startActivity(Intent(this@MainActivity, TopUsersActivity::class.java))
                    }
                    R.id.services -> {
                        startActivity(Intent(this@MainActivity, ServicesActivity::class.java))
                    }
                    else -> return true
                }
                return true
            }
        })
        getStoresList()
    }

    private fun getStoresList() {
        getMainApp().farmerApi.getAllStoresProduct().enqueue(object : Callback<Products>{
            override fun onFailure(call: Call<Products>, t: Throwable) {
                Log.e("error", t.message.toString())
            }

            override fun onResponse(call: Call<Products>, response: Response<Products>) {
                if (response.isSuccessful){
                    val data = response.body()!!.Products as ArrayList<Product>
                    storeAdapter.storeList.addAll(data)
                    storeAdapter.notifyDataSetChanged()
                }
            }

        })
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return if (t!!.onOptionsItemSelected(item)) true else super.onOptionsItemSelected(item)
    }

    private fun getMainApp(): Mainapp{
        return application as Mainapp
    }
}

