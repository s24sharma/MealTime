package com.sasha.myapplication.activities

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.AsyncTask
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.RelativeLayout
import android.widget.Toast
import androidx.appcompat.app.ActionBar
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.room.Room
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.google.gson.Gson
import com.sasha.myapplication.ConnectionManager
import com.sasha.myapplication.FoodItem
import com.sasha.myapplication.R
import com.sasha.myapplication.adapterfiles.RestaurantDetailAdapter
import com.sasha.myapplication.databaseRes.OrderEntity
import com.sasha.myapplication.databaseRes.ResDatabase

class RestaurantDetail : AppCompatActivity() {

  lateinit var  recyclerDetail: RecyclerView
    lateinit var layoutManager: RecyclerView.LayoutManager
    lateinit var btnNext: Button
   // lateinit var recyclerAdapter: RestaurantDetailAdapter


    private lateinit var restaurantMenuAdapter: RestaurantDetailAdapter
    private var menuList = arrayListOf<FoodItem>()
    private lateinit var l2content: RelativeLayout
    private var orderLis = arrayListOf<FoodItem>()
    lateinit var sharedPreferences: SharedPreferences

    companion object {
       @SuppressLint("StaticFieldLeak")
       lateinit var goToCart: Button
       var resId: Int? = 0
        var resName=""
        var userId: Int? = 0
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_restaurant_detail)
        sharedPreferences =
            getSharedPreferences(getString(R.string.preference_file_name), Context.MODE_PRIVATE)
//        resId = getInt("restaurant_id",5)
      //  userId = getInt("user_id",99)
      //  resName=getString("name","")

        if (intent != null) {
            resId = intent.getIntExtra("restaurant_id",5)
            userId = intent.getIntExtra("user_id",99)
            resName =intent.getStringExtra("name")
        }

        goToCart = findViewById(
            R.id.btnNext
        )


        goToCart.setOnClickListener {
       // proceedToCart()
         /*   val data = Bundle()
            data.putInt("resId", resId as Int)
            data.putString("resName", resName)
            val intent = Intent(this@RestaurantDetail, LastActivity::class.java)
            intent.putExtra("data", data)
            startActivity(intent)
            finish()*/
            val intent = Intent(this@RestaurantDetail, LastActivity::class.java)
            //intent.putExtra("data", data)
            startActivity(intent)
            finish()
        }

        setUpRestaurantMenu()

    }


    private fun setUpRestaurantMenu() {

       recyclerDetail = findViewById(R.id.recyclerDetail)
        if (ConnectionManager().checkConnectivity(this@RestaurantDetail)) {

            val queue = Volley.newRequestQueue(this@RestaurantDetail)
            val url = "http://13.235.250.119/v2/restaurants/fetch_result/"
            val jsonObjectRequest = object :
                JsonObjectRequest(Method.GET, url + resId, null, Response.Listener {
                  //  rlLoading.visibility = View.GONE

                    try {
                        val data = it.getJSONObject("data")
                        val success = data.getBoolean("success")
                        if (success) {
                            val resArray = data.getJSONArray("data")
                            for (i in 0 until resArray.length()) {
                                val menuObject = resArray.getJSONObject(i)
                                val foodItem = FoodItem(
                                    menuObject.getString("id"),
                                    menuObject.getString("name"),
                                    menuObject.getString("cost_for_one").toInt(),
                                    menuObject.getString("restaurant_id")

                                )
                               menuList.add(foodItem)

                             restaurantMenuAdapter =
                                 RestaurantDetailAdapter(
                                     this@RestaurantDetail,
                                     menuList,
                                     object :
                                         RestaurantDetailAdapter.OnItemClickListener {
                                         override fun onAddItemClick(foodItem: FoodItem) {
                                             orderLis.add(foodItem)
                                             if (orderLis.size > 0) {
                                                 goToCart.visibility =
                                                     View.VISIBLE
                                                 RestaurantDetailAdapter.isCartEmpty =
                                                     false
                                             }
                                         }

                                         override fun onRemoveItemClick(foodItem: FoodItem) {
                                             orderLis.remove(foodItem)
                                             if (orderLis.isEmpty()) {
                                                 goToCart.visibility =
                                                     View.GONE
                                                 RestaurantDetailAdapter.isCartEmpty =
                                                     true
                                             }
                                         }
                                     })
                                val mLayoutManager = LinearLayoutManager(this@RestaurantDetail)
                                recyclerDetail.layoutManager = mLayoutManager
                                recyclerDetail.itemAnimator = DefaultItemAnimator()
                               recyclerDetail.adapter = restaurantMenuAdapter
                            }
                        }

                    } catch (e: Exception) {
                        e.printStackTrace()
                    }

                }, Response.ErrorListener {
                    Toast.makeText(this@RestaurantDetail, it.message, Toast.LENGTH_SHORT).show()
                }) {
                override fun getHeaders(): MutableMap<String, String> {
                    val headers = HashMap<String, String>()
                    headers["Content-type"] = "application/json"
                    headers["token"] = "bf479684678793"
                    return headers
                }
            }
            queue.add(jsonObjectRequest)
        } else {
            Toast.makeText(this@RestaurantDetail, "No Internet Connection", Toast.LENGTH_SHORT).show()
        }
    }


  private fun proceedToCart() {

       val gson = Gson()

       val foodItems = gson.toJson(orderLis)

        val async = ItemsOfCart(
            this@RestaurantDetail,
            resId.toString(),
            foodItems,
            1
        ).execute()
        val result = async.get()
        if (result) {
            val data = Bundle()
            data.putInt("resId", resId as Int)
            data.putString("resName",
                resName
            )
         val intent = Intent(this@RestaurantDetail, CartActivity::class.java)
          intent.putExtra("data", data)
          startActivity(intent)
        } else {
            Toast.makeText((this@RestaurantDetail), "Some unexpected error", Toast.LENGTH_SHORT)
                .show()
        }

    }

   class ItemsOfCart(
        context: Context,
        val restaurantId: String,
        val foodItems: String,
        val mode: Int
    ) : AsyncTask<Void, Void, Boolean>() {
        val db = Room.databaseBuilder(context, ResDatabase::class.java, "res-db").build()


        override fun doInBackground(vararg params: Void?): Boolean {
            when (mode) {
                1 -> {
                        db.orderDao().insertOrder(OrderEntity(restaurantId, foodItems))
                        db.close()
                    return true
                }

                2 -> {
                    db.orderDao().deleteOrder(OrderEntity(restaurantId, foodItems))
                    db.close()
                    return true
                }
            }

            return false
        }

    }

}
