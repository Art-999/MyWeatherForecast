package com.example.arturmusayelyan.myweatherforecast.dataController;

import java.util.ArrayList;

/**
 * Created by artur.musayelyan on 26/02/2018.
 */

public class FavoritesController {
    private static FavoritesController favoritesController=new FavoritesController();
    private ArrayList<String> favoriteCitesIdList;
    private ArrayList<String> favoriteCitesNameList;

    private FavoritesController() {

    }

    public static FavoritesController getInstance() {
        return favoritesController;
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

    public String favoriteSitesIdListInfo() {
        if (favoriteCitesIdList != null) {
            return favoriteCitesIdList.toString();
        }
        return null;
    }

    public String nameToCitesIdQUERY(){
        if(favoriteCitesIdList!=null){
            StringBuilder builder=null;
            for (int i = 0; i <favoriteCitesIdList.size() ; i++) {
                builder=builder.append(favoriteCitesIdList);
                if(i!=(favoriteCitesIdList.size()-1)){
                    builder=builder.append(",");
                }
            }
            return new String(builder);
        }
        return null;
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
