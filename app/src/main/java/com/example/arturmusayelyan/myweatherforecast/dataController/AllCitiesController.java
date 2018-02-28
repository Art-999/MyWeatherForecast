package com.example.arturmusayelyan.myweatherforecast.dataController;

import android.content.Context;
import android.content.SharedPreferences;

import com.example.arturmusayelyan.myweatherforecast.models.WeatherList;
import com.google.gson.Gson;

import java.util.ArrayList;

/**
 * Created by artur.musayelyan on 27/02/2018.
 */

public class AllCitiesController {
    private final String SHARED_PREFS_FILE = "sharedPreferencesKey2";
    private static AllCitiesController instance = null;
    private ArrayList<WeatherList> allCitiesIdList;

    private AllCitiesController() {

    }

    public static AllCitiesController getInstance() {
        if (instance == null) {
            instance = new AllCitiesController();
        }
        return instance;
    }

    public ArrayList<WeatherList> getAllCitiesIdList() {
        return allCitiesIdList;
    }

    public void getAllCitiesListFromSharedPref(Context context) {
        if (allCitiesIdList == null) {
            allCitiesIdList = new ArrayList<>();
        }

        SharedPreferences preferences = context.getSharedPreferences(SHARED_PREFS_FILE, Context.MODE_PRIVATE);
        allCitiesIdList.clear();
        int size = preferences.getInt("Status_size", 0);
        for (int i = 0; i < size; i++) {
            Gson gson = new Gson();
            allCitiesIdList.add(gson.fromJson(preferences.getString("Status_" + i, null), WeatherList.class));
        }
    }

    public void saveAllCitiesListToSharedPref(Context context) {
        SharedPreferences preferences = context.getSharedPreferences(SHARED_PREFS_FILE, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putInt("Status_size", allCitiesIdList.size());
        for (int i = 0; i < allCitiesIdList.size(); i++) {
            editor.remove("Status_" + i);
            Gson gson = new Gson();
            editor.putString("Status_" + i, gson.toJson(allCitiesIdList.get(i)));
        }
        editor.commit();
    }

    public void addWeatherListObject(WeatherList weatherList) {
        if (allCitiesIdList == null) {
            allCitiesIdList = new ArrayList<>();
        }
        allCitiesIdList.add(weatherList);

    }

    public void removeWeatherListObject(WeatherList weatherList) {
        if (allCitiesIdList != null) {
            allCitiesIdList.remove(weatherList);
        }
    }
    public void deleteAllCitiesList(){
       if(allCitiesIdList!=null){
           allCitiesIdList.clear();
       }
    }
}
