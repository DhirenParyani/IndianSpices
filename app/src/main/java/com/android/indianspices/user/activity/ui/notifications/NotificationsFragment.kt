package com.android.indianspices.user.activity.ui.notifications

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.android.indianspices.R
import com.android.indianspices.model.User
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
        notificationsViewModel =
            ViewModelProviders.of(this).get(NotificationsViewModel::class.java)
        val root = inflater.inflate(R.layout.fragment_notifications, container, false)
        val textView: TextView = root.findViewById(R.id.text_notifications)
        notificationsViewModel.text.observe(viewLifecycleOwner, Observer {
            textView.text = it
        })
        val databaseReference = FirebaseDatabase.getInstance().getReference("users")
       val userId=FirebaseAuth.getInstance().uid

        //val user= databaseReference.child(userId.toString()). as User
        /*nameText.text=user.name
        phoneText.text=user.phone
        emailText.text=user.email*/
       val postListener=object :ValueEventListener{
           override fun onDataChange(dataSnapshot: DataSnapshot) {
               for(child in dataSnapshot.children)
               {
                  val user= child.getValue(User::class.java)
                   //val user= child.getValuevalue<User>()
                   if(user?.userId.equals(userId))
                   {
                       nameText.text=user?.name
                       phoneText.text=user?.phone
                       emailText.text=user?.email
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