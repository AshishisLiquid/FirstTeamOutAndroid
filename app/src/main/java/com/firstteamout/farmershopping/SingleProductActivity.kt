package com.firstteamout.farmershopping

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.widget.Toast
import com.firstteamout.farmershopping.MainApp.Mainapp
import com.firstteamout.farmershopping.POKO.*
import com.google.firebase.auth.FirebaseAuth
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_single_product.*
import kotlinx.android.synthetic.main.single_shop_item.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class SingleProductActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_single_product)
        getSupportActionBar()!!.setDisplayHomeAsUpEnabled(true)
        val intent = intent
        val product_id = intent.getStringExtra("storeid")
        val url = intent.getStringExtra("url")
        val price = intent.getStringExtra("price")
        val name = intent.getStringExtra("name")
        val desc = intent.getStringExtra("desc")
        Picasso.get().load(url).resize(200, 200).centerCrop().into(single_product_image)
        single_product_text.setText(name)
        single_product_description.setText(desc)
        productPrice.setText(price)
        productPurchaseBtn.setOnClickListener {
            if(FirebaseAuth.getInstance().currentUser != null){
                getMainApp().farmerApi.buyProduct(FirebaseAuth.getInstance().currentUser!!.uid, product_id!!, productPrice.text.toString().toInt()).enqueue(object : Callback<ResponseData>{
                    override fun onFailure(call: Call<ResponseData>, t: Throwable) {
                        Log.e("error", t.message.toString())
                    }

                    override fun onResponse(
                        call: Call<ResponseData>,
                        response: Response<ResponseData>
                    ) {
                        if (response.isSuccessful){
                            Toast.makeText(this@SingleProductActivity, "Hello", Toast.LENGTH_SHORT).show()
                            Toast.makeText(this@SingleProductActivity, response.body()!!.msg, Toast.LENGTH_SHORT).show()
                            val intent2 = Intent(this@SingleProductActivity, MainActivity::class.java)
                            intent2.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                            startActivity(intent2)
                            finish()
                        }
                    }

                })
            }else{
                val intent1 = Intent(this, LoginActivity::class.java)
                intent1.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                startActivity(intent1)
                finish()
            }
        }
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

    private fun getMainApp(): Mainapp{
        return application as Mainapp
    }
}
