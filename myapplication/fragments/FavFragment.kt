package com.sasha.myapplication.fragments

import android.content.Context
import android.os.AsyncTask
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.room.Room
import com.sasha.myapplication.R
import com.sasha.myapplication.Restaurant
import com.sasha.myapplication.adapterfiles.HomeRecyclerAdapter
import com.sasha.myapplication.databaseRes.ResDatabase
import com.sasha.myapplication.databaseRes.ResEntity

/**
 * A simple [Fragment] subclass.
 */
class FavFragment : Fragment() {

    lateinit var recyclerFav: RecyclerView
    lateinit var homeRecyclerAdapter: HomeRecyclerAdapter

    var restaurantList = arrayListOf<Restaurant>()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_fav, container, false)

        setUpRecycler(view)

        return view
    }

   private fun setUpRecycler(view:View){

       recyclerFav=view.findViewById(R.id.recyclerFav)
            val backgroundList= FavouritesAsync(
                activity as Context
            ).execute().get()


            for( i in backgroundList){

                restaurantList.add(
                    Restaurant(
                        i.res_id,
                        i.name,
                        i.rating,
                        i.cost_for_one.toInt(),
                        i.image_url
                    )
                )
            }
            homeRecyclerAdapter=
                HomeRecyclerAdapter(
                    activity as Context,
                    restaurantList
                )
            val mLayoutManager= LinearLayoutManager(activity)
            recyclerFav.layoutManager=mLayoutManager
            recyclerFav.adapter=homeRecyclerAdapter
            recyclerFav.itemAnimator=DefaultItemAnimator()

        }
}
    class FavouritesAsync(context: Context):AsyncTask<Void ,Void, List<ResEntity>>(){
        val db = Room.databaseBuilder(context, ResDatabase::class.java,"res-db").build()

        override fun doInBackground(vararg params: Void?): List<ResEntity> {

           return db.resDao().getAllRes()
        }

    }


