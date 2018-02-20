package com.example.arturmusayelyan.myweatherforecast.fragments;

import android.annotation.SuppressLint;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.Icon;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.example.arturmusayelyan.myweatherforecast.R;
import com.example.arturmusayelyan.myweatherforecast.activites.MainActivity;
import com.example.arturmusayelyan.myweatherforecast.models.SeparateCity;
import com.example.arturmusayelyan.myweatherforecast.networking.ApiClient;
import com.example.arturmusayelyan.myweatherforecast.networking.ApiInterface;
import com.example.arturmusayelyan.myweatherforecast.views.Loader;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by artur.musayelyan on 20/02/2018.
 */

public class CityFragment extends Fragment implements View.OnClickListener {
    private TextView dateTimeTV, cityNameTv, weatherDescTv, temperatureTv, windSpeedTv, humidityTv, tempMaxTv, tempMinTv;
    private ImageView slaqButton,cityMainIcon;
    private String cityName;
    private ApiInterface apiInterface;
    private Loader loader;
    private SwipeRefreshLayout swipeRefreshLayout;

    public static String CITY_NAME;

    public CityFragment() {

    }

    public static CityFragment newInstance(String cityName) {
        Bundle bundle = new Bundle();
        bundle.putString("cityName", cityName);
        CityFragment fragment = new CityFragment();
        fragment.setArguments(bundle);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.separate_city_fragment, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        init(view);

        final Handler someHandler = new Handler(Looper.getMainLooper());
        someHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                dateTimeTV.setText(new SimpleDateFormat("dd/MM/yyyy HH:mm:ss", Locale.US).format(new Date()));
                someHandler.postDelayed(this, 1000);
            }
        }, 10);

        cityName = getArguments().getString("cityName");

        CITY_NAME=cityName;

        cityNameTv.setText(cityName);

        doSeparateCityCall(cityName);
    }

    private void init(View view) {
        dateTimeTV = view.findViewById(R.id.date_tame_tv);
        cityNameTv = view.findViewById(R.id.city_name);
        temperatureTv = view.findViewById(R.id.temperature_tv);
        weatherDescTv = view.findViewById(R.id.weather_description_tv);
        humidityTv = view.findViewById(R.id.humidity_tv);
        tempMaxTv = view.findViewById(R.id.temp_max_tv);
        tempMinTv = view.findViewById(R.id.temp_min_tv);
        windSpeedTv = view.findViewById(R.id.wind_speed_tv);
        loader = view.findViewById(R.id.custom_loader);
        cityMainIcon=view.findViewById(R.id.city_main_icon);
        slaqButton = view.findViewById(R.id.slaq_button);
        slaqButton.setOnClickListener(this);

        apiInterface = ApiClient.getApiClient().create(ApiInterface.class);

        swipeRefreshLayout = view.findViewById(R.id.swipe_refresh_layout);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                doSeparateCityCall(cityName);
            }
        });
    }

    private void doSeparateCityCall(final String cityName) {
        loader.start();
        final String[] tempature = {null};
        Call<SeparateCity> call = apiInterface.getCityWeather(cityName);
        call.enqueue(new Callback<SeparateCity>() {
            @SuppressLint("CheckResult")
            @Override
            public void onResponse(Call<SeparateCity> call, Response<SeparateCity> response) {
                //Log.d("Weather",response.body()+"");
                SeparateCity separateCity = response.body();
                if (separateCity != null && (separateCity.getList().size() > 0)) {
                    Double temp = separateCity.getList().get(0).getMain().getTemp();
                    int tempInt = Integer.valueOf(temp.intValue());
                    tempature[0] = String.valueOf(tempInt);

                    cityNameTv.setText(cityName + ", " + separateCity.getList().get(0).getSys().getCountry());
                    weatherDescTv.setText(separateCity.getList().get(0).getWeather().get(0).getDescription());
                    temperatureTv.setText(tempature[0] + "°C");
                    windSpeedTv.setText(separateCity.getList().get(0).getWind().getSpeed() + "mph");
                    humidityTv.setText(separateCity.getList().get(0).getMain().getHumidity() + "%");
                    tempMaxTv.setText("max: " + separateCity.getList().get(0).getMain().getTempMax() + "°C");
                    tempMinTv.setText("min: " + separateCity.getList().get(0).getMain().getTempMin() + "°C");
                    Log.d("Art",separateCity.getList().get(0).getWeather().get(0).getIcon());

                    Glide.with(getActivity()).load("http://openweathermap.org/img/w/"+separateCity.getList().get(0).getWeather().get(0).getIcon()+".png").listener(new RequestListener<Drawable>() {
                        @Override
                        public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                            return false;
                        }

                        @Override
                        public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                            return false;
                        }
                    }).into(cityMainIcon);
                   // cityMainIcon.setImageDrawable(Glide.with(getActivity()).load(separateCity.getList().get(0).getWeather().get(0).getIcon()));

                }

                if (swipeRefreshLayout != null && swipeRefreshLayout.isRefreshing()) {
                    swipeRefreshLayout.setRefreshing(false);
                }

                if (tempature[0] != null) {
                    loader.end();
                    //Toast.makeText(getActivity(), tempature[0], Toast.LENGTH_SHORT).show();
                    return;
                }
                Toast.makeText(getActivity(), "Type city name in correct", Toast.LENGTH_SHORT).show();
                loader.end();
            }


            @Override
            public void onFailure(Call<SeparateCity> call, Throwable t) {
//                Log.d("AAAA", t.toString());

                if (swipeRefreshLayout != null && swipeRefreshLayout.isRefreshing()) {
                    swipeRefreshLayout.setRefreshing(false);
                }
                //Toast.makeText(getActivity(), t.toString(), Toast.LENGTH_SHORT).show();
                Toast.makeText(getActivity(), "Check your Internet Connection", Toast.LENGTH_SHORT).show();
                loader.end();
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.slaq_button:
                //Toast.makeText(getActivity(), "Worked", Toast.LENGTH_SHORT).show();
                ((MainActivity)getActivity()).addMainFragment();
                break;
        }
    }
}
