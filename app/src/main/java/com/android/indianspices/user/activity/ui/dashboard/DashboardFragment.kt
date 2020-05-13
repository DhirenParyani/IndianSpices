package com.android.indianspices.user.activity.ui.dashboard

import android.app.AlertDialog
import android.content.DialogInterface
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.indianspices.R
import com.android.indianspices.adapter.FoodCartAdapter
import com.android.indianspices.database.AppDatabase
import com.android.indianspices.model.*
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList


class DashboardFragment : Fragment()
{

    private lateinit var dashboardViewModel: DashboardViewModel


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View?
    {


        var foodCartList=ArrayList<Orders>()
        val root = inflater.inflate(R.layout.fragment_dashboard, container, false)
        val database = FirebaseDatabase.getInstance()
        val databaseReferencetoUserData = database.getReference("users")
        val databaseReferenceToRequest=database.getReference("Requests")
        val userId= FirebaseAuth.getInstance().uid
        var placeOrderButton: Button =root.findViewById(R.id.placeorderbutton)
        val foodCartRecyclerView: RecyclerView =root.findViewById(R.id.recycler_foodCart)
        val totalValue: TextView =root.findViewById(R.id.total_value)
        foodCartRecyclerView.layoutManager= LinearLayoutManager(this.activity,LinearLayoutManager.VERTICAL,false)
        var foodCartAdapter= FoodCartAdapter(foodCartList)
        foodCartRecyclerView.adapter=foodCartAdapter

        GlobalScope.launch {

            foodCartList.clear()
            val db = AppDatabase.getInstance(root.context)
            val databaseAccess = db?.orderDao()
            var total:Double=0.0
            foodCartList.addAll(databaseAccess?.getAll() as ArrayList<Orders>)
            for(order in foodCartList)
            {
                total+=order.price.toString().toDouble()*order.quantity.toString().toDouble()


            }
            GlobalScope.launch(Dispatchers.Main) {
                totalValue.text=total.toString()
                foodCartAdapter.notifyDataSetChanged()


            }


        }


        placeOrderButton.setOnClickListener(View.OnClickListener {view->
            var alertDialog=AlertDialog.Builder(view.context)
            val linearLayout:LinearLayout=LinearLayout(view.context)
            linearLayout.orientation=LinearLayout.VERTICAL
            alertDialog.setTitle("Enter Address")


            var editAddress:EditText= EditText(view.context)
            editAddress.hint="Address"

            var editPinCode:EditText= EditText(view.context)
            editPinCode.hint="Pincode"



            linearLayout.addView(editAddress)
            linearLayout.addView(editPinCode)
            alertDialog.setView(linearLayout)
            alertDialog.setIcon(R.drawable.ic_shopping_cart_black_24dp)

            alertDialog.setPositiveButton("YES", DialogInterface.OnClickListener {
                    dialog, id ->
                val postListener=object : ValueEventListener
                {
                    override fun onDataChange(dataSnapshot: DataSnapshot)
                    {

                        var user = dataSnapshot.getValue(User::class.java)
                        var request=Request()
                        if(user!=null)
                        {
                            var status="0"
                             request = Request(user.name, user.phone,totalValue.text.toString(),foodCartList,
                                 SimpleDateFormat("dd-MMM-YYYY HH:mm:sss").format(Date()),status)
                        }

                        databaseReferenceToRequest.child(System.currentTimeMillis().toString()).setValue(request)
                        GlobalScope.launch {

                            foodCartList.clear()
                            val db = AppDatabase.getInstance(root.context)
                            val databaseAccess = db?.orderDao()
                            var total:Double=0.0
                            databaseAccess?.deleteAll()
                            GlobalScope.launch(Dispatchers.Main) {

                                foodCartAdapter.notifyDataSetChanged()


                            }




                        }


                    }
                    override fun onCancelled(databaseError: DatabaseError) {

                    }
                }
                databaseReferencetoUserData.child(userId.toString()).addListenerForSingleValueEvent(postListener)

                Toast.makeText(root.context,"Thank You!!",Toast.LENGTH_SHORT).show()

            })


            alertDialog.setNegativeButton("NO", DialogInterface.OnClickListener {
                    dialog, id ->
                dialog.dismiss()


            })


            alertDialog.create()
            alertDialog.show()




        })










        return root
    }



}