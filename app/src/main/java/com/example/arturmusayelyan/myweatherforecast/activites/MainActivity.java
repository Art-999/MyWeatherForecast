package com.example.arturmusayelyan.myweatherforecast.activites;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.Toast;

import com.example.arturmusayelyan.myweatherforecast.R;
import com.example.arturmusayelyan.myweatherforecast.RecyclerCityClick;
import com.example.arturmusayelyan.myweatherforecast.adapters.RecyclerCityAdapter;
import com.example.arturmusayelyan.myweatherforecast.networking.ApiClient;
import com.example.arturmusayelyan.myweatherforecast.networking.ApiInterface;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements RecyclerCityClick {
    private RecyclerView recyclerView;
    private RecyclerCityAdapter adapter;
    private RecyclerView.LayoutManager layoutManager;
    private ApiInterface apiInterface;

    private List<String> citysList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        init();
        createCitysList();
        initRecCityAdapter();
    }

    private void init() {
        recyclerView = findViewById(R.id.recycler_view);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);

        apiInterface = ApiClient.getApiClient().create(ApiInterface.class);

    }

    private void createCitysList() {
        citysList = new ArrayList<>();
        citysList.add("Yerevan");
        citysList.add("Abovyan");
        citysList.add("Vanadzor");
        citysList.add("Hrazdan");
        citysList.add("Masis");
        citysList.add("Kapan");
        citysList.add("Goris");
        citysList.add("Gavar");
        citysList.add("Gyumri");
        citysList.add("Meghri");
        citysList.add("Aparan");
        citysList.add("Tashir");
        citysList.add("Alaverdi");
        citysList.add("Yeghegnadzor");
        citysList.add("Dilijan");
        citysList.add("Echmiadzin");
    }
    private void initRecCityAdapter(){
        adapter = new RecyclerCityAdapter(citysList);
        adapter.setCityClickListener(this);
        recyclerView.setAdapter(adapter);
    }

    @Override
    public void cityClick(String cityName) {
        Toast.makeText(this, cityName, Toast.LENGTH_SHORT).show();
    }
}
