package com.example.arturmusayelyan.myweatherforecast;

import android.view.View;

import com.example.arturmusayelyan.myweatherforecast.models.WeatherList;

/**
 * Created by artur.musayelyan on 13/02/2018.
 */

public interface RecyclerItemClickListener {
    void onItemClick(View view, WeatherList weatherList, int position);
}
