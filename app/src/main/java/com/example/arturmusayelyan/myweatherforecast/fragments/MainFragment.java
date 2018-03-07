package com.example.arturmusayelyan.myweatherforecast.fragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.arturmusayelyan.myweatherforecast.R;
import com.example.arturmusayelyan.myweatherforecast.activites.MainActivity;
import com.example.arturmusayelyan.myweatherforecast.adapters.RecyclerCityAdapter;
import com.example.arturmusayelyan.myweatherforecast.dataController.AllCitiesController;
import com.example.arturmusayelyan.myweatherforecast.dataController.FavoritesController;
import com.example.arturmusayelyan.myweatherforecast.dataController.ShPrefController;
import com.example.arturmusayelyan.myweatherforecast.interfaces.FragmentsComunicateListener;
import com.example.arturmusayelyan.myweatherforecast.interfaces.RecyclerItemClickListener;
import com.example.arturmusayelyan.myweatherforecast.models.Example;
import com.example.arturmusayelyan.myweatherforecast.models.SeparateCity;
import com.example.arturmusayelyan.myweatherforecast.models.WeatherList;
import com.example.arturmusayelyan.myweatherforecast.networking.ApiClient;
import com.example.arturmusayelyan.myweatherforecast.networking.ApiInterface;
import com.example.arturmusayelyan.myweatherforecast.networking.NetworkController;
import com.example.arturmusayelyan.myweatherforecast.networking.WebServiceManager;
import com.example.arturmusayelyan.myweatherforecast.views.Loader;

import net.yslibrary.android.keyboardvisibilityevent.util.UIUtil;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by artur.musayelyan on 20/02/2018.
 */

public class MainFragment extends Fragment implements View.OnClickListener, RecyclerItemClickListener, FragmentsComunicateListener {

    private RecyclerView recyclerView;
    private RecyclerCityAdapter adapter;
    private RecyclerView.LayoutManager layoutManager;
    private ApiInterface apiInterface;
    private SwipeRefreshLayout swipeRefreshLayout;
    private SearchView searchView;
    private TextView toolbarTitle;
    private ImageView toolbarImage;
    private Loader loader;
    private ImageView slaqButton;
    private int k;

    public MainFragment() {

    }

    public static MainFragment newInstance() {
        Bundle args = new Bundle();
        MainFragment fragment = new MainFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.main_fragment, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        init(view);
        //  doGroupCityCall(true, AllCitiesController.getInstance().createGroupCitiesQUERY(getActivity()));
        if (NetworkController.isNetworkAvailable(getActivity())) {
            do20CitiesGroupCall(true);
        } else {
            initRecCityAdapter(ShPrefController.getAllObjects(getActivity()));
            Toast.makeText(getActivity(), R.string.check_connection, Toast.LENGTH_SHORT).show();
        }
    }


    private void do20CitiesGroupCall(final boolean isAdapterFirstInit) {
        loader.start();
        WebServiceManager.do20CitiesGroupCall().enqueue(new Callback<Example>() {
            @Override
            public void onResponse(Call<Example> call, Response<Example> response) {
                ShPrefController.removeAll(getActivity());
                List<WeatherList> dataList = response.body().getList();
                for (int i = 0; i < dataList.size(); i++) {
                    ShPrefController.addObject(getActivity(), dataList.get(i));
                }
                Log.d("Art", ShPrefController.getAllObjects(getActivity()).size() + "");
                if (isAdapterFirstInit) {
                    initRecCityAdapter(ShPrefController.getAllObjects(getActivity()));
                    loader.end();
                } else {
                    adapter.notifyItemRangeChanged(0, ShPrefController.getAllObjects(getActivity()).size());
                    loader.end();
                }

                if (swipeRefreshLayout != null && swipeRefreshLayout.isRefreshing()) {
                    swipeRefreshLayout.setRefreshing(false);
                }

            }

            @Override
            public void onFailure(Call<Example> call, Throwable t) {
                loader.end();
                initRecCityAdapter(ShPrefController.getAllObjects(getActivity()));
                Toast.makeText(getActivity(), R.string.check_connection, Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void init(View view) {
        recyclerView = view.findViewById(R.id.recycler_view);
        loader = view.findViewById(R.id.custom_loader);
        slaqButton = view.findViewById(R.id.slaq_button);
        slaqButton.setOnClickListener(this);
        toolbarTitle = view.findViewById(R.id.toolbar_title);
        toolbarImage = view.findViewById(R.id.toolbar_image_view);

        searchView = view.findViewById(R.id.search_view);
        EditText searchEditText = (EditText) searchView.findViewById(android.support.v7.appcompat.R.id.search_src_text);
        searchEditText.setTextColor(getResources().getColor(R.color.white));
        searchEditText.setHint(getResources().getString(R.string.search_hint));
        searchEditText.setHintTextColor(getResources().getColor(R.color.yellow));
        //   searchEditText.setBackgroundColor(getResources().getColor(R.color.light_blue));
        searchView.setOnSearchClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toolbarTitle.setVisibility(View.GONE);
                toolbarImage.setVisibility(View.GONE);
            }
        });

//        searchView.animate().x().y().setDuration().alpha().scaleX().start();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                UIUtil.hideKeyboard(getActivity());
                doSeparateCityCall(query);
                searchView.onActionViewCollapsed();
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });
        searchView.setOnCloseListener(new SearchView.OnCloseListener() {
            @Override
            public boolean onClose() {
                UIUtil.hideKeyboard(getActivity());
                recyclerView.smoothScrollToPosition(0);

                toolbarTitle.setVisibility(View.VISIBLE);
                toolbarImage.setVisibility(View.VISIBLE);
                return false;
            }
        });


        swipeRefreshLayout = view.findViewById(R.id.swipe_refresh_layout);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                do20CitiesGroupCall(false);
            }
        });
//        layoutManager = new LinearLayoutManager(getActivity());
//        recyclerView.setLayoutManager(layoutManager);
//        recyclerView.setHasFixedSize(true);
//        int colorResourceName = getResources().getIdentifier("blue", "color", getActivity().getPackageName());
//        Toasty.Config.getInstance().setTextColor(ContextCompat.getColor(getActivity(), colorResourceName)).apply();

        apiInterface = ApiClient.getApiClient().create(ApiInterface.class);
    }


    public void initRecCityAdapter(List<WeatherList> dataList) {
        Log.d("Art", dataList.size() + "");
        adapter = new RecyclerCityAdapter(getActivity(), dataList);
        adapter.setRecyclerItemClickListener(this);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
    }

    private void doGroupCityCall(final boolean fistTime, String query) {
        loader.start();
        Call<Example> call = apiInterface.getGroupCitesWeatherListByQuery(query);
        call.enqueue(new Callback<Example>() {
            @Override
            public void onResponse(Call<Example> call, Response<Example> response) {
                //  Log.d("Artur", response.body().toString());

                List<WeatherList> dataList = response.body().getList();
                if (dataList != null && !dataList.isEmpty()) {
                    if (fistTime) {

                        AllCitiesController.getInstance().clearShPrferences(getActivity());
                        for (int i = 0; i < dataList.size(); i++) {
                            AllCitiesController.getInstance().addObjectToPreferences(getActivity(), dataList.get(i), null, true);
                        }
                        initRecCityAdapter(AllCitiesController.getInstance().getWeatherListFromPrefernces(getActivity()));
                        // initRecCityAdapter(dataList);
                        Log.d("ShPreferences", AllCitiesController.getInstance().getWeatherListFromPrefernces(getActivity()).size() + "");
                    } else {
                        adapter.notifyItemRangeChanged(0, adapter.getDataList().size());
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
                initRecCityAdapter(AllCitiesController.getInstance().getWeatherListFromPrefernces(getContext()));
                if (swipeRefreshLayout != null && swipeRefreshLayout.isRefreshing()) {
                    swipeRefreshLayout.setRefreshing(false);
                }
                loader.end();
            }
        });
    }

    private void doCityWeatherCall(final String cityName){
        if(NetworkController.isNetworkAvailable(getActivity())) {
            loader.start();
            WebServiceManager.doCityWeatherCallByName(cityName).enqueue(new Callback<SeparateCity>() {
                @Override
                public void onResponse(Call<SeparateCity> call, Response<SeparateCity> response) {
                    WebServiceManager.doCityWeatherCallByName(cityName).enqueue(new Callback<SeparateCity>() {
                        @Override
                        public void onResponse(Call<SeparateCity> call, Response<SeparateCity> response) {

                        }

                        @Override
                        public void onFailure(Call<SeparateCity> call, Throwable t) {
                            Toast.makeText(getActivity(), R.string.check_connection, Toast.LENGTH_SHORT).show();
                            toolbarImage.setVisibility(View.VISIBLE);
                            toolbarTitle.setVisibility(View.VISIBLE);
                            loader.end();
                        }
                    });
                }

                @Override
                public void onFailure(Call<SeparateCity> call, Throwable t) {
                    Toast.makeText(getActivity(), R.string.type_correct, Toast.LENGTH_SHORT).show();
                    toolbarImage.setVisibility(View.VISIBLE);
                    toolbarTitle.setVisibility(View.VISIBLE);
                    loader.end();
                }
            });
        }else {
            Toast.makeText(getActivity(), R.string.type_correct, Toast.LENGTH_SHORT).show();
        }
    }

    private void doSeparateCityCall(final String cityName) {
        loader.start();
        Call<SeparateCity> call = apiInterface.getCityWeather(cityName);
        call.enqueue(new Callback<SeparateCity>() {
            @Override
            public void onResponse(Call<SeparateCity> call, Response<SeparateCity> response) {
                SeparateCity separateCity = response.body();
                if (separateCity != null && (separateCity.getList().size() > 0)) {

                    Call<Example> call1 = apiInterface.getGroupCitesWeatherListByQuery(String.valueOf(separateCity.getList().get(0).getId()));
                    call1.enqueue(new Callback<Example>() {
                        @Override
                        public void onResponse(Call<Example> call, Response<Example> response) {
                            List<WeatherList> dataList = response.body().getList();
                            WeatherList cityWeather = dataList.get(0);


                            recyclerView.smoothScrollToPosition(adapter.getItemCount() - 1);
                            toolbarImage.setVisibility(View.VISIBLE);
                            toolbarTitle.setVisibility(View.VISIBLE);
                            loader.end();
                        }

                        @Override
                        public void onFailure(Call<Example> call, Throwable t) {

                        }
                    });

                } else {
                    Toast.makeText(getActivity(), R.string.type_correct, Toast.LENGTH_SHORT).show();
                    toolbarImage.setVisibility(View.VISIBLE);
                    toolbarTitle.setVisibility(View.VISIBLE);
                    loader.end();
                }
            }


            @Override
            public void onFailure(Call<SeparateCity> call, Throwable t) {
//                Log.d("AAAA", t.toString());
                //Toast.makeText(getActivity(), t.toString(), Toast.LENGTH_SHORT).show();
                Toast.makeText(getActivity(), R.string.check_connection, Toast.LENGTH_SHORT).show();
                loader.end();
            }
        });
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.slaq_button:
                //doGroupCitiesCallByCustomNames();
                // Log.d("Art", FavoritesController.getInstance().favoriteCitesIdListInfo());
                if (FavoritesController.getInstance() != null) {
                    if (FavoritesController.getInstance().getFavoriteCitesIdList().size() > 0) {
                        FavoritesFragment favoritesFragment = FavoritesFragment.newInstance();
                        favoritesFragment.setFragmentsComunicateListener(this);
                        ((MainActivity) getActivity()).pushFragment(favoritesFragment, true);
                        return;
                    }
                }

                Toast.makeText(getActivity(), R.string.empty_favorites, Toast.LENGTH_SHORT).show();
                break;
        }
    }


    @Override
    public void onItemClick(View view, WeatherList weatherList, int position) {
        switch (view.getId()) {
            case R.id.custom_check_box:
                if (weatherList.isFavorite()) {
                    k++;
                    Log.d("Art", k + "");

                    FavoritesController.getInstance().addID(String.valueOf(weatherList.getId()));
                    Log.d("Art", FavoritesController.getInstance().favoriteCitesIdListInfo());
                } else {
                    k--;
                    Log.d("Art", k + "");

                    FavoritesController.getInstance().removeID(String.valueOf(weatherList.getId()));
                    Log.d("Art", FavoritesController.getInstance().favoriteCitesIdListInfo());
                }

                break;
            default:
                ((MainActivity) getActivity()).pushFragment(CityFragment.newInstance(weatherList.getName()), true);
                break;
        }

    }

    @Override
    public void onFragmentClick(View view, int position) {
        // adapter.notifyItemRangeChanged(position, dataList.size());
    }
}
