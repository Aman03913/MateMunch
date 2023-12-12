package com.example.munchmate

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.example.munchmate.databinding.ActivitySignBinding // Import your generated binding class
import com.example.munchmate.model.UserModel
import com.google.android.gms.common.api.Api.Client
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

class SignActivity : AppCompatActivity() {
    private lateinit var email:String
    private lateinit var password:String
    private lateinit var username:String
    private lateinit var auth:FirebaseAuth
    private lateinit var database:DatabaseReference
//    private lateinit var googleSignInClient: GoogleSignInClient
    private val binding: ActivitySignBinding by lazy {
        ActivitySignBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root) // Use the root view from the binding
        auth= Firebase.auth
        //initialise dFirebase database
        database=Firebase.database.reference
        binding.createAccountButton.setOnClickListener {
            username=binding.userName.text.toString()
            email=binding.EmailAddress.text.toString().trim()
            password=binding.password.text.toString().trim()
            if(email.isEmpty()||password.isEmpty()||username.isEmpty()){
                Toast.makeText(this,"Please Fill the details",Toast.LENGTH_SHORT).show()
            }else{
                createAccount(email,password)
            }
        }

    }

    private fun createAccount(email: String, password: String) {
        auth.createUserWithEmailAndPassword(email,password).addOnCompleteListener { task->
            if(task.isSuccessful){
                Toast.makeText(this,"Account Created Succesfully",Toast.LENGTH_SHORT).show()
                savaUserData()
                startActivity(Intent(this,LoginActivity2::class.java))
                finish()
            }else{
                Toast.makeText(this,"Account Creation Failed",Toast.LENGTH_SHORT).show()
                Log.e("Account","create Account:Failure",task.exception)

            }
        }
    }

    private fun savaUserData() {
        //retrieve Data from input field
        username=binding.userName.text.toString()
        password=binding.password.text.toString().trim()
        email=binding.EmailAddress.text.toString().trim()
        val user=UserModel(username,email,password)
        val userId=FirebaseAuth.getInstance().currentUser!!.uid
        //save Data to Firebase database
        database.child("user").child(userId).setValue(user)
    }
}
