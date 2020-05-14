package com.android.indianspices.user.activity.ui.menu

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.android.indianspices.R
import com.android.indianspices.adapter.CategoryListAdapter
import com.android.indianspices.adapter.FoodListAdapter
import com.android.indianspices.model.Food
import com.android.indianspices.model.FoodCategory
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener


class MenuFragment : Fragment()
{


    private lateinit var firebaseAuth: FirebaseAuth

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View?
    {

        val root = inflater.inflate(R.layout.fragment_menu, container, false)
        val databaseReference = FirebaseDatabase.getInstance().getReference("Foods")
        var foodList=ArrayList<Food>()
        val foodListRecyclerView:RecyclerView=root.findViewById(R.id.recycler_foodList)
        foodListRecyclerView.layoutManager= StaggeredGridLayoutManager(2,LinearLayoutManager.HORIZONTAL)
        var foodListAdapter=FoodListAdapter(foodList)
        foodListRecyclerView.adapter=foodListAdapter
        var isArgumentPresent=false
        var position:Int=0
       /* if(this.arguments!=null)
        {
            position=arguments!!.getInt("position")
            isArgumentPresent=true

        }*/
        arguments?.let {
            val safeargs=MenuFragmentArgs.fromBundle(it)
            position=safeargs.categoryID
            isArgumentPresent=true
        }

        val postListener=object : ValueEventListener
        {
            override fun onDataChange(dataSnapshot: DataSnapshot) {


                foodList.clear()

                for(child in dataSnapshot.children)
                {
                    if(isArgumentPresent)
                    {
                        val food = child.getValue(Food::class.java)
                        food?.id=child.key.toString()
                        if(food!=null)
                        {

                            if((food.menuid)?.toInt()==(position+1))
                            foodList.add(food!!)
                        }

                    }
                    else
                    {
                        val food = child.getValue(Food::class.java)
                        food?.id=child.key.toString()

                        foodList.add(food!!)
                    }
                    //val user= child.getValuevalue<User>()


                }


               foodListAdapter .notifyDataSetChanged()

            }


            override fun onCancelled(databaseError: DatabaseError) {

            }
        }
        databaseReference.addListenerForSingleValueEvent(postListener)





        return root
    }
}