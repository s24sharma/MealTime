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

class RegisterActivity : AppCompatActivity() {


    lateinit var etName: EditText
    lateinit var etEmail: EditText
    lateinit var etAddress: EditText
    lateinit var etMobile2: EditText
    lateinit var etPassword2: EditText
    lateinit var etPasswordC: EditText
    lateinit var btnSubmit: Button
    lateinit var sharedPreferences: SharedPreferences
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        sharedPreferences =
            getSharedPreferences(getString(R.string.preference_file_name), Context.MODE_PRIVATE)
        setContentView(R.layout.activity_register)



        etName = findViewById(R.id.etName)
        etEmail = findViewById(R.id.etEmail)
        etAddress = findViewById(R.id.etAddress)
        etMobile2 = findViewById(R.id.etMobile2)
        etPassword2 = findViewById(R.id.etPassword2)
        etPasswordC = findViewById(R.id.etPasswordC)
        btnSubmit = findViewById(R.id.btnSubmit)

        btnSubmit.setOnClickListener() {
            if (etPassword2.text.toString() == etPasswordC.text.toString() && etPassword2.length() > 3) {

                if (etMobile2.length() == 10) {


                    val queue = Volley.newRequestQueue(this@RegisterActivity)
                    val url = "http://13.235.250.119/v2/register/fetch_result/"
                    val jsonParams = JSONObject()
                    jsonParams.put("name", etName.text.toString())
                    jsonParams.put("mobile_number", etMobile2.text.toString())
                    jsonParams.put("email", etEmail.text.toString())
                    jsonParams.put("address", etAddress.text.toString())
                    jsonParams.put("password", etPassword2.text.toString())
                    jsonParams.put("confirm_password", etPasswordC.text.toString())


                    val jsonObjectRequest =
                        object :
                            JsonObjectRequest(
                                Request.Method.POST,
                                url,
                                jsonParams,
                                Response.Listener {

                                    try {
                                        val data = it.getJSONObject("data")
                                        val success = data.getBoolean("success")
                                        if (success) {

                                            val response = data.getJSONObject("data")
                                            sharedPreferences.edit()
                                                .putString("user_id", response.getString("user_id"))
                                                .apply()

                                            sharedPreferences.edit()
                                                .putString("user_name", response.getString("name"))
                                                .apply()

                                            sharedPreferences.edit()
                                                .putString(
                                                    "user_mobile_number",
                                                    response.getString("mobile_number")
                                                ).apply()


                                            sharedPreferences.edit()
                                                .putString(
                                                    "user_address",
                                                    response.getString("address")
                                                )
                                                .apply()

                                            sharedPreferences.edit()
                                                .putString(
                                                    "user_email",
                                                    response.getString("email")
                                                )
                                                .apply()


                                            startActivity(
                                                Intent(
                                                    this@RegisterActivity,
                                                    NavigationActivity::class.java
                                                )
                                            )
                                            Toast.makeText(
                                                this@RegisterActivity,
                                                "Successfully Registered!!",
                                                Toast.LENGTH_LONG
                                            )
                                                .show()


                                        } else {

                                            Toast.makeText(
                                                this@RegisterActivity,
                                                "Enter Valid Email or Mobile number",
                                                Toast.LENGTH_LONG
                                            )
                                                .show()
                                        }
                                    } catch (e: JSONException) {
                                        Toast.makeText(
                                            this@RegisterActivity,
                                            "error occurred $it",
                                            Toast.LENGTH_LONG
                                        )
                                            .show()


                                    }
                                },
                                Response.ErrorListener {
                                    Toast.makeText(
                                        this@RegisterActivity,
                                        "No Internet Connection",
                                        Toast.LENGTH_LONG
                                    ).show()
                                }
                            ) {
                            override fun getHeaders(): MutableMap<String, String> {         //code for sending the header content
                                val headers = HashMap<String, String>()
                                headers["Content-type"] = "application/json"
                                headers["token"] = "bf479684678793"
                                return headers
                            }
                        }
                    queue.add(jsonObjectRequest)
                }else {
                    Toast.makeText(
                        this@RegisterActivity,
                        "Enter valid Mobile number ",
                        Toast.LENGTH_LONG
                    ).show()
                }
                } else {
                    Toast.makeText(
                        this@RegisterActivity,
                        "Passwords do not match or too short ",
                        Toast.LENGTH_LONG
                    ).show()
                }
            }
            }

    }

