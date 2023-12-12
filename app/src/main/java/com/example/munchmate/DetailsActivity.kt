package com.example.munchmate

import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.bumptech.glide.Glide
import com.example.munchmate.databinding.ActivityDetailsBinding
import com.example.munchmate.model.CartItems
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase

class DetailsActivity : AppCompatActivity() {
    private lateinit var binding:ActivityDetailsBinding
    private var foodName: String?=null
    private var foodImage: String?=null
    private var foodDescription: String?=null
    private var foodIngredients: String?=null
    private var foodPrice: String?=null
    private lateinit var auth:FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        auth=FirebaseAuth.getInstance()
        foodName=intent.getStringExtra("MenuItemName")
        foodDescription=intent.getStringExtra("MenuItemDescription")
        foodIngredients=intent.getStringExtra("MenuItemIngredients")
        foodPrice=intent.getStringExtra("MenuItemPrice")
        foodImage=intent.getStringExtra("MenuItemImage")
         with(binding){
             detailFoodName.text=foodName
             detailDescription.text=foodDescription
             detailIngredients.text=foodIngredients
             Glide.with(this@DetailsActivity).load(Uri.parse(foodImage)).into(detailFoodImage)
         }
        binding.AddItemButton.setOnClickListener {
            addItemToCart()
        }
        binding.imageButton.setOnClickListener {
            finish()
        }
    }

    private fun addItemToCart() {
        val database=FirebaseDatabase.getInstance().reference
        val userID=auth.currentUser?.uid?:""
        //create a cart item object
        val cartItem=CartItems(foodName.toString(),foodPrice.toString(),foodDescription.toString(),foodImage.toString(),1)
        //save data to cart items to firebase
        database.child("user").child(userID).child("cartItems").push().setValue(cartItem).addOnSuccessListener {
            Toast.makeText(this,"Item Add into Cart is Succefully",Toast.LENGTH_SHORT).show()
        }.addOnFailureListener {
            Toast.makeText(this,"Item Not Added to Cart",Toast.LENGTH_SHORT).show()
        }
    }
}