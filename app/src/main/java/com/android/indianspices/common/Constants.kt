package com.android.indianspices.common

import com.google.firebase.auth.FirebaseAuth

class Constants
{
    companion object{

        val PAYPAL_CLIENT_ID="Afbvj5ao-JaU4_HWaPtV_gmUWB9IQT9CNTbBB5jlKqop_5FBRcdVpD8nILz7M_SO5vE7WH1WNist7gSR"
        val userId= FirebaseAuth.getInstance().uid
        var username:String?=null
        var userphone:String?=null
        var userEmail:String?=null


    }

}