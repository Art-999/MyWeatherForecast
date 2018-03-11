package com.example.arturmusayelyan.myweatherforecast.interfaces;

import android.view.View;

import com.example.arturmusayelyan.myweatherforecast.models.WeatherList;

/**
 * Created by artur.musayelyan on 27/02/2018.
 */

public interface FragmentsCommunicatorListener {
    void onFragmentsCommunicateClick(View view, WeatherList weatherList);
}
