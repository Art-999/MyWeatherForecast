package com.example.arturmusayelyan.myweatherforecast.activites;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import com.example.arturmusayelyan.myweatherforecast.R;
import com.example.arturmusayelyan.myweatherforecast.models.City;
import com.google.gson.Gson;

public class WeatherActivity extends AppCompatActivity {
    private TextView cityTv, temperatureTv;
    private String cityName = "";
    private City city;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weather);
        init();
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            String gsonObject = extras.getString(MainActivity.KEY_CITY);
            city = receivedGsonParseToJson(gsonObject);
            cityName = city.getName();
        }
        cityTv.setText(cityName);
    }

    private void init() {
        cityTv = findViewById(R.id.city_tv);
        temperatureTv = findViewById(R.id.temp_tv);
    }

    private City receivedGsonParseToJson(String gsonObject) {
        Gson gson = new Gson();
        City city = gson.fromJson(gsonObject, City.class);
        return city;
    }
}
