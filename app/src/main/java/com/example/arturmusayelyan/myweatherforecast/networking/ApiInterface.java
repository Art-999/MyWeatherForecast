package com.example.arturmusayelyan.myweatherforecast.networking;

import com.example.arturmusayelyan.myweatherforecast.models.Example;

import retrofit2.Call;
import retrofit2.http.GET;

/**
 * Created by artur.musayelyan on 13/02/2018.
 */

public interface ApiInterface {

    @GET("group?id=616052,617026,616530,616629,823816&units=metric&APPID=06c0cd0ecefd895a9600f56949423d8e")
    Call<Example> getCitysWeatherList();
}
