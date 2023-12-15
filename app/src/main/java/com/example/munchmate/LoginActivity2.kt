package com.example.munchmate

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.example.munchmate.databinding.ActivityLogin2Binding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.ktx.Firebase

class LoginActivity2 : AppCompatActivity() {
    private lateinit var password:String
    private lateinit var email:String
    private lateinit var database:FirebaseDatabase
    private lateinit var auth: FirebaseAuth

    private val binding:ActivityLogin2Binding by lazy {
        ActivityLogin2Binding.inflate(layoutInflater)
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        auth=Firebase.auth
        database=FirebaseDatabase.getInstance()
        binding.loginbtn.setOnClickListener{
            email=binding.emailAddress.text.toString().trim()
            password=binding.password.text.toString().trim()
            if(email.isEmpty()||password.isEmpty()){
                Toast.makeText(this,"Enter the Details Please",Toast.LENGTH_SHORT).show()
            }else{
                createUser()
                Toast.makeText(this,"Login Succesfull",Toast.LENGTH_SHORT).show()

            }

        }
        binding.donthavebtn.setOnClickListener{
            val intent = Intent(this,SignActivity::class.java)
            startActivity(intent)
        }
        binding.loginOtp.setOnClickListener {
            val i=Intent(this,ActivityPhone::class.java)
            startActivity(i)
        }
    }

    private fun createUser() {
      auth.signInWithEmailAndPassword(email,password).addOnCompleteListener { task->
          if(task.isSuccessful){
              val user=auth.currentUser
              updateUi(user)

          }else{
              Toast.makeText(this,"Bhai Zra SignIn krle please",Toast.LENGTH_SHORT).show()
              Log.d("Login","Pta nhi error aagya yar",task.exception)
          }
      }
    }

    private fun updateUi(user: FirebaseUser?) {
        val intent = Intent(this,MainActivity::class.java)
        startActivity(intent)
        finish()

    }
}