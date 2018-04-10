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

    /**
     List{id=616052, name='Yerevan', coord=Coord{lon=44.5126, lat=40.1776},
     main=Main{temp=21.0, pressure=1017.0, humidity=14.0, tempMin=21.0, tempMax=21.0},
     dt=1523359800, wind=Wind{speed=0.5, deg=null}, sys=Sys{type=null, id=null, message=null, country='AM', sunrise=null, sunset=null},
     rain=null, snow=null,
     clouds=Clouds{all=8}, weather=[Weather{id=800, main='Clear', description='sky is clear', icon='02d'}]}
     */
}
