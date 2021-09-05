package com.sasha.myapplication.activities

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.sasha.myapplication.R
import org.json.JSONException
import org.json.JSONObject

class ForgotPasswordActivity2 : AppCompatActivity() {

   lateinit var etMobile3: EditText
    lateinit var etEmail: EditText
    lateinit var btnSubmit2: Button
lateinit var sharedPreferenc: SharedPreferences
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_forgot_password2)

        etMobile3=findViewById(R.id.etMobile3)
        etEmail=findViewById(R.id.etEmail)
        btnSubmit2=findViewById(R.id.btnSubmit2)

        sharedPreferenc =
            getSharedPreferences(getString(R.string.preference_file_name), Context.MODE_PRIVATE)
            val mobile=etMobile3.text.toString()

        btnSubmit2.setOnClickListener(){

            val queue = Volley.newRequestQueue(this@ForgotPasswordActivity2)
            val url = "http://13.235.250.119/v2/forgot_password/fetch_result/"
            val jsonParams = JSONObject()
            jsonParams.put("mobile_number", etMobile3.text.toString())
            jsonParams.put("email", etEmail.text.toString())

            savepref(mobile)

            val jsonObjectRequest =
                object : JsonObjectRequest(Request.Method.POST, url, jsonParams, Response.Listener {

                    try {
                        val data = it.getJSONObject("data")
                        val success = data.getBoolean("success")
                        if (success){
                            Toast.makeText(
                                this@ForgotPasswordActivity2,
                                "OTP sent to your EmailID ",
                                Toast.LENGTH_LONG
                            )
                                .show()

                            val response = data.getJSONObject("data")
                            sharedPreferenc.edit()
                                .putString(
                                    "mobile_number",
                                    response.getString("mobile_number")
                                ).apply()
                            val intent=Intent(this@ForgotPasswordActivity2, ForgotPasswordActivity::class.java)
                            intent.putExtra("user_mobile",etMobile3.toString())
                            startActivity(intent)

                        } else {


                            Toast.makeText(
                                this@ForgotPasswordActivity2,
                                "wrong credentials ",
                                Toast.LENGTH_LONG
                            )
                                .show()
                        }
                    } catch (e: JSONException) {

                        val intent=Intent(this@ForgotPasswordActivity2, ForgotPasswordActivity::class.java)
                        intent.putExtra("user_mobile",etMobile3.toString())
                        startActivity(intent)
                    }

                }, Response.ErrorListener {
                    Toast.makeText(this@ForgotPasswordActivity2, "No Internet Connection$it", Toast.LENGTH_LONG)
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
  fun  savepref(mobile:String){
        sharedPreferenc.edit().putString("user_mobile",mobile).apply()
    }
        }
        

