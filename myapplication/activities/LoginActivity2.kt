package com.sasha.myapplication.activities

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.*
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.sasha.myapplication.R
import org.json.JSONException
import org.json.JSONObject

class LoginActivity2 : AppCompatActivity() {

    lateinit var imgImage: ImageView
    lateinit var etMobile: EditText
    lateinit var etPassword: EditText
    lateinit var btnLogin: Button
    lateinit var txtFp: TextView
    lateinit var txtSignUp: TextView
lateinit var sharedPreferences: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        sharedPreferences =
            getSharedPreferences(getString(R.string.preference_file_name), Context.MODE_PRIVATE)
        val isLoggedIn=sharedPreferences.getBoolean("isLoggedIn",false)
        setContentView(R.layout.activity_login2)

        if(isLoggedIn){
            val intent=Intent(this@LoginActivity2,
                NavigationActivity::class.java)
            startActivity(intent)
            finish()
        }

        imgImage = findViewById(R.id.imgImage)
        etMobile = findViewById(R.id.etMobile)
        etPassword = findViewById(R.id.etPassword)
        btnLogin = findViewById(R.id.btnLogin)
        txtFp = findViewById(R.id.txtFp)
        txtSignUp = findViewById(R.id.txtSignUp)

        txtSignUp.setOnClickListener() {
            val intent2 = Intent(this@LoginActivity2, RegisterActivity::class.java)
            startActivity(intent2)
            finish()
        }

        txtFp.setOnClickListener() {
            val intent2 = Intent(this@LoginActivity2, ForgotPasswordActivity2::class.java)
            startActivity(intent2)
            finish()
        }

        btnLogin.setOnClickListener() {


            val queue = Volley.newRequestQueue(this@LoginActivity2)
            val url = "http://13.235.250.119/v2/login/fetch_result/"
            val jsonParams = JSONObject()
            jsonParams.put("mobile_number", etMobile.text.toString())
            jsonParams.put("password", etPassword.text.toString())
            savePreferences()
            val jsonObjectRequest =
                object : JsonObjectRequest(Request.Method.POST, url, jsonParams, Response.Listener {

                    try {
                        val data = it.getJSONObject("data")
                        val success = data.getBoolean("success")
                        if (success) {

                            val response = data.getJSONObject("data")
                            sharedPreferences.edit()
                                .putString("user_id", response.getString("user_id")).apply()

                            sharedPreferences.edit()
                                .putString("user_name", response.getString("name")).apply()

                            sharedPreferences.edit()
                                .putString(
                                    "user_mobile_number",
                                    response.getString("mobile_number")).apply()


                            sharedPreferences.edit()
                                .putString("user_address", response.getString("address")).apply()

                            sharedPreferences.edit()
                                .putString("user_email", response.getString("email")).apply()

                            startActivity(Intent(this@LoginActivity2, NavigationActivity::class.java))
                            finish()
                        } else {
                            btnLogin.visibility = View.VISIBLE
                            txtFp.visibility = View.VISIBLE

                            Toast.makeText(
                                this@LoginActivity2,
                                "wrong credentials ",
                                Toast.LENGTH_LONG
                            )
                                .show()
                        }
                    } catch (e: JSONException) {
                        Toast.makeText(this@LoginActivity2, "No Internet Connection", Toast.LENGTH_LONG)
                            .show()
                    }

                }, Response.ErrorListener {
                    Toast.makeText(this@LoginActivity2, "No Internet Connection", Toast.LENGTH_LONG)
                        .show()
                }) {
                    override fun getHeaders(): MutableMap<String, String> {         //code for sending the header content
                        val headers = HashMap<String, String>()
                        headers["Content-type"] = "application/json"
                        headers["token"] = "bf479684678793"
                        return headers
                    }
                }
            queue.add(jsonObjectRequest)
        }
    }
fun savePreferences(){
    sharedPreferences.edit().putBoolean("isLoggedIn",true).apply()
}
}

