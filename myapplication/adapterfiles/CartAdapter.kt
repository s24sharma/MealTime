package com.sasha.myapplication.adapterfiles

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.sasha.myapplication.FoodItem
import com.sasha.myapplication.activities.LastActivity
import com.sasha.myapplication.R

class CartAdapter (private val cartList:ArrayList<FoodItem>, val context: Context):
RecyclerView.Adapter<CartAdapter.CartViewHolder>(){


    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): CartViewHolder {
        val itemView = LayoutInflater.from(p0.context)
            .inflate(R.layout.recycler_row3, p0, false)

        return CartViewHolder(
            itemView
        )
    }

    override fun getItemCount(): Int {
        return cartList.size
    }


    override fun onBindViewHolder(p0: CartViewHolder, p1: Int) {
        val CartObject = cartList[p1]
        p0.itemName.text=CartObject.name
        val cost ="Rs. ${CartObject.cost_for_one?.toString()}"
       p0.itemCost.text=cost

       p0.btnplace.setOnClickListener{
           val intent2 = Intent( context, LastActivity::class.java)
                     //(context as AppCompatActivity).supportActionBar?.title=holder.txtName.text.toString() //for changing title when clicked
           context.startActivity(intent2)

        }
    }

    class CartViewHolder(view: View):RecyclerView.ViewHolder(view){
    val itemName:TextView=view.findViewById(R.id.txtMenuItem)
        val itemCost: TextView =view.findViewById(R.id.txtPrice)
        val btnplace:TextView=view.findViewById(R.id.btnPlace)
    }
}
