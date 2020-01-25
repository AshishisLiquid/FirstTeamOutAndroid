package com.firstteamout.farmershopping

import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.firstteamout.farmershopping.MainApp.Mainapp
import com.firstteamout.farmershopping.POKO.ResponseMessage
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_signup.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class SignupActivity : AppCompatActivity() {
    private var fAuth = FirebaseAuth.getInstance()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_signup)
        getSupportActionBar()!!.setDisplayHomeAsUpEnabled(true)
        supportActionBar!!.title = "Sign Up"
        btn_signup.setOnClickListener {
            signUpTasks()
        }
    }

    private fun signUpTasks() {
        Log.e("in", "signup second if")
        if (input_password.text.toString().equals(input_confirm_password.text.toString())) {
            Log.e("in", "done")
            fAuth.createUserWithEmailAndPassword(
                input_email.text.toString(),
                input_password.text.toString()
            ).addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    getMainApp().farmerApi.registerUser(
                        fAuth.currentUser!!.uid,
                        input_name.text.toString(),
                        input_email.text.toString(),
                        input_password.text.toString()
                    ).enqueue(object :
                        Callback<ResponseMessage> {
                        override fun onFailure(
                            call: Call<ResponseMessage>,
                            t: Throwable
                        ) {
                            Toast.makeText(
                                this@SignupActivity,
                                t.message,
                                Toast.LENGTH_SHORT
                            ).show()
                        }

                        override fun onResponse(
                            call: Call<ResponseMessage>,
                            response: Response<ResponseMessage>
                        ) {
                            if (response.isSuccessful) {
                                if (response.body()!!.message.equals("User saved successfully")) {
                                    val alertDialog = AlertDialog.Builder(this@SignupActivity)
                                    alertDialog.setTitle("Referral Code")
                                    alertDialog.setMessage("Enter Referral Code")
                                    val input = EditText(this@SignupActivity)
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
                                                                        this@SignupActivity,
                                                                        MainActivity::class.java
                                                                    )
                                                                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                                                                    startActivity(intent)
                                                                    finish()
                                                                } else {
                                                                    Toast.makeText(
                                                                        this@SignupActivity,
                                                                        "Invalid Referral Code",
                                                                        Toast.LENGTH_SHORT
                                                                    ).show()
                                                                }
                                                            }
                                                        }

                                                    })
                                                } else {
                                                    Toast.makeText(
                                                        this@SignupActivity,
                                                        "Please Enter valid Referral Code",
                                                        Toast.LENGTH_SHORT
                                                    ).show()
                                                }
                                            }

                                        })
                                    alertDialog.setNegativeButton(
                                        "Skip"
                                    ) { dialog, which ->
                                        dialog.cancel()
                                        val intent = Intent(this@SignupActivity, MainActivity::class.java)
                                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                                        startActivity(intent)
                                        finish()
                                    }
                                    alertDialog.show()
                                }
                            }
                        }

                    })

                }
            }.addOnFailureListener { exception ->
                Toast.makeText(this, exception.message, Toast.LENGTH_SHORT).show()
            }
        } else {
            Toast.makeText(this, "Please Confirm Your Password", Toast.LENGTH_SHORT).show()
        }

    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.getItemId()) {
            android.R.id.home -> {
                // app icon in action bar clicked; go home
                val intent = Intent(this, LoginActivity::class.java)
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
