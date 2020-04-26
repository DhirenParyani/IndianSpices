package com.android.indianspices

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.android.indianspices.model.User
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthUserCollisionException
import com.google.firebase.database.FirebaseDatabase
import kotlinx.android.synthetic.main.activity_sign_up.*

class SignUpActivity : AppCompatActivity()
{
    lateinit var mAuth:FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up)
        mAuth = FirebaseAuth.getInstance()
        createAccountButton.setOnClickListener { _->
            registerUser()
        }

    }

    fun registerUser()
    {
        val name:String=textName.text?.trim().toString()
        val phone=textNumber.text?.trim().toString()
        val email=textEmail.text?.trim().toString()
        val password=textPassword.text?.trim().toString()
        mAuth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(this){

                task->
            if(task.isSuccessful)
            {
                var userID=mAuth.currentUser?.uid

                 val user:User=User(userID,name,phone,email,password)
                FirebaseDatabase.getInstance().getReference("users")
                    .child(userID.orEmpty()).setValue(user).addOnCompleteListener(this) {
                        subTask->
                        if (subTask.isSuccessful)
                        {
                            Toast.makeText(applicationContext, "Registered Successfully", Toast.LENGTH_SHORT).show();
                        }
                        else{
                            Toast.makeText(applicationContext, task.exception.toString(), Toast.LENGTH_SHORT).show();

                        }


                    }

            }
            else{
                if (task.exception is FirebaseAuthUserCollisionException)
                    Toast.makeText(getApplicationContext(), "Email Already Exists!!", Toast.LENGTH_SHORT).show();
                else
                Toast.makeText(this, "Sign up unsuccessful", Toast.LENGTH_SHORT)
            }

        }


    }


}
