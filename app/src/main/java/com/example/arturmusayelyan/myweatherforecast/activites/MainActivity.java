package com.example.arturmusayelyan.myweatherforecast.activites;

import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.arturmusayelyan.myweatherforecast.R;
import com.example.arturmusayelyan.myweatherforecast.RecyclerCityClick;
import com.example.arturmusayelyan.myweatherforecast.adapters.RecyclerCityAdapter;
import com.example.arturmusayelyan.myweatherforecast.models.City;
import com.example.arturmusayelyan.myweatherforecast.models.Example;
import com.example.arturmusayelyan.myweatherforecast.models.WeatherList;
import com.example.arturmusayelyan.myweatherforecast.networking.ApiClient;
import com.example.arturmusayelyan.myweatherforecast.networking.ApiInterface;

import java.util.ArrayList;
import java.util.List;

import es.dmoral.toasty.Toasty;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity implements RecyclerCityClick {
    public static String KEY_CITY = "cityKey";
    private RecyclerView recyclerView;
    private RecyclerCityAdapter adapter;
    private RecyclerView.LayoutManager layoutManager;
    private ProgressBar progressBar;
    private ApiInterface apiInterface;


    private List<City> citysList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        init();
        //createCitysList();


        doGroupCityCall();
    }

    private void init() {
        recyclerView = findViewById(R.id.recycler_view);
        progressBar = findViewById(R.id.progress_bar);
        progressBar.setVisibility(View.VISIBLE);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);
        int colorResourceName = getResources().getIdentifier("blue", "color", getApplicationContext().getPackageName());
        Toasty.Config.getInstance().setTextColor(ContextCompat.getColor(this, colorResourceName)).apply();

        apiInterface = ApiClient.getApiClient().create(ApiInterface.class);
    }

    private void createCitysList() {
        citysList = new ArrayList<>();
        citysList.add(new City("Yerevan", "616052"));
        citysList.add(new City("Abovyan", "617026"));
        citysList.add(new City("Vanadzor", "616530"));
        citysList.add(new City("Hrazdan", "616629"));
        citysList.add(new City("Masis", "823816"));
        citysList.add(new City("Kapan", "174875"));
        citysList.add(new City("Goris", "174895"));
        citysList.add(new City("Gavar", "616599"));
        citysList.add(new City("Gyumri", "616635"));
        citysList.add(new City("Meghri", "174823"));
        citysList.add(new City("Aparan", "616953"));
        citysList.add(new City("Tashir", "616178"));
        citysList.add(new City("Alaverdi", "616974"));
        citysList.add(new City("Yeghegnadzor", "174710"));
        citysList.add(new City("Dilijan", "616752"));
        citysList.add(new City("Echmiadzin", "866096"));
    }

    private void initRecCityAdapter(List<WeatherList> dataList) {
        adapter = new RecyclerCityAdapter(dataList);
        adapter.setCityClickListener(this);
        recyclerView.setAdapter(adapter);
    }

    @Override
    public void cityClick(WeatherList weatherList) {
        //     Gson gson = new Gson();

//        Toast toast = Toasty.normal(this, "   " + weatherList.getName() + "   ");
//        toast.setGravity(Gravity.CENTER, 0, 0);
//        toast.show();
//        Intent intent = new Intent(this, WeatherActivity.class);
//        intent.putExtra(KEY_CITY, gson.toJson(city));
//        startActivity(intent);
    }

    private void doGroupCityCall() {
        Call<Example> call = apiInterface.getCitysWeatherList();
        call.enqueue(new Callback<Example>() {
            @Override
            public void onResponse(Call<Example> call, Response<Example> response) {
                Log.d("Artur", response.body().toString());
//                Main mainData=response.body().getMain();
//
//                Double temp=mainData.getTemp();
//                int tempInt=Integer.valueOf(temp.intValue());
//                Log.d("Artur",tempInt+"");

                List<WeatherList> dataList = response.body().getList();
                if (dataList != null && !dataList.isEmpty()) {
                    initRecCityAdapter(dataList);
                }
                progressBar.setVisibility(View.GONE);
            }

            @Override
            public void onFailure(Call<Example> call, Throwable t) {
                Toast.makeText(MainActivity.this, "Check your Internet Connection", Toast.LENGTH_LONG).show();
                //finish();
                progressBar.setVisibility(View.GONE);
            }
        });
    }

}
