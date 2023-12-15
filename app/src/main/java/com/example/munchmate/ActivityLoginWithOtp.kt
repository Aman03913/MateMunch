package com.example.munchmate

import android.content.ContentValues.TAG
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.example.munchmate.databinding.ActivityLoginWithOtpBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthProvider
import com.google.firebase.auth.auth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class ActivityLoginWithOtp : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth
    private lateinit var binding: ActivityLoginWithOtpBinding
    private var storedVerificationId: String? = ""


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginWithOtpBinding.inflate(layoutInflater)
        setContentView(binding.root)
        auth= Firebase.auth
        storedVerificationId=intent.getStringExtra("storedVerificationId")
        binding.verifyOTP.setOnClickListener {
                   verifyPhoneNumberWithCode(storedVerificationId,binding.enterotp.text.toString())

        }
    }
    private fun verifyPhoneNumberWithCode(verificationId: String?, code: String) {
        // [START verify_with_code]
        val credential = PhoneAuthProvider.getCredential(verificationId!!, code)
        // [END verify_with_code]
        signInWithPhoneAuthCredential(credential)
    }

    private fun signInWithPhoneAuthCredential(credential: PhoneAuthCredential) {
        auth.signInWithCredential(credential)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    Log.d("Sucess", "signInWithCredential:success")

                    val user = task.result?.user
                    //[pass user
                    val intent= Intent(this@ActivityLoginWithOtp,MainActivity::class.java)
                    startActivity(intent)
                } else {
                    // Sign in failed, display a message and update the UI
                    Log.w("faied", "signInWithCredential:failure", task.exception)
                    if (task.exception is FirebaseAuthInvalidCredentialsException) {
                        // The verification code entered was invalid
                    }
                    // Update UI
                }
            }
    }
}
