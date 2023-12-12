package com.example.munchmate

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.munchmate.adapter.MenuAdapter
import com.example.munchmate.databinding.FragmentMenuBottomSheetBinding
import com.example.munchmate.model.MenuItem
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener


class MenuBottomSheetFragment : BottomSheetDialogFragment(){
    private lateinit var binding: FragmentMenuBottomSheetBinding
    private lateinit var database:FirebaseDatabase
    private lateinit var menuItems: MutableList<MenuItem>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    @SuppressLint("SuspiciousIndentation")
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
      binding = FragmentMenuBottomSheetBinding.inflate(inflater,container,false)
        binding.buttonBack.setOnClickListener{
            dismiss()
        }
       retrieveMenuItems()
        return binding.root

    }

    private fun retrieveMenuItems() {
        database = FirebaseDatabase.getInstance()
        val foodRef: DatabaseReference = database.reference.child("menu")
        menuItems = mutableListOf()
        foodRef.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                for (foodSnapshot in snapshot.children) {
                    val menuItem = foodSnapshot.getValue(MenuItem::class.java)
                    menuItem?.let { menuItems.add(it) }
                }
                Log.d("Items", "Data received")
                setAdapter()
            }

            override fun onCancelled(error: DatabaseError) {
                Log.e("Items", "Error fetching data: $error")
            }
        })
    }

    private fun setAdapter() {
        // Check if the fragment is attached to a context
        if (isAdded) {
            if (menuItems.isNotEmpty()) {
                val adapter = MenuAdapter(menuItems, requireContext())
                binding.menuRecyclerView.layoutManager = LinearLayoutManager(requireContext())
                binding.menuRecyclerView.adapter = adapter
                Log.d("Items", "Adapter data set")
            } else {
                Log.e("Items", "Adapter data not set: menuItems is empty")
            }
        } else {
            Log.e("Items", "Fragment not attached to a context")
        }
    }



    companion object {

    }
}