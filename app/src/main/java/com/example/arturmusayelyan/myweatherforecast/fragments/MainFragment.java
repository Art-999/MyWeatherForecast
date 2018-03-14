package com.example.arturmusayelyan.myweatherforecast.fragments;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
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
import com.example.arturmusayelyan.myweatherforecast.dataController.ShPrefController;
import com.example.arturmusayelyan.myweatherforecast.interfaces.FragmentsCommunicatorListener;
import com.example.arturmusayelyan.myweatherforecast.interfaces.MainFragmentItemClickListener;
import com.example.arturmusayelyan.myweatherforecast.models.Example;
import com.example.arturmusayelyan.myweatherforecast.models.SeparateCity;
import com.example.arturmusayelyan.myweatherforecast.models.WeatherList;
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

public class MainFragment extends Fragment implements View.OnClickListener, MainFragmentItemClickListener, FragmentsCommunicatorListener {

    private RecyclerView recyclerView;
    private RecyclerCityAdapter adapter;
    private SwipeRefreshLayout swipeRefreshLayout;
    private SearchView searchView;
    private TextView toolbarTitle;
    private ImageView toolbarImage;
    private Loader loader;
    private ImageView slaqButton;
    private FavoritesFragment favoritesFragment;


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
        if (NetworkController.isNetworkAvailable(getActivity())) {
            do20CitiesGroupCall(true);
        } else {
            initRecCityAdapter(syncFavorite(ShPrefController.getAllObjects(getActivity())));
            //Toast.makeText(getActivity(), R.string.check_connection, Toast.LENGTH_SHORT).show();
            Snackbar snackbar = Snackbar.make(view, getActivity().getResources().getString(R.string.check_connection), Snackbar.LENGTH_LONG);
            snackbar.setAction("RETRY", new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    do20CitiesGroupCall(true);
                }
            });
            snackbar.setActionTextColor(Color.RED);
            View snackBarView = snackbar.getView();
            TextView snackBarTextView = snackBarView.findViewById(android.support.design.R.id.snackbar_text);
            snackBarTextView.setTextColor(Color.YELLOW);
            snackbar.show();
        }
    }

    public void upDateData() {
        do20CitiesGroupCall(false);
    }

    private void do20CitiesGroupCall(final boolean isAdapterFirstInit) {
        loader.start();
        WebServiceManager.do20CitiesGroupCall().enqueue(new Callback<Example>() {
            @Override
            public void onResponse(Call<Example> call, Response<Example> response) {
                final List<WeatherList> dataList = response.body().getList();
                if (isAdapterFirstInit) {
                    List<WeatherList> dataListForAdapter = syncFavorite(dataList);
                    initRecCityAdapter(dataListForAdapter);
                    upDateShPrefData(dataList);
                } else {
                    List<WeatherList> dataListForAdapter = syncFavorite(dataList);
                    adapter.notifyItemRangeChanged(0, dataListForAdapter.size());
                }
                if (swipeRefreshLayout != null && swipeRefreshLayout.isRefreshing()) {
                    swipeRefreshLayout.setRefreshing(false);
                }
                loader.post(new Runnable() {
                    @Override
                    public void run() {
                        loader.end();
                    }
                });

            }

            @Override
            public void onFailure(Call<Example> call, Throwable t) {
                loader.end();
                initRecCityAdapter(ShPrefController.getAllObjects(getActivity()));
                //Toast.makeText(getActivity(), R.string.check_connection, Toast.LENGTH_SHORT).show();

                Snackbar snackbar = Snackbar.make(getActivity().findViewById(R.id.toolbar_image_view), getActivity().getResources().getString(R.string.check_connection), Snackbar.LENGTH_SHORT);
                snackbar.setAction("RETRY", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        do20CitiesGroupCall(false);
                    }
                });
                snackbar.setActionTextColor(Color.RED);
                View snackBarView = snackbar.getView();
                TextView snackBarTextView = snackBarView.findViewById(android.support.design.R.id.snackbar_text);
                snackBarTextView.setTextColor(Color.YELLOW);
                snackbar.show();

                if (swipeRefreshLayout != null && swipeRefreshLayout.isRefreshing()) {
                    swipeRefreshLayout.setRefreshing(false);
                }
            }
        });
    }

    private List<WeatherList> syncFavorite(List<WeatherList> dataList) {
        List<String> favoriteCitiesList = ShPrefController.getAllFavoriteCitiesIdList(getActivity());
        Log.d("Favorite", favoriteCitiesList.toString());
        for (int i = 0; i < dataList.size(); i++) {
            if (favoriteCitiesList.contains(String.valueOf(dataList.get(i).getId()))) {
                dataList.get(i).setFavorite(true);
                if (adapter != null) {
                    adapter.getDataList().get(i).setFavorite(true);
                }

            } else {
                dataList.get(i).setFavorite(false);
                if (adapter != null) {
                    adapter.getDataList().get(i).setFavorite(false);
                }

            }
        }

        return dataList;
    }


    private void upDateShPrefData(final List<WeatherList> dataList) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                ShPrefController.removeAll(getActivity());
                for (int i = 0; i < dataList.size(); i++) {
                    ShPrefController.addObject(getActivity(), dataList.get(i));
                }
            }
        }).start();
    }

    private void init(View view) {
        favoritesFragment = FavoritesFragment.newInstance();
        favoritesFragment.setFragmentsCommunicatorListener(this);
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
                // doSeparateCityCall(query);
                doCityWeatherCallBySearchedQuery(query);
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

    }


    public void initRecCityAdapter(List<WeatherList> dataList) {
        adapter = new RecyclerCityAdapter(getActivity(), dataList);
        adapter.setRecyclerItemClickListener(this);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
    }

    private void doCityWeatherCallBySearchedQuery(String query) {
        loader.start(); //kareliya es pahe mi qich popoxel
        WebServiceManager.doCityWeatherCallByName(query).enqueue(new Callback<SeparateCity>() {
            @Override
            public void onResponse(Call<SeparateCity> call, Response<SeparateCity> response) {
                SeparateCity separateCity = response.body();
                if (separateCity != null && ((separateCity.getList().size() > 0))) {
                    toolbarImage.setVisibility(View.VISIBLE);
                    toolbarTitle.setVisibility(View.VISIBLE);
                    ((MainActivity) getActivity()).pushFragment(CityFragment.newInstance(separateCity.getList().get(0).getName()), true, MainActivity.CITY_FRAGMENT_TAG);
                    loader.end();
                } else {
                    Toast.makeText(getActivity(), R.string.type_correct, Toast.LENGTH_SHORT).show();
                    toolbarImage.setVisibility(View.VISIBLE);
                    toolbarTitle.setVisibility(View.VISIBLE);
                    loader.end();
                }
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
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.slaq_button:
                slaqButtonWork();
                break;
        }
    }

    private void slaqButtonWork() {
        //List<String> favoriteCitiesList = ShPrefController.getAllFavoriteCities(getActivity());
        List<String> favoriteCitiesList = ShPrefController.getAllFavoriteCitiesIdList(getActivity());
        if (!favoriteCitiesList.isEmpty()) {
            if (NetworkController.isNetworkAvailable(getActivity())) {
                ((MainActivity) getActivity()).pushFragment(favoritesFragment, true, MainActivity.FAVORITE_FRAGMENT_TAG);
            } else {
                Snackbar snackbar = Snackbar.make(getActivity().findViewById(R.id.toolbar_image_view), getActivity().getResources().getString(R.string.check_connection), Snackbar.LENGTH_SHORT);
                snackbar.setAction("RETRY", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        slaqButtonWork();
                    }
                });
                snackbar.setActionTextColor(Color.RED);
                View snackBarView = snackbar.getView();
                TextView snackBarTextView = snackBarView.findViewById(android.support.design.R.id.snackbar_text);
                snackBarTextView.setTextColor(Color.YELLOW);
                snackbar.show();
            }
            return;
        }
        Toast.makeText(getActivity(), R.string.empty_favorites, Toast.LENGTH_SHORT).show();
    }


    @Override
    public void onMainFragmentClick(View view, WeatherList weatherList) {
        switch (view.getId()) {
            case R.id.custom_check_box:
                if (weatherList.isFavorite()) {
                    //ShPrefController.addFavorites(getActivity(), weatherList.getName());
                    ShPrefController.addFavoritesById(getActivity(), String.valueOf(weatherList.getId()));
                } else {
                    //ShPrefController.removeFavorite(getActivity(), weatherList.getName());
                    ShPrefController.removeFavoritesById(getActivity(), String.valueOf(weatherList.getId()));
                }
                break;
            default:
                mainItemClick(weatherList);
                break;
        }
    }

    private void mainItemClick(final WeatherList weatherList) {
        UIUtil.hideKeyboard(getActivity());
        if (NetworkController.isNetworkAvailable(getActivity())) {
            ((MainActivity) getActivity()).pushFragment(CityFragment.newInstance(weatherList.getName()), true, MainActivity.CITY_FRAGMENT_TAG);
        } else {
            Snackbar snackbar = Snackbar.make(getActivity().findViewById(R.id.toolbar_image_view), getActivity().getResources().getString(R.string.check_connection), Snackbar.LENGTH_SHORT);
            snackbar.setAction("RETRY", new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mainItemClick(weatherList);
                }
            });
            snackbar.setActionTextColor(Color.RED);
            View snackBarView = snackbar.getView();
            TextView snackBarTextView = snackBarView.findViewById(android.support.design.R.id.snackbar_text);
            snackBarTextView.setTextColor(Color.YELLOW);
            snackbar.show();
        }

    }


    @Override
    public void onFragmentsCommunicateClick(View view, WeatherList weatherList) {
        //prosto pakaca
        // Toast.makeText(getActivity(), "Worked", Toast.LENGTH_SHORT).show();


    }

    public Loader getLoader() {
        return loader;
    }
}
