package com.example.arturmusayelyan.myweatherforecast.dataController;

import android.content.Context;
import android.content.SharedPreferences;

import com.example.arturmusayelyan.myweatherforecast.models.WeatherList;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;

/**
 * Created by artur.musayelyan on 07/03/2018.
 */

public class ShPrefController {
    private static final String SHARED_PREFERENCES_NAME = "sharedPreferencesName01";
    private static final String LIST_KEY = "Status_list";

    private static ShPrefController instance = null;
//    private static ArrayList<WeatherList> citiesGroupWeatherList;
//
//    private ShPrefController() {
//            citiesGroupWeatherList = new ArrayList<>();
//    }
//
//    public static ShPrefController getInstance(Context context) {
//        if (instance == null) {
//            instance = new ShPrefController();
//            preferences = context.getSharedPreferences(SHARED_PREFERENCES_NAME, Context.MODE_PRIVATE);
//            SharedPreferences.Editor editor = preferences.edit();
//            Gson gson = new Gson();
//            String list = gson.toJson(citiesGroupWeatherList);
//            editor.putString(LIST_KEY, list);
//            editor.apply();
//        }
//        return instance;
//    }

    public static String getSharedPreferencesName() {
        return SHARED_PREFERENCES_NAME;
    }

    /**
     * return WeatherList type arrayList from preferences
     */
    public static ArrayList<WeatherList> getAllObjects(Context context) {
        SharedPreferences preferences = context.getSharedPreferences(SHARED_PREFERENCES_NAME, Context.MODE_PRIVATE);
        if (preferences.contains(LIST_KEY)) {
            Gson gson = new Gson();
            String json = preferences.getString(LIST_KEY, null);
            Type type = new TypeToken<ArrayList<WeatherList>>() {
            }.getType();

            return gson.fromJson(json, type);
        }
        return new ArrayList<>();
    }


    public static void addObject(Context context, WeatherList weatherList) {
        SharedPreferences preferences = context.getSharedPreferences(SHARED_PREFERENCES_NAME, Context.MODE_PRIVATE);
        ArrayList<WeatherList> citiesGroupWeatherList;
        if (preferences.contains(LIST_KEY)) {
            citiesGroupWeatherList = getAllObjects(context);
        } else {
            citiesGroupWeatherList = new ArrayList<>();
        }
        citiesGroupWeatherList.add(weatherList);
        SharedPreferences.Editor editor = preferences.edit();
        Gson gson = new Gson();
        String list = gson.toJson(citiesGroupWeatherList);
        editor.putString(LIST_KEY, list);
        editor.apply();
    }

    /**
     * return WeatherList type object from preferences
     */
    public static WeatherList getObject(Context context, int position) {
        ArrayList<WeatherList> citiesGroupWeatherList = getAllObjects(context);
        if (position < citiesGroupWeatherList.size()) {
            return citiesGroupWeatherList.get(position);
        }
        return null;
    }

    public static void removeObject(Context context, int position) {
        SharedPreferences preferences = context.getSharedPreferences(SHARED_PREFERENCES_NAME, Context.MODE_PRIVATE);
        ArrayList<WeatherList> citiesGroupWeatherList = getAllObjects(context);
        if (position < citiesGroupWeatherList.size()) {
            citiesGroupWeatherList.remove(position);
            SharedPreferences.Editor editor = preferences.edit();
            Gson gson = new Gson();
            String list = gson.toJson(citiesGroupWeatherList);
            editor.putString(LIST_KEY, list);
            editor.apply();
        }
    }

    public static void removeAll(Context context) {
        SharedPreferences preferences = context.getSharedPreferences(SHARED_PREFERENCES_NAME, Context.MODE_PRIVATE);
        ArrayList<WeatherList> citiesGroupWeatherList = getAllObjects(context);
        citiesGroupWeatherList.clear();
        SharedPreferences.Editor editor = preferences.edit();
        Gson gson = new Gson();
        String list = gson.toJson(citiesGroupWeatherList);
        editor.putString(LIST_KEY, list);
        editor.apply();
    }

    public static void editCityFavoriteType(Context context, int position, boolean isFavorite) {
        SharedPreferences preferences = context.getSharedPreferences(SHARED_PREFERENCES_NAME, Context.MODE_PRIVATE);
        ArrayList<WeatherList> citiesGroupWeatherList = getAllObjects(context);
        if (position < citiesGroupWeatherList.size()) {
            citiesGroupWeatherList.get(position).setFavorite(isFavorite);
            SharedPreferences.Editor editor = preferences.edit();
            Gson gson = new Gson();
            String list = gson.toJson(citiesGroupWeatherList);
            editor.putString(LIST_KEY, list);
            editor.apply();
        }
    }

    public static String createQueryForCall(Context context) {
        ArrayList<WeatherList> citiesGroupWeatherList = getAllObjects(context);
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < citiesGroupWeatherList.size(); i++) {
            builder.append(String.valueOf(citiesGroupWeatherList.get(i).getId()));
            if (i != (citiesGroupWeatherList.size() - 1)) {
                builder.append(",");
            }
        }
        return new String(builder);

    }


}
