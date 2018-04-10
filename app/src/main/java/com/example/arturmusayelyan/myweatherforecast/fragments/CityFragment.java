package com.example.arturmusayelyan.myweatherforecast.fragments;

import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.arturmusayelyan.myweatherforecast.R;
import com.example.arturmusayelyan.myweatherforecast.activites.MainActivity;
import com.example.arturmusayelyan.myweatherforecast.dataController.ShPrefController;
import com.example.arturmusayelyan.myweatherforecast.models.List;
import com.example.arturmusayelyan.myweatherforecast.models.SeparateCity;
import com.example.arturmusayelyan.myweatherforecast.networking.WebServiceManager;
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
    private final static String CITY_NAME_KEY = "cityName";
    private final String CLEAR_SKY = "Clear";
    private final String CLOUDS = "Clouds";
    private final String RAIN = "Rain";
    private final String THUNDERSTORM = "Thunderstorm";
    private final String SNOW = "Snow";
    private final String MIST = "Mist";
    private final String HAZE = "Haze";
    private final String Drizzle = "Drizzle";

    private TextView dateTimeTV, cityNameTv, weatherDescTv, temperatureTv, windSpeedTv, humidityTv, tempMaxTv, tempMinTv;
    private ImageView slaqButton, cityMainIcon, tittleIcon;
    private String cityName;
    private String cityId;
    private CheckBox checkBox;
    private Loader loader;
    private SwipeRefreshLayout swipeRefreshLayout;
    public static String currentCityName;

    public CityFragment() {

    }

    public static CityFragment newInstance(String cityName) {
        Bundle bundle = new Bundle();
        bundle.putString(CITY_NAME_KEY, cityName);

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


        currentCityName = cityName;
        cityNameTv.setText(cityName);
        doSeparateCityCallBySearchedQuery(cityName);
    }

    private void init(View view) {
        cityName = getArguments().getString(CITY_NAME_KEY);

        dateTimeTV = view.findViewById(R.id.date_tame_tv);
        cityNameTv = view.findViewById(R.id.city_name);
        tittleIcon = view.findViewById(R.id.toolbar_image_view);
        temperatureTv = view.findViewById(R.id.temperature_tv);
        weatherDescTv = view.findViewById(R.id.weather_description_tv);
        humidityTv = view.findViewById(R.id.humidity_tv);
        tempMaxTv = view.findViewById(R.id.temp_max_tv);
        tempMinTv = view.findViewById(R.id.temp_min_tv);
        windSpeedTv = view.findViewById(R.id.wind_speed_tv);
        loader = view.findViewById(R.id.custom_loader);
        cityMainIcon = view.findViewById(R.id.city_main_icon);
        slaqButton = view.findViewById(R.id.slaq_button);
        checkBox = view.findViewById(R.id.custom_check_box);
        swipeRefreshLayout = view.findViewById(R.id.swipe_refresh_layout);

        slaqButton.setOnClickListener(this);

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                doSeparateCityCallBySearchedQuery(cityName);
            }
        });




        checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (buttonView.isPressed()) {
                    if (isChecked) {
                        // ShPrefController.addFavorites(getActivity(),cityName);
                        ShPrefController.addFavoritesById(getActivity(), cityId,cityName);
                    } else {
                        //ShPrefController.removeFavorite(getActivity(), cityName);
                        ShPrefController.removeFavoritesById(getActivity(), cityId,cityName);
                    }
                }
            }
        });
    }

    private void doSeparateCityCallBySearchedQuery(final String query) {
        loader.start();
        MainFragment mainFragment = (MainFragment) getActivity().getSupportFragmentManager().findFragmentByTag(MainActivity.MAIN_FRAGMENT_TAG);
        mainFragment.getLoader().end();
        WebServiceManager.doCityWeatherCallByName(query).enqueue(new Callback<SeparateCity>() {
            @Override
            public void onResponse(Call<SeparateCity> call, Response<SeparateCity> response) {
                SeparateCity separateCity = response.body();
                List currentCity = separateCity.getList().get(0);
                Log.d("DataWatching",currentCity.toString());
                Log.d("DataWatching",currentCity.getWeather().get(0).getMain());
                boolean isRainy=(currentCity.getWeather().get(0).getMain().equals("Rain"));
                Log.d("DataWatching",isRainy+"");

                Double temp = currentCity.getMain().getTemp();
                int tempInt = Integer.valueOf(temp.intValue());
                temperatureTv.setText(tempInt + "°C");
                String weatherDescription = currentCity.getWeather().get(0).getDescription();
                String weatherMainDescription = currentCity.getWeather().get(0).getMain();
                Log.d("Description", weatherDescription);
                Log.d("mainDescription", weatherMainDescription);


                windSpeedTv.setText(currentCity.getWind().getSpeed() + "mph");
                humidityTv.setText(currentCity.getMain().getHumidity() + "%");
                tempMaxTv.setText("max: " + currentCity.getMain().getTempMax() + "°C");
                tempMinTv.setText("min: " + currentCity.getMain().getTempMin() + "°C");
                String weatherIcon = currentCity.getWeather().get(0).getIcon();
                downloadImage(weatherIcon, tittleIcon);
                cityMainIcon.setImageResource(iconByDescription(weatherMainDescription, weatherIcon));
                if (swipeRefreshLayout != null && swipeRefreshLayout.isRefreshing()) {
                    swipeRefreshLayout.setRefreshing(false);
                }


                cityId = String.valueOf(currentCity.getId());
                java.util.List<String> favoritesList = ShPrefController.getAllFavoriteCitiesIdList(getActivity());
                if (favoritesList.contains(cityId)) {
                    checkBox.setChecked(true);
                } else {
                    checkBox.setChecked(false);
                }

                loader.end();
            }

            @Override
            public void onFailure(Call<SeparateCity> call, Throwable t) {
                if (swipeRefreshLayout != null && swipeRefreshLayout.isRefreshing()) {
                    swipeRefreshLayout.setRefreshing(false);
                }
                //Toast.makeText(getActivity(), getActivity().getResources().getString(R.string.check_connection), Toast.LENGTH_SHORT).show();
                loader.end();

                Snackbar snackbar = Snackbar.make(getActivity().findViewById(R.id.toolbar_image_view), getActivity().getResources().getString(R.string.check_connection), Snackbar.LENGTH_SHORT);
                snackbar.setAction("RETRY", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        doSeparateCityCallBySearchedQuery(query);
                    }
                });
                snackbar.setActionTextColor(Color.RED);
                View snackBarView = snackbar.getView();
                TextView snackBarTextView = snackBarView.findViewById(android.support.design.R.id.snackbar_text);
                snackBarTextView.setTextColor(Color.YELLOW);
                snackbar.show();
            }
        });
    }

    private void downloadImage(String icon, ImageView weatherIcon) {
        Glide.with(getActivity()).load("http://openweathermap.org/img/w/" + icon + ".png").into(weatherIcon);
    }

    private int iconByDescription(String description, String weatherIcon) {
        switch (description) {
            case CLEAR_SKY:
                if (weatherIcon.endsWith("d")) {
                    return R.drawable.clear_sky_day;
                }
                return R.drawable.clear_night;
            case CLOUDS:
                return R.drawable.clouds;
            case RAIN:
                return R.drawable.rain;
            case THUNDERSTORM:
                return R.drawable.thunderstorm;
            case HAZE:
                return R.drawable.haze;
            case SNOW:
                return R.drawable.snow;
            case Drizzle:
                if (weatherIcon.endsWith("d")) {
                    return R.drawable.drizzle_day;
                }
                return R.drawable.drizzle_night;
            case MIST:
                return R.drawable.mist;

        }
        return R.drawable.sun_separate_city_icon;
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.slaq_button:
                // (getActivity()).onBackPressed();
                ((MainActivity) getActivity()).backToHomeScreen();
                break;
        }
    }
}
