package com.example.munchmate.Fragment

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.munchmate.PayOutActivity
import com.example.munchmate.adapter.CartAdapter
import com.example.munchmate.databinding.FragmentCartBinding
import com.example.munchmate.model.CartItems
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*

class CartFragment : Fragment() {
    private lateinit var binding: FragmentCartBinding
    private lateinit var auth: FirebaseAuth
    private lateinit var database: FirebaseDatabase
    private lateinit var foodNames: MutableList<String>
    private lateinit var foodPrices: MutableList<String>
    private lateinit var foodDescriptions: MutableList<String>
    private lateinit var foodImagesUri: MutableList<String>
    private lateinit var foodIngredients: MutableList<String>
    private lateinit var quantity: MutableList<Int>
    private lateinit var userId: String
    private lateinit var cartAdapter: CartAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentCartBinding.inflate(inflater, container, false)
        auth = FirebaseAuth.getInstance()
        reteriveCartItems()

        binding.proceedButton.setOnClickListener {
            //get orderItem details before Procedding out
            getOrderItemsDetail()
        }

        return binding.root
    }

    private fun getOrderItemsDetail() {
        database = FirebaseDatabase.getInstance()
        val orderIdReference: DatabaseReference =
            database.reference.child("user").child(userId).child("cartItems")
        val foodName = mutableListOf<String>()
        val foodPrice = mutableListOf<String>()
        val foodImage = mutableListOf<String>()
        val foodDescription = mutableListOf<String>()
        val foodIngredient = mutableListOf<String>()
        val foodQuantities = cartAdapter.getUpdatedItemQuantities()
        orderIdReference.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                for (foodSnapshot in snapshot.children) {
                    //get the cart items to the respective list
                    val orderItems = foodSnapshot.getValue(CartItems::class.java)
                    //add items details to the list
                    orderItems?.foodName?.let { foodName.add(it) }
                    orderItems?.foodPrice?.let { foodName.add(it) }
                    orderItems?.foodDescription?.let { foodName.add(it) }
                    orderItems?.foodImage?.let { foodName.add(it) }
                    orderItems?.foodIngredient?.let { foodName.add(it) }

                }
                orderNow(
                    foodName,
                    foodPrice,
                    foodDescription,
                    foodImage,
                    foodIngredient,
                    foodQuantities
                )
            }

            private fun orderNow(
                foodName: MutableList<String>,
                foodPrice: MutableList<String>,
                foodDescription: MutableList<String>,
                foodImage: MutableList<String>,
                foodIngredient: MutableList<String>,
                foodQuantities: MutableList<Int>
            ) {
                if (isAdded && context != null) {
                    val intent = Intent(requireContext(), PayOutActivity::class.java)
                    intent.putExtra("FoodItemName", foodName as ArrayList<String>)
                    intent.putExtra("FoodItemPrice", foodPrice as ArrayList<String>)
                    intent.putExtra("FoodItemImage", foodImage as ArrayList<String>)
                    intent.putExtra("FoodItemDescription", foodDescription as ArrayList<String>)
                    intent.putExtra("FoodItemIngredient", foodIngredient as ArrayList<String>)
                    intent.putExtra("FoodItemQuantities", foodQuantities as ArrayList<Int>)
                    startActivity(intent)
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(context, "Order Making failed please try Again", Toast.LENGTH_SHORT)
                    .show()
            }

        })

    }

    private fun reteriveCartItems() {
        // Initialize foodIngredients list
        foodIngredients = mutableListOf()

        // Data
        database = FirebaseDatabase.getInstance()
        userId = auth.currentUser?.uid ?: ""
        val foodReference: DatabaseReference =
            database.reference.child("user").child(userId).child("cartItems")
        // List to store cart Items
        foodNames = mutableListOf()
        foodPrices = mutableListOf()
        foodDescriptions = mutableListOf()
        foodImagesUri = mutableListOf()
        quantity = mutableListOf()

        // Fetch data from the database
        foodReference.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                for (foodSnapshot in snapshot.children) {
                    val orderItems = foodSnapshot.getValue(CartItems::class.java)
                    // add items details to the respective lists
                    orderItems?.foodName?.let { foodNames.add(it) }
                    orderItems?.foodPrice?.let { foodPrices.add(it) }
                    orderItems?.foodDescription?.let { foodDescriptions.add(it) }
                    orderItems?.foodImage?.let { foodImagesUri.add(it) }
                    orderItems?.foodIngredient?.let { foodIngredients.add(it) }
                }
                setAdapter()
            }

            private fun setAdapter() {
                cartAdapter = CartAdapter(
                        requireContext(),
                        foodNames,
                        foodPrices,
                        foodDescriptions,
                        foodImagesUri,
                        quantity,
                        foodIngredients
                    )
                binding.cartRecyclerView.layoutManager =
                    LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
                binding.cartRecyclerView.adapter =cartAdapter
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(context, "Data not fetched", Toast.LENGTH_SHORT).show()
            }
        })
    }

    companion object {
        // Any companion object members if needed
    }
}
