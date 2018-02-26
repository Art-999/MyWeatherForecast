package com.example.arturmusayelyan.myweatherforecast.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.arturmusayelyan.myweatherforecast.R;
import com.example.arturmusayelyan.myweatherforecast.RecyclerItemClickListener;
import com.example.arturmusayelyan.myweatherforecast.adapters.FavoriteCitiesAdapter;
import com.example.arturmusayelyan.myweatherforecast.dataController.FavoritesController;
import com.example.arturmusayelyan.myweatherforecast.models.Example;
import com.example.arturmusayelyan.myweatherforecast.models.WeatherList;
import com.example.arturmusayelyan.myweatherforecast.networking.ApiClient;
import com.example.arturmusayelyan.myweatherforecast.networking.ApiInterface;
import com.example.arturmusayelyan.myweatherforecast.views.Loader;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by artur.musayelyan on 23/02/2018.
 */

public class FavoritesFragment extends Fragment implements RecyclerItemClickListener {
    private final static String CITY_NAMES_KEY = "cityNamesKey";
    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;
    private Loader loader;
    private Context context;
    private ApiInterface apiInterface;
    private FavoriteCitiesAdapter adapter;
    private ArrayList<WeatherList> favoriteCitiesList;
    private ArrayList<String> favorityCitiesNames;
    private SwipeRefreshLayout swipeRefreshLayout;

    public FavoritesFragment() {

    }

    public static FavoritesFragment newInstance() {
        Bundle bundle = new Bundle();
        // bundle.putStringArrayList(CITY_NAMES_KEY, cityNames);
        // Log.d("Art", cityNames.toString());
        FavoritesFragment fragment = new FavoritesFragment();
        fragment.setArguments(bundle);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_favorites, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        context = getActivity();
        init(view);

//        for (int i = 0; i <favorityCitiesNames.size() ; i++) {
//            doSeparateCityCall(favorityCitiesNames.get(i));
//        }

        doGroupCitiesCallByCustomNames(FavoritesController.getInstance().nameToCitesIdQUERY());
    }

    private void init(View view) {
        favoriteCitiesList = new ArrayList<>();
        favorityCitiesNames = getArguments().getStringArrayList(CITY_NAMES_KEY);
        recyclerView = view.findViewById(R.id.favorites_recycler);
        loader = view.findViewById(R.id.custom_loader);
        layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);

        apiInterface = ApiClient.getApiClient().create(ApiInterface.class);

        swipeRefreshLayout = view.findViewById(R.id.swipe_refresh_layout);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                // doGroupCityCall();
                loader.start();
                adapter.notifyItemRangeChanged(0, favoriteCitiesList.size());
                if (swipeRefreshLayout != null && swipeRefreshLayout.isRefreshing()) {
                    swipeRefreshLayout.setRefreshing(false);
                    loader.end();
                }
            }
        });
    }

    private void doGroupCityCall() {
        loader.start();
        Call<Example> call = apiInterface.getCitysWeatherList();
        call.enqueue(new Callback<Example>() {
            @Override
            public void onResponse(Call<Example> call, Response<Example> response) {
                //  Log.d("Artur", response.body().toString());

                favoriteCitiesList = (ArrayList<WeatherList>) response.body().getList();
                if (favoriteCitiesList != null && !favoriteCitiesList.isEmpty()) {

                    initFavoriteCitesAdapter(favoriteCitiesList);
                }

                if (swipeRefreshLayout != null && swipeRefreshLayout.isRefreshing()) {
                    swipeRefreshLayout.setRefreshing(false);
                }
                loader.end();
            }

            @Override
            public void onFailure(Call<Example> call, Throwable t) {
                Toast.makeText(getActivity(), "Check your Internet Connection", Toast.LENGTH_LONG).show();

                if (swipeRefreshLayout != null && swipeRefreshLayout.isRefreshing()) {
                    swipeRefreshLayout.setRefreshing(false);
                }
                loader.end();
            }
        });
    }

    private void doGroupCitiesCallByCustomNames(String query) {
        loader.start();

        Call<Example> call = apiInterface.getFavoriteCitesWeatherList(query);
        call.enqueue(new Callback<Example>() {
            @Override
            public void onResponse(Call<Example> call, Response<Example> response) {
                Log.d("Art", response.body().getList().toString());
                initFavoriteCitesAdapter((ArrayList<WeatherList>) response.body().getList());
                loader.end();
            }

            @Override
            public void onFailure(Call<Example> call, Throwable t) {

            }
        });
    }

    private void initFavoriteCitesAdapter(ArrayList<WeatherList> favoriteCitiesList) {
        adapter = new FavoriteCitiesAdapter(getActivity(), favoriteCitiesList);
        adapter.setRecyclerItemClickListener(this);
        recyclerView.setAdapter(adapter);
    }


//    private void doSeparateCityCall(final String cityName) {
//        loader.start();
//        //       final String[] tempature = {null};
//        Call<SeparateCity> call = apiInterface.getFavoriteCityWeather(cityName);
//        call.enqueue(new Callback<SeparateCity>() {
//            @Override
//            public void onResponse(Call<SeparateCity> call, Response<SeparateCity> response) {
//                //Log.d("Weather",response.body()+"");
//                SeparateCity separateCity=response.body();
//                if (separateCity != null) {
//                    Log.d("Art", separateCity.toString());
//                    favoriteCitiesList.add();
//                    //adapter.notifyItemRangeChanged(0,favorityCitiesNames.size());
//                    if(favorityCitiesNames.get(favorityCitiesNames.size()-1).equalsIgnoreCase(cityName)){
//                        adapter = new FavoriteCitiesAdapter(getActivity(), favoriteCitiesList);
//                        // adapter.setRecyclerItemClickListener(context);
//                        recyclerView.setAdapter(adapter);
//                        loader.end();
//                    }
//                    return;
//                }
//
//              //  loader.end();
//            }
//
//
//            @Override
//            public void onFailure(Call<SeparateCity> call, Throwable t) {
////                Log.d("AAAA", t.toString());
//                //Toast.makeText(getActivity(), t.toString(), Toast.LENGTH_SHORT).show();
//                Toast.makeText(getActivity(), R.string.check_connection, Toast.LENGTH_SHORT).show();
//                loader.end();
//            }
//        });
//    }

    @Override
    public void onItemClick(View view, WeatherList weatherList, int position) {

    }
}
