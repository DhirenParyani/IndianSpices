package com.android.indianspices.user.activity.ui.dashboard

import android.app.Activity.RESULT_OK
import android.app.AlertDialog
import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.indianspices.common.Constants
import com.android.indianspices.R
import com.android.indianspices.adapter.FoodCartAdapter
import com.android.indianspices.database.AppDatabase
import com.android.indianspices.model.*
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.paypal.android.sdk.payments.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import org.json.JSONException
import org.json.JSONObject
import java.math.BigDecimal
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList


class DashboardFragment : Fragment()
{
    val PAYPAL_REQUEST_CODE=999
    var foodCartList=ArrayList<Orders>()
    var request = Request()
    var total:Double=0.0


   private val config:PayPalConfiguration=PayPalConfiguration().environment(PayPalConfiguration.ENVIRONMENT_SANDBOX).clientId(Constants.PAYPAL_CLIENT_ID)

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View?
    {




        val root = inflater.inflate(R.layout.fragment_dashboard, container, false)
        val database = FirebaseDatabase.getInstance()

        val userId= FirebaseAuth.getInstance().uid
        var placeOrderButton: Button =root.findViewById(R.id.placeorderbutton)
        val foodCartRecyclerView: RecyclerView =root.findViewById(R.id.recycler_foodCart)
        val totalValue: TextView =root.findViewById(R.id.total_value)

        val databaseReferenceToRequest=database.getReference("Requests")

        foodCartRecyclerView.layoutManager= LinearLayoutManager(this.activity,LinearLayoutManager.VERTICAL,false)
        var foodCartAdapter= FoodCartAdapter(foodCartList)
        foodCartRecyclerView.adapter=foodCartAdapter

        GlobalScope.launch {

            foodCartList.clear()
            val db = AppDatabase.getInstance(root.context)
            val databaseAccess = db?.orderDao()

            foodCartList.addAll(databaseAccess?.getAll() as ArrayList<Orders>)
            for(order in foodCartList)
            {
                total+=order.price.toString().toDouble()*order.quantity.toString().toDouble()


            }
            GlobalScope.launch(Dispatchers.Main) {
                totalValue.text=total.toString()
                foodCartAdapter.notifyDataSetChanged()
                val intent = Intent(activity, PayPalService::class.java)
                intent.putExtra(PayPalService.EXTRA_PAYPAL_CONFIGURATION,config)
                root.context.startService(intent)


            }


        }




        placeOrderButton.setOnClickListener(View.OnClickListener {view->
            var alertDialog=AlertDialog.Builder(view.context)
            val linearLayout:LinearLayout=LinearLayout(view.context)
            linearLayout.orientation=LinearLayout.VERTICAL
            alertDialog.setTitle("Discount Codes?")


            var promoCode:EditText= EditText(view.context)
            promoCode.hint="PromoCode"

            var enterPromoCode:Button= Button(view.context)
            enterPromoCode.text="ENTER"



            linearLayout.addView(promoCode)
            linearLayout.addView(enterPromoCode)
            alertDialog.setView(linearLayout)
            alertDialog.setIcon(R.drawable.ic_shopping_cart_black_24dp)

            alertDialog.setNeutralButton("Pay at Counter", DialogInterface.OnClickListener {
                    dialog, id ->

                            var status="0"
                             request = Request(Constants.username, Constants.userphone,totalValue.text.toString(),foodCartList,
                                 SimpleDateFormat("dd-MMM-YYYY HH:mm:sss").format(Date()),status,"Pay at counter")


                        databaseReferenceToRequest.child(System.currentTimeMillis().toString()).setValue(request)
                        GlobalScope.launch {

                            foodCartList.clear()
                            val db = AppDatabase.getInstance(root.context)
                            val databaseAccess = db?.orderDao()
                             total=0.0
                            databaseAccess?.deleteAll()
                            GlobalScope.launch(Dispatchers.Main) {

                                foodCartAdapter.notifyDataSetChanged()


                            }




                        }




                Toast.makeText(root.context,"Thank You!!",Toast.LENGTH_SHORT).show()

            })

            alertDialog.setPositiveButton("Pay using PayPal", DialogInterface.OnClickListener { dialog, id ->
                val totalAmount=totalValue.text.toString()
                val paypalPayment:PayPalPayment= PayPalPayment(BigDecimal(totalAmount),"USD","Indian Spices",PayPalPayment.PAYMENT_INTENT_SALE)
                val intent=Intent(activity,PaymentActivity::class.java)
                intent.putExtra(PayPalService.EXTRA_PAYPAL_CONFIGURATION,config)
                intent.putExtra(PaymentActivity.EXTRA_PAYMENT,paypalPayment)
                startActivityForResult(intent,PAYPAL_REQUEST_CODE)
            })


            alertDialog.setNegativeButton("Cancel", DialogInterface.OnClickListener {
                    dialog, id ->
                dialog.dismiss()


            })


            alertDialog.create()
            alertDialog.show()




        })










        return root
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?)
    {
        //super.onActivityResult(requestCode, resultCode, data)
        val database = FirebaseDatabase.getInstance()
        if(requestCode== PAYPAL_REQUEST_CODE)
        {
            if(resultCode==RESULT_OK)
            {
                val confirmation:PaymentConfirmation=data!!.getParcelableExtra(PaymentActivity.EXTRA_RESULT_CONFIRMATION)
                if(confirmation!=null)
                {
                    try
                    {
                        var paymentDetails = confirmation.toJSONObject().toString(4)
                        var json: JSONObject = JSONObject(paymentDetails)
                        var status = "0"
                        request = Request(Constants.username,
                            Constants.userId,
                            total.toString(),
                            foodCartList,
                            SimpleDateFormat("dd-MMM-YYYY HH:mm:sss").format(Date()),
                            status,
                            json.getJSONObject("response").getString("state"))
                        val databaseReferenceToRequest = database.getReference("Requests")
                        databaseReferenceToRequest.child(System.currentTimeMillis().toString())
                            .setValue(request)
                        GlobalScope.launch {

                            foodCartList.clear()


                        }
                    }
                    catch (ex:JSONException)
                    {
                        ex.printStackTrace()

                    }

                }
            }

        }
    }
}