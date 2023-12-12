package com.example.munchmate

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.ContactsContract.CommonDataKinds.Phone
import com.example.munchmate.databinding.ActivityPayOutBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener

class PayOutActivity : AppCompatActivity() {

    lateinit var binding : ActivityPayOutBinding
    private lateinit var auth:FirebaseAuth
   private lateinit var  address:String
   private lateinit var  phone:String
   private lateinit var  totalamount:String
   private lateinit var  foodItemName:ArrayList<String>
   private lateinit var  foodItemPrice:ArrayList<String>
   private lateinit var  foodItemDescription:ArrayList<String>
   private lateinit var  foodItemIngredient:ArrayList<String>
   private lateinit var  foodItemQuantities:ArrayList<Int>
   private lateinit var databaseReference: DatabaseReference
   private lateinit var userId:String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPayOutBinding.inflate(layoutInflater)
        setContentView(binding.root)
        //initialise firebase
        auth=FirebaseAuth.getInstance()
        //setuserData
        SetUserData()
        binding.placeMyOrderButton.setOnClickListener {
            val bottomSheetDialog = CongratsBottomSheet()
            bottomSheetDialog.show(supportFragmentManager,"test")
        }

    }
    private fun SetUserData() {
        val user = auth.currentUser
        if (user != null) {
            // Remove the 'val' keyword here
            userId = user.uid

            val userReference = databaseReference.child("user").child(userId)
            userReference.addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    if (snapshot.exists()) {
                        val names = snapshot.child("name").getValue(String::class.java) ?: ""
                        val addresss = snapshot.child("address").getValue(String::class.java) ?: ""
                        val phones = snapshot.child("phone").getValue(String::class.java) ?: ""
                        binding.apply {
                            name.setText(names)
                            address.setText(addresss)
                            phone.setText(phones)
                        }
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    TODO("Not yet implemented")
                }
            })
        }
    }


}