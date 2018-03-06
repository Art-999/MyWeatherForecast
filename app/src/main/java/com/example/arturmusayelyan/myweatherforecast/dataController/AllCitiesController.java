package com.example.arturmusayelyan.myweatherforecast.dataController;

import android.content.Context;
import android.content.SharedPreferences;
import android.widget.Toast;

import com.example.arturmusayelyan.myweatherforecast.adapters.RecyclerCityAdapter;
import com.example.arturmusayelyan.myweatherforecast.models.WeatherList;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;

/**
 * Created by artur.musayelyan on 27/02/2018.
 */

public class AllCitiesController {
    private final String SHARED_PREFS_FILE = "sharedPreferencesKey2";
    private final String SHARED_PREFERENCES_NAME = "sharedPreferencesName23";
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

    public ArrayList<WeatherList> getWeatherListFromPrefernces(Context context) {
//        SharedPreferences preferences = context.getSharedPreferences(SHARED_PREFERENCES_NAME, Context.MODE_PRIVATE);
//        ArrayList<WeatherList> weatherListArrayList = new ArrayList<>();
//        int listSize = preferences.getInt("Status_size", 0);
//        for (int i = 0; i < listSize; i++) {
//            Gson gson = new Gson();
//            weatherListArrayList.add(gson.fromJson(preferences.getString("Status_" + i, null), WeatherList.class));
//        }
//        return weatherListArrayList;

        SharedPreferences preferences = context.getSharedPreferences(SHARED_PREFERENCES_NAME, Context.MODE_PRIVATE);
        Gson gson1 = new Gson();
        String json = preferences.getString("Status_list", null);
        Type type = new TypeToken<ArrayList<WeatherList>>() {
        }.getType();
        ArrayList<WeatherList> weatherListArrayList = gson1.fromJson(json, type);
        if (weatherListArrayList != null) {
            return weatherListArrayList;
        }
        return new ArrayList<>();
    }

    public void addObjectToPreferences(Context context, WeatherList weatherList, RecyclerCityAdapter adapter, boolean firstInit) {
        SharedPreferences preferences = context.getSharedPreferences(SHARED_PREFERENCES_NAME, Context.MODE_PRIVATE);
        Gson gson1 = new Gson();
        String json = preferences.getString("Status_list", null);
        Type type = new TypeToken<ArrayList<WeatherList>>() {
        }.getType();
        ArrayList<WeatherList> weatherListArrayList = gson1.fromJson(json, type);


        // ArrayList<WeatherList> weatherListArrayList = new ArrayList<>();
        // ArrayList<WeatherList> weatherListArrayList=new Gson().fromJson(preferences.getString("Status_list",null),new TypeToken<List<WeatherList>>(){}.getType());
        //  int listSize = preferences.getInt("Status_size", 0);
//        for (int i = 0; i < weatherListArrayList.size(); i++) {
//           //  Gson gson = new Gson();
//           //  weatherListArrayList.add(gson.fromJson(preferences.getString("Status_" + i, null), WeatherList.class));
//        }
        if (weatherListArrayList == null) {
            weatherListArrayList = new ArrayList<>();
        }

        if (firstInit) {
            weatherListArrayList.add(weatherList);

        } else {
            for (int i = 0; i < weatherListArrayList.size(); i++) {
                if (weatherListArrayList.get(i).getName().equalsIgnoreCase(weatherList.getName())) {
                    Toast.makeText(context, "This city already exist in list", Toast.LENGTH_SHORT).show();
                    return;
                }
            }
            if (adapter != null) {
                weatherListArrayList.add(weatherList);
                adapter.getDataList().add(weatherList);
                adapter.notifyDataSetChanged();
                //adapter.notifyItemRangeChanged(0,);
            }
        }
        SharedPreferences.Editor editor = preferences.edit();
        Gson gson = new Gson();
        String list = gson.toJson(weatherListArrayList);

        editor.putString("Status_list", list);
        editor.apply();

    }

    public void removeObjectFromPreferences(Context context, int position) {

        ArrayList<WeatherList> weatherListArrayList = getWeatherListFromPrefernces(context);
        if (weatherListArrayList.size() > 0) {

            weatherListArrayList.remove(position);


            SharedPreferences preferences = context.getSharedPreferences(SHARED_PREFERENCES_NAME, Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = preferences.edit();
            Gson gson = new Gson();
            String list = gson.toJson(weatherListArrayList);

            editor.putString("Status_list", list);
            editor.apply();
        }

    }

    public void clearShPrferences(Context context) {
        SharedPreferences preferences = context.getSharedPreferences(SHARED_PREFERENCES_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.clear();
        editor.apply();
    }


    public void removeWeatherListObject(WeatherList weatherList) {
        if (allCitiesList != null) {
            allCitiesList.remove(weatherList);
        }
    }

    public void deleteAllCitiesList() {
        if (allCitiesList != null) {
            allCitiesList.clear();
        }
    }

    public String createGroupCitiesQUERY(Context context) {
        ArrayList<WeatherList> weatherListArrayList = getWeatherListFromPrefernces(context);

        if (weatherListArrayList.size() > 0) {
            StringBuilder builder = new StringBuilder();
            for (int i = 0; i < weatherListArrayList.size(); i++) {
                builder.append(String.valueOf(weatherListArrayList.get(i).getId()));
                if (i != (weatherListArrayList.size() - 1)) {
                    builder.append(",");
                }
            }
            return new String(builder);
        }
        return "616052,617026,616530,174895";
    }
}
