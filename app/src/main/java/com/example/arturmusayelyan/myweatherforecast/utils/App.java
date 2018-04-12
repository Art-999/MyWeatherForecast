package com.example.arturmusayelyan.myweatherforecast.utils;

import android.app.Application;
import android.content.Context;

public class App extends Application {
    private static Context myAppContext;

    public static Context getMyAppContext(){
        return myAppContext;
    }
}
