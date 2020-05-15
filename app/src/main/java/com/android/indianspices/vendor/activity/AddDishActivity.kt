package com.android.indianspices.user.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.core.view.get
import com.android.indianspices.R
import com.android.indianspices.model.Food
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import kotlinx.android.synthetic.main.add_dish.*


class AddDishActivity : AppCompatActivity()
{
    lateinit var dishName : EditText
    lateinit var dishDesc : EditText
    lateinit var dishPrice : EditText
    lateinit var dishDiscount : EditText
    lateinit var  foodCategory : Spinner
    lateinit var selectedCategory : String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.add_dish)

        dishName = findViewById(R.id.dishNameField)
        dishDesc = findViewById(R.id.descriptionField)
        dishPrice = findViewById(R.id.dishPriceField)
        dishDiscount = findViewById(R.id.dishOfferField)
        foodCategory = findViewById(R.id.categorySpinner)

//        val categoryDatabaseRef = FirebaseDatabase.getInstance().getReference("Category")

        val categories = resources.getStringArray(R.array.foodCategories)

        foodCategory.adapter = ArrayAdapter<String>(this, android.R.layout.simple_list_item_1,categories)

        foodCategory.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
            override fun onNothingSelected(p0: AdapterView<*>?) {}
            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                selectedCategory = foodCategory.selectedItem.toString()
            }
        }

        addNewDishBtn.setOnClickListener(){
            saveDish()
        }

    }

    private fun saveDish()
    {

        var name = dishName.text.toString().trim()
        var description = dishDesc.text.toString().trim()
        var price = dishPrice.text.toString().trim()
        var discount = dishDiscount.text.toString().trim()
        var category: String = selectedCategory[0].toString()

        // Food ID
        val ref = FirebaseDatabase.getInstance().getReference("Foods")
        val dishId = "40"


        // checking if name is given to the dish
        if(name.isEmpty()){
            dishName.error = "Please enter the dish name"
            return
        }

        if(description.isEmpty()){
            dishDesc.error = "Please enter the dish description"
            return
        }

        if(price.isEmpty()){
            dishPrice.error = "Please enter the dish price"
            return
        }

        if(discount.isEmpty()){
            dishDiscount.error = "Please enter a discount (0 if none)"
            return
        }

        val dish = Food(name, "https://hebbarskitchen.com/wp-content/uploads/mainPhotos/vada-pav-recipe-how-to-make-vada-pav-wada-pav-1-696x927.jpeg", description, price, discount, category)

        if (dishId != null) {
            ref.child(dishId).setValue(dish).addOnCompleteListener{
                Toast.makeText(applicationContext, "Dish Saved Successfully", Toast.LENGTH_LONG).show()
            }
        }

    }
}
