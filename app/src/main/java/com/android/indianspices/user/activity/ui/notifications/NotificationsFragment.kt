package com.android.indianspices.user.activity.ui.notifications

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.android.indianspices.LoginActivity
import com.android.indianspices.R
import com.android.indianspices.model.User
import com.android.indianspices.user.activity.HomeScreenActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.fragment_notifications.*


class NotificationsFragment : Fragment()
{

    private lateinit var notificationsViewModel: NotificationsViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View?
    {

        val root = inflater.inflate(R.layout.fragment_notifications, container, false)

        val databaseReference = FirebaseDatabase.getInstance().getReference("users")
       val userId=FirebaseAuth.getInstance().uid
        val logoutButton:Button=root.findViewById(R.id.logoutButton)
        val textHeader:TextView=root.findViewById(R.id.text_header)


        logoutButton.setOnClickListener { _->
            FirebaseAuth.getInstance().signOut()
            val i = Intent(activity, LoginActivity::class.java)
            startActivity(i)

        }


       val postListener=object :ValueEventListener{
           override fun onDataChange(dataSnapshot: DataSnapshot) {
               for(child in dataSnapshot.children)
               {
                  val user= child.getValue(User::class.java)
                   //val user= child.getValuevalue<User>()
                   if(user!=null && user?.userId.equals(userId))
                   {
                       textHeader.text="Hello,"+" "+user.name

                   }
               }

           }



           override fun onCancelled(databaseError: DatabaseError) {

           }
       }

        databaseReference.addListenerForSingleValueEvent(postListener)

        return root
    }

    override fun onAttachFragment(childFragment: Fragment)
    {
        super.onAttachFragment(childFragment)
    }
}