package com.sasha.myapplication.fragments

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.FragmentActivity
import com.sasha.myapplication.R

/**
 * A simple [Fragment] subclass.
 */
class MyProfile : Fragment() {

    lateinit var txtName: TextView
    lateinit var txtMobile: TextView
    lateinit var txtEmail :TextView
    lateinit var txtAddress: TextView
    lateinit var sharedPref: SharedPreferences
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        // Inflate the layout for this fragment
        val view =inflater.inflate(R.layout.fragment_my_profile, container, false)

      // (activity as DrawerLocker).setDrawerEnabled(true)
        sharedPref =
            (activity as FragmentActivity).getSharedPreferences("Restaurant Preferences", Context.MODE_PRIVATE)

        txtName=view.findViewById(R.id.txtName)
        txtMobile=view.findViewById(R.id.txtMobile)
        txtEmail=view.findViewById(R.id.txtEmail)
        txtAddress=view.findViewById(R.id.txtAddress)
        txtName=view.findViewById(R.id.txtName)

        txtName.text=sharedPref.getString("user_name","no")
        txtMobile.text= sharedPref.getString("user_mobile_number",null)
        txtEmail.text= sharedPref.getString("user_email",null)
        txtAddress.text= sharedPref.getString("user_address",null)
    return view
    }

}
