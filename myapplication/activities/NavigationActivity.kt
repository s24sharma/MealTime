package com.sasha.myapplication.activities

import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.widget.FrameLayout
import android.widget.TextView
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import com.google.android.material.navigation.NavigationView
import com.sasha.myapplication.*
import com.sasha.myapplication.fragments.*

class NavigationActivity : AppCompatActivity() {

    lateinit var drawerLayout: DrawerLayout
    lateinit var coordinatorLayout: CoordinatorLayout
    lateinit var frame: FrameLayout
    lateinit var navigationView: NavigationView
    var previousmenuItem:MenuItem?=null
    lateinit var sharedPreferences: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_navigation)

        drawerLayout=findViewById(R.id.drawerLayout)
        coordinatorLayout=findViewById(R.id.coordinatorLayout)
        frame=findViewById(R.id.frame)
        navigationView=findViewById(R.id.navigationView)

        supportActionBar?.setHomeButtonEnabled(true)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        val actionBarDrawerToggle= ActionBarDrawerToggle(this@NavigationActivity,drawerLayout,
            R.string.open_drawer,
            R.string.close_drawer
        )
        drawerLayout.addDrawerListener(actionBarDrawerToggle)   //icon fetch
        actionBarDrawerToggle.syncState()

        openHome()
        supportFragmentManager.beginTransaction()
            .replace(
                R.id.frame,
                HomeFragment()
            )
            .addToBackStack("Home")

        sharedPreferences = getSharedPreferences(getString(R.string.preference_file_name),
            Context.MODE_PRIVATE)

        navigationView.setNavigationItemSelectedListener {
            if(previousmenuItem!=null) {
                previousmenuItem?.isChecked = false
            }


            it.isCheckable=true
            it.isChecked=true
            previousmenuItem=it

            when (it.itemId) {
                R.id.Home -> openHome()
                R.id.Favourite -> {
                    val fragment= FavFragment()
                    val transaction=supportFragmentManager.beginTransaction()
                    transaction.replace(R.id.frame,fragment)
                    transaction.commit()
                    supportActionBar?.title="Favourite Restaurants"
                    drawerLayout.closeDrawers()
                }

                R.id.Profile -> {
                    val fragment= MyProfile()
                    val transaction=supportFragmentManager.beginTransaction()
                    transaction.replace(R.id.frame,fragment)
                    transaction.commit()
                    supportActionBar?.title="MyProfile"
                    drawerLayout.closeDrawers()
                }


                R.id.Order -> {
                    val fragment=
                        OrderHistory()
                    val transaction=supportFragmentManager.beginTransaction()
                    transaction.replace(R.id.frame,fragment)
                    transaction.commit()
                    supportActionBar?.title="Order History"
                    drawerLayout.closeDrawers()
                }
                //to close the drawer once clicked

                R.id.Faq -> {
                    val fragment= FaqFragment()
                    val transaction=supportFragmentManager.beginTransaction()
                    transaction.replace(R.id.frame,fragment)
                    transaction.commit()
                    supportActionBar?.title="FAQs"
                    drawerLayout.closeDrawers()
                }
                R.id.Logout -> {
                    openHome()
                    val dialog = AlertDialog.Builder(this@NavigationActivity)
                    dialog.setTitle("Logout")
                    dialog.setMessage("Are you sure?")
                    dialog.setPositiveButton("ok") { text, listener ->

                        sharedPreferences.edit().clear().apply()


                        val intent= Intent(this@NavigationActivity, LoginActivity2::class.java)
                        startActivity(intent)
                        finish()

                    }
                    dialog.setNegativeButton("cancel") { text, listener -> }
                    dialog.create()
                    dialog.show()


                }
            }



            return@setNavigationItemSelectedListener true
        }


    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val id= item.itemId
        if (id== android.R.id.home){  //get id of hamburger icon
            drawerLayout.openDrawer(GravityCompat.START)        // open drawer from left side
        }
        return super.onOptionsItemSelected(item)
    }

    fun openHome(){

        val fragment= HomeFragment()
        val transaction=supportFragmentManager.beginTransaction()
        transaction.replace(R.id.frame,fragment)
        transaction.commit()
        supportActionBar?.title="All Restaurants"
        drawerLayout.closeDrawers()
    }
    override fun onBackPressed() {
        val frag =supportFragmentManager.findFragmentById(R.id.frame)
        when(frag) {
            !is HomeFragment -> openHome()
            else->
                super.onBackPressed()

        }}
}
