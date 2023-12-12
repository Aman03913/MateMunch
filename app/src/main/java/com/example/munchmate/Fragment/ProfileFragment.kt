package com.example.munchmate.Fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.munchmate.R
import com.example.munchmate.databinding.FragmentProfileBinding
import com.example.munchmate.model.UserModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class ProfileFragment : Fragment() {
    private lateinit var binding:FragmentProfileBinding
    private  val auth=FirebaseAuth.getInstance()
    private val database=FirebaseDatabase.getInstance()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        binding=FragmentProfileBinding.inflate(inflater,container,false)

        setUserData()
        binding.saveInfoButton.setOnClickListener {
            val
        }
        return binding.root
    }

    private fun setUserData() {
      val userId=auth.currentUser?.uid
        if(userId!=null){
            val useRefrence=database.getReference("user").child(userId)
            useRefrence.addListenerForSingleValueEvent(object :ValueEventListener{
                override fun onDataChange(snapshot: DataSnapshot) {
                    if(snapshot.exists()){
                        val userProfile=snapshot.getValue(UserModel::class.java)
                        if(userProfile!=null){
                            binding.name.setText(userProfile.name)
                            binding.address.setText(userProfile.address)
                            binding.phone.setText(userProfile.phone)
                            binding.email.setText(userProfile.email)


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