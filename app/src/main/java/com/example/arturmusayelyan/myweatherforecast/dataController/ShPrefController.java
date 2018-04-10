package com.example.arturmusayelyan.myweatherforecast.dataController;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

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
    private static final String FAVORITE_lIST_ID_KEY = "Status_favorite_list_ID_key";
    private static final String FAVORITE_LIST_NAME_KEY = "Status_favorite_list_Name_key";
    private static final String REPORT_MESSAGE_KEY = "Report_message_key";
    private static final String SPINNER_ITEM_ID_KEY = "Spinner_item_id_key";
    private static final String SPINNER_ITEM_NAME_KEY = "Spinner_item_name_key";

    public static void addReportMessage(Context context, String message) {
        SharedPreferences preferences = context.getSharedPreferences(SHARED_PREFERENCES_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        cleanReportMessage(context);
        editor.putString(REPORT_MESSAGE_KEY, message);
        editor.apply();
    }

    public static String getReportMessage(Context context) {
        SharedPreferences preferences = context.getSharedPreferences(SHARED_PREFERENCES_NAME, Context.MODE_PRIVATE);
        return preferences.getString(REPORT_MESSAGE_KEY, null);
    }

    public static void cleanReportMessage(Context context) {
        SharedPreferences preferences = context.getSharedPreferences(SHARED_PREFERENCES_NAME, Context.MODE_PRIVATE);
        preferences.edit().remove(REPORT_MESSAGE_KEY).apply();
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

    public static ArrayList<String> getAllFavoriteCities(Context context) {
        SharedPreferences preferences = context.getSharedPreferences(SHARED_PREFERENCES_NAME, Context.MODE_PRIVATE);
        if (preferences.contains(FAVORITE_lIST_ID_KEY)) {
            Gson gson = new Gson();
            String json = preferences.getString(FAVORITE_lIST_ID_KEY, null);
            Type type = new TypeToken<ArrayList<String>>() {
            }.getType();
            return gson.fromJson(json, type);
        }
        return new ArrayList<>();
    }

    public static ArrayList<String> getAllFavoriteCitiesIdList(Context context) {
        SharedPreferences preferences = context.getSharedPreferences(SHARED_PREFERENCES_NAME, Context.MODE_PRIVATE);
        if (preferences.contains(FAVORITE_lIST_ID_KEY)) {
            Gson gson = new Gson();
            String json = preferences.getString(FAVORITE_lIST_ID_KEY, null);
            Type type = new TypeToken<ArrayList<String>>() {
            }.getType();
            return gson.fromJson(json, type);
        }
        return new ArrayList<>();
    }

    public static ArrayList<String> getAllFavoriteCitiesNameList(Context context) {
        SharedPreferences preferences = context.getSharedPreferences(SHARED_PREFERENCES_NAME, Context.MODE_PRIVATE);
        if (preferences.contains(FAVORITE_lIST_ID_KEY)) {
            Gson gson = new Gson();
            String json = preferences.getString(FAVORITE_LIST_NAME_KEY, null);
            Type type = new TypeToken<ArrayList<String>>() {
            }.getType();
            return gson.fromJson(json, type);
        }
        return new ArrayList<>();
    }

    public static ArrayList<WeatherList> getFavoriteCitiesMainList(Context context) {
        SharedPreferences preferences = context.getSharedPreferences(SHARED_PREFERENCES_NAME, Context.MODE_PRIVATE);
        if (preferences.contains(FAVORITE_lIST_ID_KEY)) {
            String json = preferences.getString(FAVORITE_lIST_ID_KEY, null);
            Type type = new TypeToken<ArrayList<WeatherList>>() {
            }.getType();
            return new Gson().fromJson(json, type);
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

    public static void addFavorites(Context context, String cityName) {
        SharedPreferences preferences = context.getSharedPreferences(SHARED_PREFERENCES_NAME, Context.MODE_PRIVATE);
        ArrayList<String> favoriteCitiesList;
        if (preferences.contains(FAVORITE_lIST_ID_KEY)) {
            favoriteCitiesList = getAllFavoriteCities(context);
        } else {
            favoriteCitiesList = new ArrayList<>();
        }
        favoriteCitiesList.add(cityName);
        SharedPreferences.Editor editor = preferences.edit();
        Gson gson = new Gson();
        String list = gson.toJson(favoriteCitiesList);
        editor.putString(FAVORITE_lIST_ID_KEY, list);
        editor.apply();
    }

    public static void addFavoritesById(Context context, String cityId, String cityName) {
        SharedPreferences preferences = context.getSharedPreferences(SHARED_PREFERENCES_NAME, Context.MODE_PRIVATE);
        ArrayList<String> favoriteCitiesIdList;
        ArrayList<String> favoriteCitiesNameList;
        if (preferences.contains(FAVORITE_lIST_ID_KEY) && preferences.contains(FAVORITE_LIST_NAME_KEY)) {
            favoriteCitiesIdList = getAllFavoriteCitiesIdList(context);
            favoriteCitiesNameList = getAllFavoriteCitiesNameList(context);
        } else {
            favoriteCitiesIdList = new ArrayList<>();
            favoriteCitiesNameList = new ArrayList<>();
        }
        favoriteCitiesIdList.add(cityId);
        favoriteCitiesNameList.add(cityName);
        SharedPreferences.Editor editor = preferences.edit();
        //Gson gson = new Gson();
        String listId = new Gson().toJson(favoriteCitiesIdList);
        String listName = new Gson().toJson(favoriteCitiesNameList);
        editor.putString(FAVORITE_lIST_ID_KEY, listId);
        editor.putString(FAVORITE_LIST_NAME_KEY, listName);
        editor.apply();
    }

    public static void addFavoritesByFullObject(Context context, WeatherList weatherList) {
        SharedPreferences preferences = context.getSharedPreferences(SHARED_PREFERENCES_NAME, Context.MODE_PRIVATE);
        //String weatherListConvertedTostringObj = new Gson().toJson(weatherList);
        ArrayList<WeatherList> favoriteCitesMainList;
        if (preferences.contains(FAVORITE_lIST_ID_KEY)) {
            favoriteCitesMainList = getFavoriteCitiesMainList(context);
        } else {
            favoriteCitesMainList = new ArrayList<>();
        }
        favoriteCitesMainList.add(weatherList);
        SharedPreferences.Editor editor = preferences.edit();
        String list = new Gson().toJson(favoriteCitesMainList);
        editor.putString(FAVORITE_lIST_ID_KEY, list);
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

    public static void removeFavorite(Context context, String cityName) {
        SharedPreferences preferences = context.getSharedPreferences(SHARED_PREFERENCES_NAME, Context.MODE_PRIVATE);
        ArrayList<String> favoriteCitiesList = getAllFavoriteCities(context);
        if (favoriteCitiesList.contains(cityName)) {
            favoriteCitiesList.remove(cityName);
            SharedPreferences.Editor editor = preferences.edit();
            Gson gson = new Gson();
            String list = gson.toJson(favoriteCitiesList);
            editor.putString(FAVORITE_lIST_ID_KEY, list);
            editor.apply();
        }
    }

    public static void removeFavoritesById(Context context, String cityId, String cityName) {
        SharedPreferences preferences = context.getSharedPreferences(SHARED_PREFERENCES_NAME, Context.MODE_PRIVATE);
        ArrayList<String> favoriteCitiesIdList = getAllFavoriteCitiesIdList(context);
        ArrayList<String> favoriteCitiesNameList = getAllFavoriteCitiesNameList(context);
        if (favoriteCitiesIdList.contains(cityId)) {
            favoriteCitiesIdList.remove(cityId);
            favoriteCitiesNameList.remove(cityName);
            SharedPreferences.Editor editor = preferences.edit();
            //Gson gson = new Gson();
            String listId = new Gson().toJson(favoriteCitiesIdList);
            String listName = new Gson().toJson(favoriteCitiesNameList);
            editor.putString(FAVORITE_lIST_ID_KEY, listId);
            editor.putString(FAVORITE_LIST_NAME_KEY, listName);
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

    public static void removeAllFavorites(Context context) {
        SharedPreferences preferences = context.getSharedPreferences(SHARED_PREFERENCES_NAME, Context.MODE_PRIVATE);
        ArrayList<String> allFavoriteCities = getAllFavoriteCities(context);
        allFavoriteCities.clear();
        SharedPreferences.Editor editor = preferences.edit();
        Gson gson = new Gson();
        String list = gson.toJson(allFavoriteCities);
        editor.putString(FAVORITE_lIST_ID_KEY, list);
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
        ArrayList<String> favoriteCitiesList = getAllFavoriteCities(context);
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < citiesGroupWeatherList.size(); i++) {
            if (favoriteCitiesList.contains(citiesGroupWeatherList.get(i).getName())) {
                builder.append(String.valueOf(citiesGroupWeatherList.get(i).getId()));
                builder.append(",");
//                if (i != (citiesGroupWeatherList.size() - 1)) {
//                    builder.append(",");
//                }
            }
        }
        String result = new String(builder).substring(0, builder.length() - 1);
        return result;

    }

    public static String createQueryByFavorites(Context context) {
        ArrayList<String> favoriteCitiesList = getAllFavoriteCitiesIdList(context);
        Log.d("QueryResult", favoriteCitiesList.toString());
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < favoriteCitiesList.size(); i++) {
            builder.append(String.valueOf(favoriteCitiesList.get(i)));
            builder.append(",");
        }
        String result = new String(builder).substring(0, builder.length() - 1);
        return result;
    }

    public static void addSpinnerSelectedItemId(Context context, int id) {
        SharedPreferences preferences = context.getSharedPreferences(SHARED_PREFERENCES_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(SPINNER_ITEM_ID_KEY, Integer.toString(id));
        editor.apply();
    }


    public static String getSpinnerSelectedItemId(Context context) {
        SharedPreferences preferences = context.getSharedPreferences(SHARED_PREFERENCES_NAME, Context.MODE_PRIVATE);
        if (preferences.contains(SPINNER_ITEM_ID_KEY)) {
            return preferences.getString(SPINNER_ITEM_ID_KEY, null);
        }
        return null;
    }

    public static void addSpinnerSelectedItemName(Context context, String name) {
        SharedPreferences preferences = context.getSharedPreferences(SHARED_PREFERENCES_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(SPINNER_ITEM_NAME_KEY, name);
        editor.apply();
    }

    public static String getSpinnerSelectedItemName(Context context) {
        SharedPreferences preferences = context.getSharedPreferences(SHARED_PREFERENCES_NAME, Context.MODE_PRIVATE);
        if (preferences.contains(SPINNER_ITEM_NAME_KEY)) {
            return preferences.getString(SPINNER_ITEM_NAME_KEY, null);
        }
        return null;
    }

}
