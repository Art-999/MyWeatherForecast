package com.example.arturmusayelyan.myweatherforecast.fragments;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.arturmusayelyan.myweatherforecast.R;
import com.example.arturmusayelyan.myweatherforecast.activites.MainActivity;
import com.example.arturmusayelyan.myweatherforecast.adapters.FavoriteCitiesAdapter;
import com.example.arturmusayelyan.myweatherforecast.dataController.ShPrefController;
import com.example.arturmusayelyan.myweatherforecast.interfaces.FavoriteFragmentItemClickListener;
import com.example.arturmusayelyan.myweatherforecast.interfaces.FragmentsCommunicatorListener;
import com.example.arturmusayelyan.myweatherforecast.models.Example;
import com.example.arturmusayelyan.myweatherforecast.models.WeatherList;
import com.example.arturmusayelyan.myweatherforecast.networking.WebServiceManager;
import com.example.arturmusayelyan.myweatherforecast.views.Loader;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by artur.musayelyan on 23/02/2018.
 */

public class FavoritesFragment extends Fragment implements FavoriteFragmentItemClickListener {
    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;
    private Loader loader;
    private Context context;
    private FavoriteCitiesAdapter adapter;
    private java.util.List<WeatherList> dataList;
    private SwipeRefreshLayout swipeRefreshLayout;
    private FragmentsCommunicatorListener fragmentsCommunicatorListener;

    public FavoritesFragment() {

    }

    public static FavoritesFragment newInstance() {
        Bundle bundle = new Bundle();

        FavoritesFragment fragment = new FavoritesFragment();

        fragment.setArguments(bundle);

        return fragment;
    }

    public void setFragmentsCommunicatorListener(FragmentsCommunicatorListener fragmentsCommunicatorListener) {
        this.fragmentsCommunicatorListener = fragmentsCommunicatorListener;
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
        doGroupCitiesCallByCustomNames(true);
    }

    private void init(View view) {
        recyclerView = view.findViewById(R.id.favorites_recycler);
        loader = view.findViewById(R.id.custom_loader);
        layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);

        //fragmentsCommunicatorListener = (FragmentsCommunicatorListener) getActivity();
        swipeRefreshLayout = view.findViewById(R.id.swipe_refresh_layout);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                doGroupCitiesCallByCustomNames(false);
            }
        });
    }


    private void doGroupCitiesCallByCustomNames(final boolean isAdapterFirstInit) {
        loader.start();
        String queryByCitiesId = ShPrefController.createQueryForCall(getActivity());
        //String queryByCitiesId = ShPrefController.createQueryByFavorites(getActivity()); pordzel es dzevov anel
        Log.d("Query", queryByCitiesId);
        WebServiceManager.doCitiesGroupCallByIds(queryByCitiesId).enqueue(new Callback<Example>() {
            @Override
            public void onResponse(Call<Example> call, Response<Example> response) {
                List<WeatherList> dataList = response.body().getList();
                if (isAdapterFirstInit) {
                    initFavoriteCitesAdapter((ArrayList<WeatherList>) dataList);
                } else {
                    adapter.notifyItemRangeChanged(0, dataList.size());
                }
                if (swipeRefreshLayout != null && swipeRefreshLayout.isRefreshing()) {
                    swipeRefreshLayout.setRefreshing(false);
                }
                loader.end();
            }

            @Override
            public void onFailure(Call<Example> call, Throwable t) {
                //Toast.makeText(getActivity(), R.string.check_connection, Toast.LENGTH_LONG).show();
                if (swipeRefreshLayout != null && swipeRefreshLayout.isRefreshing()) {
                    swipeRefreshLayout.setRefreshing(false);
                }
                loader.end();
                Snackbar snackbar=Snackbar.make(getActivity().findViewById(R.id.toolbar_image_view),getActivity().getResources().getString(R.string.check_connection),Snackbar.LENGTH_SHORT);
                snackbar.setAction("RETRY", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        doGroupCitiesCallByCustomNames(false);
                    }
                });
                snackbar.setActionTextColor(Color.RED);
                View snackBarView=snackbar.getView();
                TextView snackBarTextView=snackBarView.findViewById(android.support.design.R.id.snackbar_text);
                snackBarTextView.setTextColor(Color.YELLOW);
                snackbar.show();
            }
        });
    }

    private void initFavoriteCitesAdapter(ArrayList<WeatherList> favoriteCitiesList) {
        adapter = new FavoriteCitiesAdapter(getActivity(), favoriteCitiesList);
        adapter.setFavoriteFragmentItemClickListener(this);
        recyclerView.setAdapter(adapter);
    }

public void upDateData(){
       //adapter.getList().remove()
    doGroupCitiesCallByCustomNames(true);
}
    @Override
    public void onFavoriteFragmentItemClick(View view, final WeatherList weatherList, int position) {
        switch (view.getId()) {
            case R.id.custom_check_box:

                //R.string-e chi haskanum
                // Toast.makeText(getActivity(), (weatherList.getName() + " " + getActivity().getResources().getString(R.string.removed_from_favorites)), Toast.LENGTH_SHORT).show();
                adapter.getList().remove(position);
                adapter.notifyItemRemoved(position);
                adapter.notifyItemRangeChanged(0, adapter.getList().size());
                ShPrefController.removeFavorite(getActivity(), weatherList.getName());
                final Snackbar snackbar = Snackbar.make(view, weatherList.getName() + " " + getActivity().getResources().getString(R.string.removed_from_favorites), Snackbar.LENGTH_SHORT);
                snackbar.setAction("UNDO", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        adapter.getList().add(weatherList);
                        adapter.notifyDataSetChanged();
                        ShPrefController.addFavorites(getActivity(), weatherList.getName());
                        //snackbar.dismiss();
                    }
                });
                snackbar.addCallback(new Snackbar.Callback() {
                    @Override
                    public void onDismissed(Snackbar transientBottomBar, int event) {
                        //System.out.println("event " + event);
                        if(event==Snackbar.Callback.DISMISS_EVENT_TIMEOUT){
                            if (adapter.getList().size() <= 0) {
                                ((MainActivity) getActivity()).backToHomeScreen();
                            }
                        }

                    }
                });
                snackbar.show();

                //fragmentsCommunicatorListener.onFragmentsCommunicateClick(view,weatherList);//ashxatuma uxaki pakaca
                break;
            default:
                ((MainActivity) getActivity()).pushFragment(CityFragment.newInstance(weatherList.getName()), true,MainActivity.CITY_FRAGMENT_TAG);
                break;
        }
    }
}
