package com.sasha.myapplication.adapterfiles

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.sasha.myapplication.FoodItem
import com.sasha.myapplication.R


class RestaurantDetailAdapter(
    val context: Context,
    private val menuList: ArrayList<FoodItem>,
    private val listener: OnItemClickListener
    ) :
        RecyclerView.Adapter<RestaurantDetailAdapter.MenuViewHolder>() {

        companion object {
            var isCartEmpty = true
        }

        override fun onCreateViewHolder(p0: ViewGroup, p1: Int): MenuViewHolder {
            val itemView = LayoutInflater.from(p0.context)
                .inflate(R.layout.recycler_row2, p0, false)

            return MenuViewHolder(
                itemView
            )
        }

        override fun getItemCount(): Int {
            return menuList.size
        }

        interface OnItemClickListener {
            fun onAddItemClick(foodItem: FoodItem)
            fun onRemoveItemClick(foodItem: FoodItem)
        }


        override fun onBindViewHolder(p0: MenuViewHolder, p1: Int) {
            val menuObject = menuList[p1]
            p0.removeFromCart.visibility = View.GONE
            p0.foodItemName.text = menuObject.name
           val cost = "Rs. ${menuObject.cost_for_one?.toString()}"
            p0.foodItemCost.text = cost
            p0.sno.text = (p1 + 1).toString()
            p0.addToCart.setOnClickListener {
                p0.addToCart.visibility = View.GONE
                p0.removeFromCart.visibility = View.VISIBLE
                listener.onAddItemClick(menuObject)

                p0.addToCart.visibility = View.GONE
                p0.removeFromCart.visibility = View.VISIBLE
                listener.onAddItemClick(menuObject)
            }

            p0.removeFromCart.setOnClickListener {
                p0.removeFromCart.visibility = View.GONE
                p0.addToCart.visibility = View.VISIBLE
                listener.onRemoveItemClick(menuObject)
            }


        }

        override fun getItemViewType(position: Int): Int {
            return position
        }

        class MenuViewHolder(view: View) : RecyclerView.ViewHolder(view) {
            val foodItemName: TextView = view.findViewById(R.id.txtMenu)
            val foodItemCost: TextView = view.findViewById(R.id.txtPrice)
           val sno: TextView = view.findViewById(R.id.txtSno)
            val addToCart: Button = view.findViewById(R.id.btnAdd)
            val removeFromCart: Button = view.findViewById(R.id.btnRemove)

        }
    }
