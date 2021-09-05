package com.sasha.myapplication.adapterfiles

import android.content.Context
import android.content.Intent
import android.os.AsyncTask
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat.startActivity
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.RecyclerView
import androidx.room.Room
import com.sasha.myapplication.fragments.HomeFragment
import com.sasha.myapplication.R
import com.sasha.myapplication.Restaurant
import com.sasha.myapplication.activities.RestaurantDetail
import com.sasha.myapplication.databaseRes.ResDatabase
import com.sasha.myapplication.databaseRes.ResEntity
import com.squareup.picasso.Picasso

class HomeRecyclerAdapter(val context: Context, val itemList: ArrayList<Restaurant>) :
    RecyclerView.Adapter<HomeRecyclerAdapter.HomeViewHolder>() {

    override fun onCreateViewHolder(p0: ViewGroup, viewType: Int): HomeViewHolder {
        val view = LayoutInflater.from(p0.context).inflate(R.layout.recycler_home_row, p0, false)

        return HomeViewHolder(
            view
        )
    }

    override fun getItemCount(): Int {
        return itemList.size              //to store the total no. of lists
    }

    override fun getItemViewType(position: Int): Int {
        return position
    }


    override fun onBindViewHolder(
        holder: HomeViewHolder,
        position: Int
    ) {       //we want to place all itmes on there positions
        val resObject = itemList.get(position)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            holder.l1content.clipToOutline = true

        }

        holder.txtName.text = resObject.name
        holder.txtRating.text = resObject.rating
        holder.txtPrice.text = resObject.cost_for_one.toString()

        // holder.imgImage.setBackgroundResource(restaurant.image_url)
        Picasso.get().load(resObject.image_url).into(holder.imgImage)

        val listOfFavourites =
            GetAllFavAsyncTask(
                context
            ).execute().get()

        if (listOfFavourites.isNotEmpty() && listOfFavourites.contains(resObject.id.toString())) {
            holder.imgFav.setImageResource(R.drawable.ic_favorite_black_24dp_checked)
        } else {
            holder.imgFav.setImageResource(R.drawable.ic_favorite_border_black_24dp)
        }


        holder.imgFav.setOnClickListener {                 //
            val restaurantEntity = ResEntity(
                resObject.id,
                resObject.name,
                resObject.rating,
                resObject.cost_for_one.toString(),
                resObject.image_url

            )
            if (!DBAsyncTask(
                    context,
                    restaurantEntity,
                    1
                ).execute().get()
            ) {
                val async = DBAsyncTask(
                    context,
                    restaurantEntity,
                    2
                ).execute()

                val result = async.get()
                if (result) {
                    holder.imgFav.setImageResource(R.drawable.ic_favorite_black_24dp_checked)
                }
            } else {
                val async =
                    DBAsyncTask(
                        context,
                        restaurantEntity,
                        3
                    ).execute()
                val result = async.get()

                if (result) {
                    holder.imgFav.setImageResource(R.drawable.ic_favorite_border_black_24dp)
                }
            }
        }


        holder.l1content.setOnClickListener {

            val fragment = HomeFragment()
            val args = Bundle()
            args.putInt("id", resObject.id as Int)
            args.putString("name", resObject.name)
            fragment.arguments = args
            val intent2 = Intent(context, RestaurantDetail::class.java)
            intent2.putExtra("id", resObject.id)
            intent2.putExtra("name", resObject.name)
            context.startActivity(intent2)

        }

    }


    class HomeViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        var txtName: TextView = view.findViewById(R.id.txtResName)
        var txtRating: TextView = view.findViewById(R.id.txtResRating)
        var txtPrice: TextView = view.findViewById(R.id.txtResPrice)
        var imgImage: ImageView = view.findViewById(R.id.imgImage)
        var l1content: RelativeLayout = view.findViewById(R.id.l1content)
        var imgFav: ImageView = view.findViewById(R.id.imgFav)
    }


    class DBAsyncTask(val context: Context, val resEntity: ResEntity, val mode: Int) :
        AsyncTask<Void, Void, Boolean>() {


        val db = Room.databaseBuilder(context, ResDatabase::class.java, "res-db").build()

        override fun doInBackground(vararg params: Void?): Boolean {

            when (mode) {

                1 -> {

                    // Check DB if the book is favourite or not
                    val res: ResEntity? = db.resDao().getResById(resEntity.res_id.toString())
                    db.close()
                    return res != null

                }

                2 -> {

                    // Save the book into DB as favourite
                    db.resDao().insertRes(resEntity)
                    db.close()
                    return true

                }

                3 -> {

                    // Remove the favourite book
                    db.resDao().deleteRes(resEntity)
                    db.close()
                    return true

                }
            }

            return false
        }

    }

    class GetAllFavAsyncTask(context: Context) : AsyncTask<Void, Void, List<String>>() {

        val db = Room.databaseBuilder(context, ResDatabase::class.java, "res-db").build()


        override fun doInBackground(vararg params: Void?): List<String> {
            val list = db.resDao().getAllRes()
            val listOfIds = arrayListOf<String>()
            for (i in list) {
                listOfIds.add(i.res_id.toString())

            }
            return listOfIds
        }

    }
}