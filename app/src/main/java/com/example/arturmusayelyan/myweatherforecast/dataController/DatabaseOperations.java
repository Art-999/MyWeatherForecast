package com.example.arturmusayelyan.myweatherforecast.dataController;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by artur.musayelyan on 05/03/2018.
 */

public class DatabaseOperations extends SQLiteOpenHelper {
    public static final int database_version=1;
    public DatabaseOperations(Context context) {
        super(context, "WeatherListDataBase", null, database_version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
      //  db.execSQL();
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
