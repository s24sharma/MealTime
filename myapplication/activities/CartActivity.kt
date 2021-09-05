package com.sasha.myapplication.activities

import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.AsyncTask
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.room.Room
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.google.gson.Gson
import com.sasha.myapplication.*
import com.sasha.myapplication.activities.RestaurantDetail.Companion.resId
import com.sasha.myapplication.activities.RestaurantDetail.Companion.resName
import com.sasha.myapplication.adapterfiles.CartAdapter
import com.sasha.myapplication.adapterfiles.RestaurantDetailAdapter
import com.sasha.myapplication.databaseRes.OrderEntity
import com.sasha.myapplication.databaseRes.ResDatabase
import kotlinx.android.synthetic.main.recycler_history_row.*
import org.json.JSONArray
import org.json.JSONObject

class CartActivity : AppCompatActivity() {

    var user_id: Int? = 99
    var res_id: Int? = 5
    lateinit var recyclerCart: RecyclerView
    lateinit var btnPlace: Button
    lateinit var txtMenuItem: TextView
    lateinit var txtPrice: TextView
    lateinit var sharedPreferences: SharedPreferences
    private lateinit var cartAdapter: CartAdapter
    lateinit var orderLis: ArrayList<FoodItem>
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cart)
        sharedPreferences =
            getSharedPreferences(getString(R.string.preference_file_name), Context.MODE_PRIVATE)
        init()
        setUpCartList()
        placeOrder()
    }

    private fun init() {
        recyclerCart = findViewById(R.id.recyclerCart)
        btnPlace = findViewById(R.id.btnPlace)
        val bundle = intent.getBundleExtra("data")
        resId = bundle?.getInt("Id", 0) as Int                  //
        resName = bundle.getString("resName", "") as String

    }

    private fun setUpCartList() {
        recyclerCart = findViewById(R.id.recyclerCart)
        //var orderListt:ArrayList<FoodItem>

        val dbList =
            GetItemsFromDBAsync(
                applicationContext
            ).execute().get()

        for (element in dbList) {
            orderLis.addAll(
                Gson().fromJson(element.foodItems, Array<FoodItem>::class.java).asList()
            )
        }

        /*If the order list extracted from DB is empty we do not display the cart/
        if (orderList.isEmpty()) {
            rlCart.visibility = View.GONE
            rlLoading.visibility = View.VISIBLE
        } else {
            rlCart.visibility = View.VISIBLE
            rlLoading.visibility = View.GONE
        }*/

        // /Else we display the cart using the cart item adapter/
        cartAdapter =
            CartAdapter(
                orderLis,
                this@CartActivity
            )
        val mLayoutManager = LinearLayoutManager(this@CartActivity)
        recyclerCart.layoutManager = mLayoutManager
        recyclerCart.itemAnimator = DefaultItemAnimator()
        recyclerCart.adapter = cartAdapter
    }


    private fun placeOrder() {
        btnPlace = findViewById(R.id.btnPlace)

        //Before placing the order, the user is displayed the price or the items on the button for placing the orders/
        var sum = 0
        for (i in 0 until orderLis.size) {
            sum += orderLis[i].cost_for_one as Int
        }
        val total = "Place Order(Total: Rs. $sum)"
        btnPlace.text = total

        btnPlace.setOnClickListener {
            r1Loading.visibility = View.VISIBLE
            //rlCart.visibility = View.INVISIBLE
            sendServerRequest()
        }
    }

    private fun sendServerRequest() {
        val queue = Volley.newRequestQueue(this)

        //Creating the json object required for placing the order/
        val jsonParams = JSONObject()
        jsonParams.put(
            "user_id",
            this@CartActivity.getSharedPreferences("Restaurant Preferences", Context.MODE_PRIVATE)
                .getString(
                    "user_id",
                    null
                ) as String
        )
        jsonParams.put("restaurant_id", RestaurantDetail.resId?.toString() as String)
        var sum = 0

        for (i in 0 until orderLis.size) {
            sum += orderLis[i].cost_for_one as Int
        }
        jsonParams.put("total_cost", sum.toString())
        val foodArray = JSONArray()
        for (i in 0 until orderLis.size) {
            val foodId = JSONObject()
            foodId.put("food_item_id", orderLis[i].id)
            foodArray.put(i, foodId)
        }
        jsonParams.put("food", foodArray)
        val url = "http://13.235.250.119/v2/place_order/fetch_result/"
        val jsonObjectRequest =
            object : JsonObjectRequest(Method.POST, url, jsonParams, Response.Listener {

                try {
                    val data = it.getJSONObject("data")
                    val success = data.getBoolean("success")

                    if (success) {
                        val clearCart =
                            ClearDBAsync(
                                applicationContext,
                                resId.toString()
                            ).execute().get()
                        RestaurantDetailAdapter.isCartEmpty = true


                        val dialog = Dialog(
                            this@CartActivity,
                            android.R.style.Theme_Black_NoTitleBar_Fullscreen
                        )
                        // dialog.setContentView(R.layout.order_placed_dialog)
                        dialog.show()
                        dialog.setCancelable(false)
                        val btnOk = dialog.findViewById<Button>(R.id.btnOk)
                        btnOk.setOnClickListener {
                            dialog.dismiss()
                            startActivity(Intent(this@CartActivity, NavigationActivity::class.java))
                            ActivityCompat.finishAffinity(this@CartActivity)
                        }
                    } else {
                        // rlCart.visibility = View.VISIBLE
                        Toast.makeText(this@CartActivity, "Some Error occurred", Toast.LENGTH_SHORT)
                            .show()
                    }

                } catch (e: Exception) {
                    // rlCart.visibility = View.VISIBLE
                    e.printStackTrace()
                }

            }, Response.ErrorListener {
                // rlCart.visibility = View.VISIBLE
                Toast.makeText(this@CartActivity, it.message, Toast.LENGTH_SHORT).show()
            }) {
                override fun getHeaders(): MutableMap<String, String> {
                    val headers = HashMap<String, String>()
                    headers["Content-type"] = "application/json"
                    headers["token"] = "bf479684678793"
                    return headers
                }
            }

        queue.add(jsonObjectRequest)

    }


    class GetItemsFromDBAsync(context: Context) : AsyncTask<Void, Void, List<OrderEntity>>() {
        val db = Room.databaseBuilder(context, ResDatabase::class.java, "res-db").build()
        override fun doInBackground(vararg params: Void?): List<OrderEntity> {
            return db.orderDao().getAllOrders()
        }

    }


    class ClearDBAsync(context: Context, val resId: String) : AsyncTask<Void, Void, Boolean>() {
        val db = Room.databaseBuilder(context, ResDatabase::class.java, "res-db").build()
        override fun doInBackground(vararg params: Void?): Boolean {
            db.orderDao().deleteOrders(resId)
            db.close()
            return true
        }

    }


    override fun onSupportNavigateUp(): Boolean {
        val clearCart =
            ClearDBAsync(
                applicationContext,
                resId.toString()
            ).execute().get()
        RestaurantDetailAdapter.isCartEmpty = true
        onBackPressed()
        return true
    }
}