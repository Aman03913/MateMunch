package com.example.munchmate

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.munchmate.adapter.NotificationAdapter
import com.example.munchmate.databinding.FragmentNotificationBottomBinding
import com.google.android.material.bottomsheet.BottomSheetDialogFragment


class notification_Bottom_Fragment : BottomSheetDialogFragment(){
    private lateinit var binding : FragmentNotificationBottomBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentNotificationBottomBinding.inflate(layoutInflater,container,false)
        val notifications = listOf("Your Order has been Cancelled Successfully","Order has been taken by the Driver","Congrats Your order has been Placed")
        val notificationImages = listOf(R.drawable.sadicon,R.drawable.truckicon,R.drawable.righttick)
        val adapter = NotificationAdapter(
            ArrayList(notifications),ArrayList(notificationImages)
        )
binding.notificationRecyclerView.layoutManager = LinearLayoutManager(requireContext())
        binding.notificationRecyclerView.adapter = adapter
        return binding.root
    }

    companion object {

        fun newInstance(param1: String, param2: String) =
            notification_Bottom_Fragment().apply {

            }
    }
}