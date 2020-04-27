package com.android.indianspices

import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.core.content.ContextCompat.getSystemService
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import kotlinx.android.synthetic.main.activity_main.*
import java.lang.Exception
import java.lang.reflect.Method
import androidx.core.app.ComponentActivity.ExtraData
import androidx.core.content.ContextCompat.getSystemService
import android.icu.lang.UCharacter.GraphemeClusterBreak.T
import android.net.NetworkInfo
import com.android.indianspices.user.activity.HomeScreenActivity


class MainActivity : AppCompatActivity()
{

    lateinit var auth: FirebaseAuth
    var user: FirebaseUser?=null
    lateinit var connectivityManager:ConnectivityManager





    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        auth =FirebaseAuth.getInstance();
        user = FirebaseAuth.getInstance().getCurrentUser()
        splashScreen.postDelayed(Runnable { ->
            if (isMobileDataEnabled())
            {

                if (user == null)
                {
                    val i = Intent(this, LoginActivity::class.java)
                    startActivity(i)
                    finish()
                }
                else{
                    val i = Intent(this, HomeScreenActivity::class.java)
                    startActivity(i)
                    finish()
                }

            }
        },3000)



    }

        fun isMobileDataEnabled():Boolean
        {
            var mobileDataEnabled: Boolean = false;
            connectivityManager =
                getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
            val activeNetwork: NetworkInfo? = connectivityManager.activeNetworkInfo

            if (activeNetwork?.isConnected != null)
            {
                return activeNetwork.isConnected
            }
            else
            {
                return false
            }
        }
}
