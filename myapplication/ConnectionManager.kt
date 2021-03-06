package com.sasha.myapplication

import android.content.Context
import android.content.Context.CONNECTIVITY_SERVICE
import android.net.ConnectivityManager
import android.net.NetworkInfo

class ConnectionManager {
    fun checkConnectivity(context: Context): Boolean {

        val connectivityManager =
            context.getSystemService(CONNECTIVITY_SERVICE) as ConnectivityManager
        val activeNetwork: NetworkInfo? = connectivityManager.activeNetworkInfo
        if (activeNetwork?.isConnected != null) {
            return activeNetwork.isConnected
        } else {
            return false
        }
    }
}