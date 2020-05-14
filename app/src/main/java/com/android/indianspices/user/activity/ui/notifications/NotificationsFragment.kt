package com.android.indianspices.user.activity.ui.notifications

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.Navigation
import com.android.indianspices.LoginActivity
import com.android.indianspices.R
import com.android.indianspices.model.User
import com.android.indianspices.user.activity.HomeScreenActivity
import com.android.indianspices.user.activity.ui.home.HomeFragmentDirections
import com.android.indianspices.user.activity.ui.menu.FoodDetailFragment
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.fragment_notifications.*


class NotificationsFragment : Fragment()
{



    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View?
    {

        val root = inflater.inflate(R.layout.fragment_notifications, container, false)

        val databaseReference = FirebaseDatabase.getInstance().getReference("users")
        val userId = FirebaseAuth.getInstance().uid
        val logoutButton: Button = root.findViewById(R.id.logoutButton)
        val textHeader: TextView = root.findViewById(R.id.text_header)
        val currentOrdersButton: Button = root.findViewById(R.id.current_orders_button)
        val updateProfileButton:Button=root.findViewById(R.id.edit_account_button)
        var phoneNumber:String?=null

        currentOrdersButton.setOnClickListener { view ->

           val onViewOrderCheck= NotificationsFragmentDirections.actionNavigationNotificationsToNavigationViewOrders()
            onViewOrderCheck.setPhoneNumber(phoneNumber.toString())
            Navigation.findNavController(view).navigate(onViewOrderCheck)


        }
        updateProfileButton.setOnClickListener { view ->
            /*var updateProfileFragment: UpdateProfileFragment = UpdateProfileFragment()

            var fragmentTransaction: FragmentTransaction =
                (view.context as HomeScreenActivity).supportFragmentManager.beginTransaction()
                    .replace(R.id.nav_host_fragment, updateProfileFragment)
            fragmentTransaction.commit()*/
            Navigation.findNavController(view).navigate(R.id.navigation_profile_update)
        }

            logoutButton.setOnClickListener { _ ->
                FirebaseAuth.getInstance().signOut()
                val i = Intent(activity, LoginActivity::class.java)
                startActivity(i)

            }


            val postListener = object : ValueEventListener
            {
                override fun onDataChange(dataSnapshot: DataSnapshot)
                {

                        val user = dataSnapshot.getValue(User::class.java)

                        if (user != null && user?.userId.equals(userId))
                        {
                            textHeader.text = "Hello," + " " + user.name
                            phoneNumber=user?.phone.toString()

                        }


                }


                override fun onCancelled(databaseError: DatabaseError)
                {

                }
            }

            databaseReference.child(userId!!).addListenerForSingleValueEvent(postListener)

            return root
        }

        override fun onAttachFragment(childFragment: Fragment)
        {
            super.onAttachFragment(childFragment)
        }
    }