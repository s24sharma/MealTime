package com.sasha.myapplication.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import com.sasha.myapplication.R

class LastActivity : AppCompatActivity() {

    lateinit var btnOk: Button
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_last)
        btnOk=findViewById(R.id.btnOk)

        btnOk.setOnClickListener{

            val intent= Intent(this@LastActivity,
                NavigationActivity::class.java)
            startActivity(intent)
            finish()

        }
    }
}
