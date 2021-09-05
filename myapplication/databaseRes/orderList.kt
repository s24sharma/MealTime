package com.sasha.myapplication.databaseRes

import org.json.JSONArray

data class orderList(
    var order_id:Int,
    var restaurant_name:String,
    var order_placed_at:String,
    val foodItem: JSONArray
)