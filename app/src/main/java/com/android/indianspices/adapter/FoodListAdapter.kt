package com.android.indianspices.adapter

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.FragmentTransaction
import androidx.recyclerview.widget.RecyclerView
import com.android.indianspices.R
import com.android.indianspices.model.Food
import com.android.indianspices.user.activity.HomeScreenActivity
import com.android.indianspices.user.activity.ui.home.MenuFragment
import com.android.indianspices.user.activity.ui.menu.FoodDetailFragment
import com.squareup.picasso.Picasso


class FoodListViewHolder(view:View):RecyclerView.ViewHolder(view)
{
    var foodThumbnail:ImageView=view.findViewById(R.id.foodImageView)
    var foodName:TextView=view.findViewById(R.id.foodName)
    var foodPrice:TextView=view.findViewById(R.id.foodPrice)

}

class  FoodListAdapter(private  var foodList:List<Food> ): RecyclerView.Adapter<FoodListViewHolder>(){
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FoodListViewHolder
    {
      val view=LayoutInflater.from(parent.context).inflate(R.layout.layout_food_item,parent,false)
        return FoodListViewHolder(view)

    }

    override fun getItemCount(): Int
    {
        return if(foodList.isNotEmpty())foodList.size else 0

    }
    fun loadNewData(newfoodList:List<Food>)
    {
        foodList=newfoodList
        notifyDataSetChanged()
    }
    fun getFood(position: Int):Food?{
        return if(foodList.isNotEmpty()) foodList[position] else null
    }

    override fun onBindViewHolder(holder: FoodListViewHolder, position: Int)
    {
        val food=foodList[position]
        Picasso.get().load(food.image).error(R.drawable.chef_image).placeholder(R.drawable.chef_image).into(holder.foodThumbnail)
         holder.foodName.text=food.name
        holder.foodPrice.text=food.price

        holder.foodThumbnail.setOnClickListener(View.OnClickListener {view->

            var bundle: Bundle = Bundle()
            bundle.putString("foodId",food.id)

            var foodDetailFragment: FoodDetailFragment = FoodDetailFragment()
            foodDetailFragment.arguments=bundle
            var fragmentTransaction: FragmentTransaction = (view?.context as HomeScreenActivity).supportFragmentManager.beginTransaction().replace(R.id.fragment_menu,foodDetailFragment)
            fragmentTransaction.commit()



        })


    }
}