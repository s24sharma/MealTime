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
import kotlinx.android.synthetic.main.activity_forgot_password2.*
import kotlinx.android.synthetic.main.fragment_my_profile.*
import org.json.JSONException
import org.json.JSONObject

class ForgotPasswordActivity : AppCompatActivity() {

    lateinit var etOtp: EditText
    lateinit var etPassword3: EditText
    lateinit var etPasswordC3: EditText
    lateinit var btnSubmit2: Button
    lateinit var sharedPreference: SharedPreferences
    var etMobile3:String? ="9998886666"
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_forgot_password)

        etOtp = findViewById(R.id.etOtp)
        etPassword3 = findViewById(R.id.etPassword3)
        etPasswordC3 = findViewById(R.id.etPasswordC3)
        btnSubmit2 = findViewById(R.id.btnSubmit2)
        sharedPreference =
            getSharedPreferences(getString(R.string.preference_file_name), Context.MODE_PRIVATE)

        if(intent!=null) {
            etMobile3 = intent.getStringExtra("user_mobile")
        }
       // etMobile3.text= sharedPreference.getString("mobile_number",null)

        btnSubmit2.setOnClickListener() {
           // txtMobile.text= sharedPreference.getString("user_mobile_number",null)

            val queue = Volley.newRequestQueue(this@ForgotPasswordActivity)
            val url = "http://13.235.250.119/v2/reset_password/fetch_result/"
            val jsonParams = JSONObject()

            jsonParams.put("otp", etOtp.text.toString())
           jsonParams.put("mobile_number", etMobile3.toString())
            jsonParams.put("password", etPassword3.text.toString())

            val jsonObjectRequest =
                object : JsonObjectRequest(Request.Method.POST, url, jsonParams, Response.Listener {

                    try {
                        val data = it.getJSONObject("data")
                        val success = data.getBoolean("success")
                        if (success) {

                            val response = data.getJSONObject("data")
                            sharedPreference.edit()
                                .putString("user_password", response.getString("user_password")).apply()

                            Toast.makeText(
                                this@ForgotPasswordActivity,
                                " Success Found $it",
                                Toast.LENGTH_LONG
                            ).show()
                            startActivity(
                                Intent(
                                    this@ForgotPasswordActivity,
                                    LoginActivity2::class.java
                                )
                            )
                            finish()



                        } else {

                            Toast.makeText(
                                this@ForgotPasswordActivity,
                                "wrong credentials ",
                                Toast.LENGTH_LONG
                            )
                                .show()
                        }
                    } catch (e: JSONException) {
                        Toast.makeText(
                            this@ForgotPasswordActivity,
                            "No Internet Connection $it",
                            Toast.LENGTH_LONG
                        ).show()


                    }

                }, Response.ErrorListener {
                    Toast.makeText(
                        this@ForgotPasswordActivity,
                        "No Internet Connectn $it",
                        Toast.LENGTH_LONG
                    )
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
}


