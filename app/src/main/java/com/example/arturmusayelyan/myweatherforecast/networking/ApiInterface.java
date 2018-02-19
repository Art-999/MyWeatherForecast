package com.example.arturmusayelyan.myweatherforecast.networking;

import com.example.arturmusayelyan.myweatherforecast.models.Example;
import com.example.arturmusayelyan.myweatherforecast.models.SeparateCity;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by artur.musayelyan on 13/02/2018.
 */

public interface ApiInterface {

    @GET("group?id=616052,617026,616530,174895,616635,174823,616953,616752,866096,174710,616178,2643123,5106292,1704129,4517009,4974617,4976934,6691831,2023469,4791160&units=metric&APPID=06c0cd0ecefd895a9600f56949423d8e")
    Call<Example> getCitysWeatherList();

    @GET("find?&units=metric&APPID=06c0cd0ecefd895a9600f56949423d8e")
    Call<SeparateCity> getCityWeather(@Query("q") String cityName);
}
