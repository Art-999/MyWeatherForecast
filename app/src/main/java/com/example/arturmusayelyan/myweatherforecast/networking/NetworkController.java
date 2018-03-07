package com.example.arturmusayelyan.myweatherforecast.networking;

import android.content.Context;
import android.net.ConnectivityManager;

/**
 * Created by artur.musayelyan on 07/03/2018.
 */

public class NetworkController {
    public static boolean isNetworkAvailable(Context context) {
        final ConnectivityManager connectivityManager = ((ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE));
        return connectivityManager.getActiveNetworkInfo() != null && connectivityManager.getActiveNetworkInfo().isConnected();
    }
}
