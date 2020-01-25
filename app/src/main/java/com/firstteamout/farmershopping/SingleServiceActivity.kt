package com.firstteamout.farmershopping

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.widget.Toast
import com.firstteamout.farmershopping.MainApp.Mainapp
import com.firstteamout.farmershopping.POKO.ResponseFlagMessage
import com.firstteamout.farmershopping.POKO.SingleService
import com.firstteamout.farmershopping.POKO.data
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_single_service.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class SingleServiceActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_single_service)
        getSupportActionBar()!!.setDisplayHomeAsUpEnabled(true)
        val intent = intent
        val message = intent.getStringExtra("message")
        if (message.equals("true")){
            purchaseBtn.setText("Purchased")
            purchaseBtn.isEnabled = false
        }
        getMainApp().farmerApi.getSingleService(intent.getStringExtra("serviceId")!!).enqueue(object : Callback<SingleService>{
            override fun onFailure(call: Call<SingleService>, t: Throwable) {
                Log.e("data", t.message.toString())
            }

            override fun onResponse(call: Call<SingleService>, response: Response<SingleService>) {
                if (response.isSuccessful){
                    supportActionBar!!.title = ""
                    single_service_text.setText(response.body()!!.singleService.service_name)
                    purchasePoints.setText(response.body()!!.singleService.service_points)
                    single_service_description.setText(response.body()!!.singleService.service_description)
                }
            }

        })
        purchaseBtn.setOnClickListener {
            if (FirebaseAuth.getInstance().currentUser != null){
                getMainApp().farmerApi.getSingleUser(FirebaseAuth.getInstance().currentUser!!.uid).enqueue(object : Callback<data>{
                    override fun onFailure(call: Call<data>, t: Throwable) {
                        Log.e("error", t.message.toString())
                    }

                    override fun onResponse(call: Call<data>, response: Response<data>) {
                        if (response.isSuccessful){
                            if (response.body()!!.data.user_points >= purchasePoints.text.toString().toInt()){
                                getMainApp().farmerApi.redeemService(intent.getStringExtra("serviceId")!!, FirebaseAuth.getInstance().currentUser!!.uid).enqueue(object : Callback<ResponseFlagMessage>{
                                    override fun onFailure(call: Call<ResponseFlagMessage>, t: Throwable) {
                                        Log.e("error", t.message.toString())
                                    }

                                    override fun onResponse(
                                        call: Call<ResponseFlagMessage>,
                                        response: Response<ResponseFlagMessage>
                                    ) {
                                        if (response.isSuccessful){
                                            val intent2 = Intent(this@SingleServiceActivity, MainActivity::class.java)
                                            intent2.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                                            startActivity(intent2)
                                            finish()
                                        }
                                    }

                                })
                            }else{
                                Toast.makeText(this@SingleServiceActivity, "You don't have Enought Credit Balance", Toast.LENGTH_SHORT).show()
                            }
                        }
                    }

                })
            }else{
                val intent3 = Intent(this, LoginActivity::class.java)
                intent3.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                startActivity(intent3)
                finish()
            }
        }
    }
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.getItemId()) {
            android.R.id.home -> {
                // app icon in action bar clicked; go home
                val intent = Intent(this, ServicesActivity::class.java)
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                startActivity(intent)
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun getMainApp(): Mainapp{
        return application as Mainapp
    }
}
