package com.android.indianspices

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.android.indianspices.user.activity.HomeScreenActivity
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_login.*

class LoginActivity : AppCompatActivity()
{
    lateinit var firebaseAuth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        firebaseAuth = FirebaseAuth.getInstance()
        signUpButton.setOnClickListener { _->
            startActivity(Intent(this, SignUpActivity::class.java))

        }
       loginButton.setOnClickListener {_->

            if(!loginEmailAddressText.text.toString().isNullOrEmpty() && !loginPasswordText.text.toString().isNullOrEmpty())
            {
                FirebaseAuth.getInstance().signInWithEmailAndPassword(loginEmailAddressText.text.toString(), loginPasswordText.getText().toString())
                    .addOnCompleteListener(this) {task->
                        openHomeScreen()

                    }
            }
       }



    }

    public fun openHomeScreen()
    {
        var intent=Intent(this,HomeScreenActivity::class.java)
        startActivity(intent)
        finish()


    }
}
