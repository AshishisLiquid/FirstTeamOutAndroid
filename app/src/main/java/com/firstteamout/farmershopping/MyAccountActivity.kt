package com.firstteamout.farmershopping

import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.text.SpannableString
import android.text.Spanned
import android.text.method.LinkMovementMethod
import android.text.style.ClickableSpan
import android.util.Log
import android.view.MenuItem
import android.view.View
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.firstteamout.farmershopping.MainApp.Mainapp
import com.firstteamout.farmershopping.POKO.ResponseMessage
import com.firstteamout.farmershopping.POKO.data
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_login.*
import kotlinx.android.synthetic.main.activity_my_account.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class MyAccountActivity : AppCompatActivity() {
    private var fAuth = FirebaseAuth.getInstance()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_my_account)
        getSupportActionBar()!!.setDisplayHomeAsUpEnabled(true)
        getUserData()
        bu_user_singout.setOnClickListener {
            FirebaseAuth.getInstance().signOut()
            val intent = Intent(this, MainActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
            startActivity(intent)
            finish()
        }
        val ss = SpannableString("Add Referral Code ? Click Here")
        val clickableSpan1 = object : ClickableSpan() {
            override fun onClick(textView: View) {
                Log.e("data", "data")
                val alertDialog = AlertDialog.Builder(this@MyAccountActivity)
                alertDialog.setTitle("Referral Code")
                alertDialog.setMessage("Enter Referral Code")
                val input = EditText(this@MyAccountActivity)
                val lp =
                    LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.MATCH_PARENT
                    )
                input.setLayoutParams(lp)
                alertDialog.setView(input)
                alertDialog.setPositiveButton(
                    "Submit",
                    object : DialogInterface.OnClickListener {
                        override fun onClick(
                            dialog: DialogInterface?,
                            which: Int
                        ) {
                            val referralCode = input.text.toString()
                            if (referralCode.length == 6) {
                                getMainApp().farmerApi.validateReferralCode(
                                    fAuth.currentUser!!.uid,
                                    referralCode
                                ).enqueue(object : Callback<ResponseMessage> {
                                    override fun onFailure(
                                        call: Call<ResponseMessage>,
                                        t: Throwable
                                    ) {
                                        Log.e("trace", t.stackTrace.toString())
                                    }

                                    override fun onResponse(
                                        call: Call<ResponseMessage>,
                                        response: Response<ResponseMessage>
                                    ) {
                                        if (response.isSuccessful) {
                                            if (response.body()!!.message.equals(
                                                    "Points Earned"
                                                )
                                            ) {
                                                val intent = Intent(
                                                    this@MyAccountActivity,
                                                    MainActivity::class.java
                                                )
                                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                                                startActivity(intent)
                                                finish()
                                            } else {
                                                Toast.makeText(
                                                    this@MyAccountActivity,
                                                    "Invalid Referral Code",
                                                    Toast.LENGTH_SHORT
                                                ).show()
                                            }
                                        }
                                    }

                                })
                            } else {
                                Toast.makeText(
                                    this@MyAccountActivity,
                                    "Please Enter valid Referral Code",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                        }

                    })
                alertDialog.show()
            }
        }
        ss.setSpan(clickableSpan1, 20, 30, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
        referredcode.setText(ss)
        referredcode.movementMethod = LinkMovementMethod.getInstance()
        sharebtn.setOnClickListener {
            getMainApp().farmerApi.getSingleUser(FirebaseAuth.getInstance().currentUser!!.uid)
                .enqueue(object : Callback<data>{
                    override fun onFailure(call: Call<data>, t: Throwable) {
                        Log.e("message", t.message.toString())
                    }

                    override fun onResponse(call: Call<data>, response: Response<data>) {
                        val sendIntent = Intent()
                        sendIntent.action = Intent.ACTION_SEND
                        sendIntent.putExtra(Intent.EXTRA_TEXT, "Download The Application from https://drive.google.com/open?id=1sVsc347Yws3i0Kxpo9Y5T492pDFtey9x . Use the referral code *${response.body()!!.data.reward_code}* To get the Advice on Farmer's Shop")
                        sendIntent.type = "text/plain"
                        startActivity(sendIntent)
                    }

                })
        }
    }

    private fun getUserData() {
        getMainApp().farmerApi.getSingleUser(FirebaseAuth.getInstance().currentUser!!.uid)
            .enqueue(object : Callback<data>{
                override fun onFailure(call: Call<data>, t: Throwable) {
                    Log.e("message", t.message.toString())
                }

                override fun onResponse(call: Call<data>, response: Response<data>) {
                    val data = response.body()!!.data
                    tv_user_name.setText(data.name)
                    rewardPoints.setText(data.user_points.toString())
                    tv_user_emailid.setText(data.email)
                    if(response.body()!!.data.referral_code != null){
                        referredcode.setVisibility(View.GONE)
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
