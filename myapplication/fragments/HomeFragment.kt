package com.sasha.myapplication.fragments

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.sasha.myapplication.R
import com.sasha.myapplication.Restaurant
import com.sasha.myapplication.adapterfiles.HomeRecyclerAdapter
import org.json.JSONException
import org.json.JSONObject


class HomeFragment : Fragment() {

    lateinit var recyclerDashboard: RecyclerView
    lateinit var layoutManager: RecyclerView.LayoutManager      //for lists
    lateinit var recyclerAdapter: HomeRecyclerAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_home, container, false)
        recyclerDashboard = view.findViewById(R.id.recyclerDashboard)

        val restaurantList = arrayListOf<Restaurant>()

        layoutManager = LinearLayoutManager(activity)


        val queue = Volley.newRequestQueue(activity as Context)
        val url = "http://13.235.250.119/v2/restaurants/fetch_result/"


            val jsonObjectRequest = object :
                JsonObjectRequest(Request.Method.GET, url, null, Response.Listener<JSONObject> {
                    //here we handle the response that we receive
                    try {
                        val data = it.getJSONObject("data")
                        val success = data.getBoolean("success")
                        if (success) {
                            val resArray = data.getJSONArray("data")


                            for (i in 0 until resArray.length()) {
                                val resObject = resArray.getJSONObject(i)
                                val restaurant =
                                    Restaurant(
                                        resObject.getString("id").toInt(),
                                        resObject.getString("name"),
                                        resObject.getString("rating"),
                                        resObject.getString("cost_for_one").toInt(),
                                        resObject.getString("image_url")

                                    )
                                restaurantList.add(restaurant)

                                recyclerAdapter =
                                    HomeRecyclerAdapter(
                                        activity as Context,
                                        restaurantList
                                    )

                                recyclerDashboard.adapter = recyclerAdapter

                                recyclerDashboard.layoutManager = layoutManager
                                //          to decorate with line  itemdecoration is a class
                                recyclerDashboard.addItemDecoration(
                                    DividerItemDecoration(
                                        recyclerDashboard.context,
                                        (layoutManager as LinearLayoutManager).orientation
                                    )
                                )
                            }



                        } else {
                            Toast.makeText(
                                activity as Context,
                                "Error occurred",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    } catch (e: JSONException) {
                         Toast.makeText(activity, "error $it", Toast.LENGTH_LONG)
                        .show()


                    }

                }, Response.ErrorListener {
                    println("No Internet Connection")
                }) {
                override fun getHeaders(): MutableMap<String, String> {         //code for sending the header content
                    val headers = HashMap<String, String>()
                    headers["Content-type"] = "application/json"
                    headers["token"] = "bf479684678793"
                    return headers
                }

            }




        queue.add(jsonObjectRequest)
        return view

    }
}

