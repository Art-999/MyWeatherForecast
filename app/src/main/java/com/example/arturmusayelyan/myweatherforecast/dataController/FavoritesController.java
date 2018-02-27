package com.example.arturmusayelyan.myweatherforecast.dataController;

import android.content.Context;
import android.content.SharedPreferences;

import java.util.ArrayList;

/**
 * Created by artur.musayelyan on 26/02/2018.
 */

public class FavoritesController {
    private final String SHARED_PREFS_FILE = "sharedPreferencesKey1";
    private static FavoritesController instance = null;
    private static ArrayList<String> favoriteCitesIdList;
    private ArrayList<String> favoriteCitesNameList;

    private FavoritesController() {

    }

    public void getFavoriteListFromSharedPref(Context context) {
        if (favoriteCitesIdList == null) {
            favoriteCitesIdList = new ArrayList<>();
        }

        SharedPreferences preferences = context.getSharedPreferences(SHARED_PREFS_FILE, Context.MODE_PRIVATE);

        favoriteCitesIdList.clear();
        int size = preferences.getInt("Status_size", 0);
        for (int i = 0; i < size; i++) {
            favoriteCitesIdList.add(preferences.getString("Status_" + i, null));
        }
    }

    public void saveFavoriteListToSharedPref(Context context) {
        SharedPreferences preferences = context.getSharedPreferences(SHARED_PREFS_FILE, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putInt("Status_size", favoriteCitesIdList.size());

        for (int i = 0; i < favoriteCitesIdList.size(); i++) {
            editor.remove("Status_" + i);
            editor.putString("Status_" + i, favoriteCitesIdList.get(i));
        }
        editor.commit();
    }

    public static void setInstance(FavoritesController instance) {
        FavoritesController.instance = instance;
    }

    public static FavoritesController getInstance() {
        if (instance == null) {
            instance = new FavoritesController();
            // favoriteCitesIdList=new ArrayList<>();
        }
        return instance;
    }

    public void addID(String ID) {
        if (favoriteCitesIdList == null) {
            favoriteCitesIdList = new ArrayList<>();
        }
        favoriteCitesIdList.add(ID);

    }

    public void removeID(String ID) {
        if (favoriteCitesIdList != null) {
            favoriteCitesIdList.remove(ID);
        }
    }

    public void removeIDsIndex(int position) {
        if (favoriteCitesIdList != null) {
            favoriteCitesIdList.remove(position);
        }
    }

    public String favoriteCitesIdListInfo() {
        if (favoriteCitesIdList != null) {
            return favoriteCitesIdList.toString();
        }
        return null;
    }

    public ArrayList<String> getFavoriteCitesIdList() {
        return favoriteCitesIdList;
    }

    public String nameToCitesIdQUERY() {
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < favoriteCitesIdList.size(); i++) {
            builder.append(favoriteCitesIdList.get(i));
            if (i != (favoriteCitesIdList.size() - 1)) {
                builder.append(",");
            }
        }
        return new String(builder);
    }

    private void addName(String name) {
        if (favoriteCitesNameList == null) {
            favoriteCitesNameList = new ArrayList<>();
        }
        favoriteCitesNameList.add(name);
    }

    private void removeName(String name) {
        if (favoriteCitesNameList != null) {
            favoriteCitesNameList.remove(name);
        }
    }

    public String favoriteSitesNameListInfo() {
        if (favoriteCitesNameList != null) {
            return favoriteCitesNameList.toString();
        }
        return null;
    }
}
