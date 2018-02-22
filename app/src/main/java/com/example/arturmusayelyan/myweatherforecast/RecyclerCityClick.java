package com.example.arturmusayelyan.myweatherforecast;

import com.example.arturmusayelyan.myweatherforecast.models.WeatherList;

/**
 * Created by artur.musayelyan on 13/02/2018.
 */

public interface RecyclerCityClick {
    void cityClick(WeatherList weatherList,boolean checkBoxClicked);
}
