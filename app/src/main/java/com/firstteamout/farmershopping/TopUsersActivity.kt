package com.firstteamout.farmershopping

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import androidx.recyclerview.widget.LinearLayoutManager
import com.firstteamout.farmershopping.Adapters.UserAdapter
import com.firstteamout.farmershopping.MainApp.Mainapp
import com.firstteamout.farmershopping.POKO.User
import com.firstteamout.farmershopping.POKO.Users
import kotlinx.android.synthetic.main.activity_top_users.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class TopUsersActivity : AppCompatActivity() {
    lateinit var userAdapter : UserAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_top_users)
        getSupportActionBar()!!.setDisplayHomeAsUpEnabled(true)
        supportActionBar!!.title = "Top Users"
        users_rcv.layoutManager = LinearLayoutManager(this)
        userAdapter = UserAdapter(arrayListOf())
        users_rcv.adapter = userAdapter
        getTopUsers()
    }

    private fun getTopUsers() {
        getMainApp().farmerApi.getTopUsersList().enqueue(object : Callback<Users>{
            override fun onFailure(call: Call<Users>, t: Throwable) {
                Log.e("error", t.message.toString())
            }

            override fun onResponse(call: Call<Users>, response: Response<Users>) {
                if(response.isSuccessful){
                    val data = response.body()!!.topUsers as ArrayList<User>
                    userAdapter.usersArray.addAll(data)
                    userAdapter.notifyDataSetChanged()
                }
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

    private fun getMainApp(): Mainapp{
        return application as Mainapp
    }
}
