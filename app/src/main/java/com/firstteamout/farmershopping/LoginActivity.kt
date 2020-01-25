package com.firstteamout.farmershopping

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.SpannableString
import android.text.Spanned
import android.text.method.LinkMovementMethod
import android.text.style.ClickableSpan
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_login.*

class LoginActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        getSupportActionBar()!!.setDisplayHomeAsUpEnabled(true)
        supportActionBar!!.title = "Login"
        val ss = SpannableString("Don't have an account? Sign Up")
        val clickableSpan1 = object : ClickableSpan() {
            override fun onClick(textView: View) {
                startActivity(Intent(this@LoginActivity, SignupActivity::class.java));
            }
        }
        ss.setSpan(clickableSpan1, 23, 30, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
        signup_text.setText(ss)
        signup_text.movementMethod = LinkMovementMethod.getInstance()
        btn_login.setOnClickListener {
            loginFunctionality()
        }
    }

    private fun loginFunctionality() {
        if (!input_email.text.equals("") && !input_password.text.equals("")) {
            FirebaseAuth.getInstance().signInWithEmailAndPassword(
                input_email.text.toString(),
                input_password.text.toString()
            ).addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val intent = Intent(this, MainActivity::class.java)
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                    startActivity(intent)
                    finish()
                }
            }.addOnFailureListener { exception ->
                    Toast.makeText(this, exception.message, Toast.LENGTH_SHORT).show()
                }
        } else {
            Toast.makeText(this, "Please Enter email or password", Toast.LENGTH_SHORT).show()
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
}
