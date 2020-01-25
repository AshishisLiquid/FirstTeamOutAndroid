package com.firstteamout.farmershopping

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.firstteamout.farmershopping.Adapters.ServicesAdapter
import com.firstteamout.farmershopping.MainApp.Mainapp
import com.firstteamout.farmershopping.POKO.ResponseMessage
import com.firstteamout.farmershopping.POKO.Service
import com.firstteamout.farmershopping.POKO.services
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_services.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ServicesActivity : AppCompatActivity() {
    lateinit var serviceList: ArrayList<Service>
    lateinit var servicesAdapter: ServicesAdapter
    lateinit var singleServiceClick : View.OnClickListener
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_services)
        getSupportActionBar()!!.setDisplayHomeAsUpEnabled(true)
        services_rcv.layoutManager = LinearLayoutManager(this)
        singleServiceClick = View.OnClickListener {
            if (FirebaseAuth.getInstance().currentUser != null){
                getMainApp().farmerApi.checkServicePurchased(servicesAdapter.serviceId, FirebaseAuth.getInstance().currentUser!!.uid).enqueue(object : Callback<ResponseMessage>{
                    override fun onFailure(call: Call<ResponseMessage>, t: Throwable) {
                        Log.e("error", t.message.toString())
                    }

                    override fun onResponse(
                        call: Call<ResponseMessage>,
                        response: Response<ResponseMessage>
                    ) {
                        if (response.isSuccessful){
                            val intent = Intent(this@ServicesActivity, SingleServiceActivity::class.java)
                            intent.putExtra("serviceId", servicesAdapter.serviceId)
                            intent.putExtra("message", response.body()!!.message)
                            startActivity(intent)
                        }
                    }

                })
            }else{
                val intent = Intent(this@ServicesActivity, SingleServiceActivity::class.java)
                intent.putExtra("serviceId", servicesAdapter.serviceId)
                intent.putExtra("message", "false")
                startActivity(intent)
            }
        }
        servicesAdapter = ServicesAdapter(arrayListOf(), singleServiceClick)
        services_rcv.adapter = servicesAdapter
        getAllData()
    }

    private fun getAllData() {
        getMainApp().farmerApi.getAllServices().enqueue(object : Callback<Service>{
            override fun onFailure(call: Call<Service>, t: Throwable) {
                Log.e("error", t.message.toString())
            }

            override fun onResponse(call: Call<Service>, response: Response<Service>) {
                val data = response.body()!!.services as ArrayList<services>
                servicesAdapter.servicesList.addAll(data)
                servicesAdapter.notifyDataSetChanged()
            }

        })
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.getItemId()) {
            android.R.id.home -> {
                // app icon in action bar clicked; go home
                val intent = Intent(this, MainActivity::class.java)
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                startActivity(intent)
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun getMainApp() : Mainapp{
        return application as Mainapp
    }
}
