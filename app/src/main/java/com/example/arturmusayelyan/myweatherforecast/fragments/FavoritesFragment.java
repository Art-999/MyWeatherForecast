package com.example.arturmusayelyan.myweatherforecast.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
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
import com.example.arturmusayelyan.myweatherforecast.activites.MainActivity;
import com.example.arturmusayelyan.myweatherforecast.adapters.FavoriteCitiesAdapter;
import com.example.arturmusayelyan.myweatherforecast.dataController.FavoritesController;
import com.example.arturmusayelyan.myweatherforecast.models.Example;
import com.example.arturmusayelyan.myweatherforecast.models.List;
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
    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;
    private Loader loader;
    private Context context;
    private ApiInterface apiInterface;
    private FavoriteCitiesAdapter adapter;
    private java.util.List<WeatherList> dataList;

    private SwipeRefreshLayout swipeRefreshLayout;

    public FavoritesFragment() {

    }

    public static FavoritesFragment newInstance() {
        Bundle bundle = new Bundle();

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


        doGroupCitiesCallByCustomNames(FavoritesController.getInstance().nameToCitesIdQUERY(), true);
    }

    private void init(View view) {
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
                doGroupCitiesCallByCustomNames(FavoritesController.getInstance().nameToCitesIdQUERY(), false);
            }
        });
    }


    private void doGroupCitiesCallByCustomNames(String query, final boolean firstTime) {
        loader.start();

        Call<Example> call = apiInterface.getFavoriteCitesWeatherList(query);
        call.enqueue(new Callback<Example>() {
            @Override
            public void onResponse(Call<Example> call, Response<Example> response) {
                Log.d("Art", response.body().getList().toString());

                dataList = response.body().getList();
                if (dataList != null && !dataList.isEmpty()) {
                    if (firstTime) {
                        initFavoriteCitesAdapter((ArrayList<WeatherList>) dataList);
                    } else {
                        adapter.notifyItemRangeChanged(0, dataList.size());
                    }
                }

                if (swipeRefreshLayout != null && swipeRefreshLayout.isRefreshing()) {
                    swipeRefreshLayout.setRefreshing(false);
                }
                loader.end();
            }

            @Override
            public void onFailure(Call<Example> call, Throwable t) {
                Toast.makeText(getActivity(), R.string.check_connection, Toast.LENGTH_LONG).show();

                if (swipeRefreshLayout != null && swipeRefreshLayout.isRefreshing()) {
                    swipeRefreshLayout.setRefreshing(false);
                }
                loader.end();

            }
        });
    }

    private void initFavoriteCitesAdapter(ArrayList<WeatherList> favoriteCitiesList) {
        adapter = new FavoriteCitiesAdapter(getActivity(), favoriteCitiesList);
        adapter.setRecyclerItemClickListener(this);
        recyclerView.setAdapter(adapter);
    }


    @Override
    public void onItemClick(View view, WeatherList weatherList, int position) {
        switch (view.getId()) {
            case R.id.custom_check_box:
                FavoritesController.getInstance().removeID(String.valueOf(weatherList.getId()));
                Log.d("Art", FavoritesController.getInstance().favoriteSitesIdListInfo());
                adapter.notifyItemRemoved(position);
                adapter.notifyItemRangeChanged(position, FavoritesController.getInstance().getFavoriteCitesIdList().size());
                break;
            default:
                ((MainActivity) getActivity()).pushFragment(CityFragment.newInstance(weatherList.getName()), true);
                break;
        }
    }
}
