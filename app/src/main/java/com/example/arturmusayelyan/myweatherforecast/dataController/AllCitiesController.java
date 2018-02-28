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
    private ArrayList<WeatherList> allCitiesList;

    private AllCitiesController() {

    }

    public static AllCitiesController getInstance() {
        if (instance == null) {
            instance = new AllCitiesController();
        }
        return instance;
    }

    public ArrayList<WeatherList> getAllCitiesList() {
        return allCitiesList;
    }

    public void getAllCitiesListFromSharedPref(Context context) {
        if (allCitiesList == null) {
            allCitiesList = new ArrayList<>();
        }

        SharedPreferences preferences = context.getSharedPreferences(SHARED_PREFS_FILE, Context.MODE_PRIVATE);
        allCitiesList.clear();
        int size = preferences.getInt("Status_size", 0);
        for (int i = 0; i < size; i++) {
            Gson gson = new Gson();
            allCitiesList.add(gson.fromJson(preferences.getString("Status_" + i, null), WeatherList.class));
        }
    }

    public void saveAllCitiesListToSharedPref(Context context) {
        SharedPreferences preferences = context.getSharedPreferences(SHARED_PREFS_FILE, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putInt("Status_size", allCitiesList.size());
        for (int i = 0; i < allCitiesList.size(); i++) {
            editor.remove("Status_" + i);
            Gson gson = new Gson();
            editor.putString("Status_" + i, gson.toJson(allCitiesList.get(i)));
        }
        editor.commit();
    }

    public void addWeatherListObject(WeatherList weatherList) {
        if (allCitiesList == null) {
            allCitiesList = new ArrayList<>();
        }
        allCitiesList.add(weatherList);

    }

    public void removeWeatherListObject(WeatherList weatherList) {
        if (allCitiesList != null) {
            allCitiesList.remove(weatherList);
        }
    }
    public void deleteAllCitiesList(){
       if(allCitiesList !=null){
           allCitiesList.clear();
       }
    }
    public String createGroupCitiesQUERY() {
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < allCitiesList.size(); i++) {
            builder.append(String.valueOf(allCitiesList.get(i).getId()));
            if (i != (allCitiesList.size() - 1)) {
                builder.append(",");
            }
        }
        return new String(builder);
    }
}
