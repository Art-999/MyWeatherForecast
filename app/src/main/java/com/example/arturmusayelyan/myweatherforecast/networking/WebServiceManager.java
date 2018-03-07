package com.example.arturmusayelyan.myweatherforecast.networking;

import com.example.arturmusayelyan.myweatherforecast.models.Example;
import com.example.arturmusayelyan.myweatherforecast.models.SeparateCity;

import retrofit2.Call;

/**
 * Created by artur.musayelyan on 13/02/2018.
 */

public class WebServiceManager {
    private static ApiInterface apiInterface = ApiClient.getApiClient().create(ApiInterface.class);

    public static Call<Example> do20CitiesGroupCall() {
        Call<Example> call = apiInterface.get20CitiesWeatherList();
        return call;
    }

    public static Call<SeparateCity> doCityWeatherCallByName(String cityName) {
        Call<SeparateCity> call = apiInterface.getCityWeather(cityName);
        return call;
    }

    public static Call<Example> doCitiesGroupCallByIds(String citiesGroupIdQuery) {
        Call<Example> call = apiInterface.getGroupCitesWeatherListByQuery(citiesGroupIdQuery);
        return call;
    }
}
