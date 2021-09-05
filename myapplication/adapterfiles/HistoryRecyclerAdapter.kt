package com.sasha.myapplication.adapterfiles

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.sasha.myapplication.R
import com.sasha.myapplication.databaseRes.OrderEntity
import com.sasha.myapplication.databaseRes.orderList
import org.json.JSONObject
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class HistoryRecyclerAdapter(
    val context: Context, val OrderList: ArrayList<orderList>

) :
    RecyclerView.Adapter<HistoryRecyclerAdapter.OrderHistoryViewHolder>() {


    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): OrderHistoryViewHolder {
        val view =
            LayoutInflater.from(context).inflate(R.layout.recycler_history_row, p0, false)
        return OrderHistoryViewHolder(
            view
        )
    }

    override fun getItemCount(): Int {
        return OrderList.size
    }

    override fun onBindViewHolder(p0: OrderHistoryViewHolder, p1: Int) {
        val orderHistoryObject = OrderList[p1]
        p0.txtResName.text = orderHistoryObject.restaurant_name
        p0.txtDate.text = formatDate(orderHistoryObject.order_placed_at)
        setUpRecycler(p0.recyclerResHistory, orderHistoryObject)

    }

    class OrderHistoryViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val txtResName: TextView = view.findViewById(R.id.txtHead)
        val txtDate: TextView = view.findViewById(R.id.txtDate)
        val recyclerResHistory: RecyclerView = view.findViewById(R.id.recyclerHistory)

    }

    private fun setUpRecycler(recyclerResHistory: RecyclerView, orderHistoryList: orderList) {
        val foodItemsList = ArrayList<OrderEntity>()
        for (i in 0 until orderHistoryList.foodItem.length()) {
            val foodJson = orderHistoryList.foodItem.getJSONObject(i)
            foodItemsList.add(
                OrderEntity(
                    foodJson.getString("res_id"),
                    foodJson.getString("food_items")
                )
            )
        }

        // val cartItemAdapter = recyclerAdapter( context,foodItemsList)
        val mLayoutManager = LinearLayoutManager(context)
        recyclerResHistory.layoutManager = mLayoutManager
        recyclerResHistory.itemAnimator = DefaultItemAnimator()
        //recyclerResHistory.adapter = cartAdapter
    }

    private fun formatDate(dateString: String): String? {
        val inputFormatter = SimpleDateFormat("dd-MM-yy HH:mm:ss", Locale.ENGLISH)
        val date: Date = inputFormatter.parse(dateString) as Date

        val outputFormatter = SimpleDateFormat("dd/MM/yyyy", Locale.ENGLISH)
        return outputFormatter.format(date)
    }
}
