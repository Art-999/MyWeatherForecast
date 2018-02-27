package com.example.arturmusayelyan.myweatherforecast.dataController;

import android.content.Context;
import android.content.SharedPreferences;

import java.util.ArrayList;

/**
 * Created by artur.musayelyan on 27/02/2018.
 */

public class AllCitiesController {
    private final String SHARED_PREFS_FILE = "sharedPreferencesKey2";
    private static AllCitiesController instance = null;
    private static ArrayList<String> allCitiesIdList;

    private AllCitiesController() {

    }

    public static AllCitiesController getInstance() {
        if (instance == null) {
            instance = new AllCitiesController();
        }
        return instance;
    }

    public static ArrayList<String> getAllCitiesIdList() {
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
            allCitiesIdList.add(preferences.getString("Status_" + i, null));
        }
    }

    public void saveAllCitiesListToSharedPref(Context context) {
        SharedPreferences preferences = context.getSharedPreferences(SHARED_PREFS_FILE, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putInt("Status_size", allCitiesIdList.size());

        for (int i = 0; i < allCitiesIdList.size(); i++) {
            editor.remove("Status_" + i);
            editor.putString("Status_" + i, allCitiesIdList.get(i));
        }
        editor.commit();
    }

    public void addID(String ID) {
        if (allCitiesIdList == null) {
            allCitiesIdList = new ArrayList<>();
        }
        allCitiesIdList.add(ID);

    }

    public void removeID(String ID) {
        if (allCitiesIdList != null) {
            allCitiesIdList.remove(ID);
        }
    }
}
