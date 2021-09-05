package com.sasha.myapplication.fragments

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RelativeLayout
import android.widget.Toast
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.sasha.myapplication.R
import com.sasha.myapplication.adapterfiles.HistoryRecyclerAdapter
import com.sasha.myapplication.adapterfiles.HomeRecyclerAdapter
import com.sasha.myapplication.databaseRes.orderList

/**
 * A simple [Fragment] subclass.
 */
class OrderHistory : Fragment() {

    lateinit var recyclerHistory: RecyclerView
    lateinit var layoutManager: RecyclerView.LayoutManager
    lateinit var historyRecyclerAdapter: HistoryRecyclerAdapter
    lateinit var r1Loading: RelativeLayout
    lateinit var sharedPreferences: SharedPreferences
    lateinit var recyclerAdapter: HistoryRecyclerAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_order_history, container, false)


        recyclerHistory = view.findViewById(R.id.recyclerHistory)
//        r1Loading = view.findViewById(R.id.r1Loading)
        //    r1Loading.visibility = View.VISIBLE
        val order = arrayListOf<orderList>()
        layoutManager = LinearLayoutManager(activity)


        sharedPreferences =
            (activity as Context).getSharedPreferences(
                getString(R.string.preference_file_name),
                Context.MODE_PRIVATE
            )
        val userId = sharedPreferences.getString("user_id", "user_id")


        val queue = Volley.newRequestQueue(activity as Context)
        val url = "http://13.235.250.119/v2/orders/fetch_result/"
        val jsonObjectRequest = object :
            JsonObjectRequest(Method.GET, url + userId, null, Response.Listener {
                // r1Loading.visibility = View.GONE
                try {
                    val data = it.getJSONObject("data")
                    val success = data.getBoolean("success")
                    if (success) {
                        val resArray = data.getJSONArray("data")

                        for (i in 0 until resArray.length()) {
                            val orderObject = resArray.getJSONObject(i)
                            val foodItems = orderObject.getJSONArray("food_items")
                            val orderDetails = orderList(
                                orderObject.getInt("order_id"),
                                orderObject.getString("restaurant_name"),
                                orderObject.getString("order_placed_at"),
                                foodItems
                            )
                            order.add(orderDetails)


                            /* if (orderList.isEmpty()) {
                                llHasOrders.visibility = View.GONE
                                rlNoOrders.visibility = View.VISIBLE
                            } else {
                                llHasOrders.visibility = View.VISIBLE
                                rlNoOrders.visibility = View.GONE
                                if (activity != null) {
                                    historyRecyclerAdapter = HistoryRecyclerAdapter(
                                        activity as Context,
                                        orderHistoryList*/


                            /*recyclerAdapter=HistoryRecyclerAdapter(activity as Context, order)
                            recyclerHistory.adapter = recyclerAdapter
                            val mLayoutManager =
                                LinearLayoutManager(activity as Context)
                            recyclerHistory.layoutManager = mLayoutManager
                            recyclerHistory.itemAnimator = DefaultItemAnimator()*/



                            recyclerAdapter =
                                HistoryRecyclerAdapter(
                                    activity as Context,
                                    order
                                )

                            recyclerHistory.adapter = recyclerAdapter

                            recyclerHistory.layoutManager = layoutManager
                            //          to decorate with line  itemdecoration is a class
                            recyclerHistory.addItemDecoration(
                                DividerItemDecoration(
                                    recyclerHistory.context,
                                    (layoutManager as LinearLayoutManager).orientation
                                )
                            )

                        }

                    }


                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }, Response.ErrorListener {
                Toast.makeText(activity as Context, it.message, Toast.LENGTH_SHORT).show()
            }) {
            override fun getHeaders(): MutableMap<String, String> {
                val headers = HashMap<String, String>()
                headers["Content-type"] = "application/json"

                /*The below used token will not work, kindly use the token provided to you in the training*/
                headers["token"] = "bf479684678793"
                return headers
            }
        }
        queue.add(jsonObjectRequest)
        return view
    }
}






