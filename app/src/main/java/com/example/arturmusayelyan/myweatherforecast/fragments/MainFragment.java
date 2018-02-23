package com.example.arturmusayelyan.myweatherforecast.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
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
import com.example.arturmusayelyan.myweatherforecast.RecyclerItemClickListener;
import com.example.arturmusayelyan.myweatherforecast.activites.MainActivity;
import com.example.arturmusayelyan.myweatherforecast.adapters.RecyclerCityAdapter;
import com.example.arturmusayelyan.myweatherforecast.models.Example;
import com.example.arturmusayelyan.myweatherforecast.models.SeparateCity;
import com.example.arturmusayelyan.myweatherforecast.models.WeatherList;
import com.example.arturmusayelyan.myweatherforecast.networking.ApiClient;
import com.example.arturmusayelyan.myweatherforecast.networking.ApiInterface;
import com.example.arturmusayelyan.myweatherforecast.views.Loader;

import net.yslibrary.android.keyboardvisibilityevent.util.UIUtil;

import java.util.ArrayList;
import java.util.List;

import es.dmoral.toasty.Toasty;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by artur.musayelyan on 20/02/2018.
 */

public class MainFragment extends Fragment implements View.OnClickListener, RecyclerItemClickListener {
    //public static boolean MAINFRAGMENT

    private RecyclerView recyclerView;
    private RecyclerCityAdapter adapter;
    private RecyclerView.LayoutManager layoutManager;
    private ApiInterface apiInterface;
    private SwipeRefreshLayout swipeRefreshLayout;
    private SearchView searchView;
    private List<WeatherList> dataList;
    private TextView toolbarTitle;
    private ImageView toolbarImage;
    private Loader loader;
    private RecyclerItemClickListener itemClickListener;
    private ImageView slaqButton;
    private Context context;
    private ArrayList<String> selectedItemsList= new ArrayList<>();

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
        context = getActivity();
        //itemClickListener = (RecyclerItemClickListener) context;
        init(view);
        doGroupCityCall();
        // onItemClick = (RecyclerItemClickListener) getActivity();

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
//                for (int i = 0; i < dataList.size(); i++) {
//                    if (query.equalsIgnoreCase(dataList.get(i).getName())) {
//                        Toast.makeText(MainActivity.this, "This city already exist in screen", Toast.LENGTH_SHORT).show();
////                     if(i>=5){
//                        recyclerView.smoothScrollToPosition(i);
//                        //                    }
////                     else if(i==4){
////                         recyclerView.smoothScrollToPosition(i-4);
////                     }
////                     else if(i==3){
////                         recyclerView.smoothScrollToPosition(i-3);
////                     }
////                     else if(i==2){
////                         recyclerView.smoothScrollToPosition(i-2);
////                     }
////                     else if(i==1){
////                         recyclerView.smoothScrollToPosition(i-1);
////                     }
////                     else {
////                         recyclerView.smoothScrollToPosition(i);
////                     }
//
//                        return false;
//                    }
//                }
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
               // doGroupCityCall();
                adapter.notifyItemRangeChanged(0,dataList.size());

                if (swipeRefreshLayout != null && swipeRefreshLayout.isRefreshing()) {
                    swipeRefreshLayout.setRefreshing(false);
                }
                Log.d("SelectedItems",selectedItemsList.toString());
            }
        });
        layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);
        int colorResourceName = getResources().getIdentifier("blue", "color", getActivity().getPackageName());
        Toasty.Config.getInstance().setTextColor(ContextCompat.getColor(getActivity(), colorResourceName)).apply();

        apiInterface = ApiClient.getApiClient().create(ApiInterface.class);
    }

    private void initRecCityAdapter(List<WeatherList> dataList) {
        adapter = new RecyclerCityAdapter(dataList, getActivity(),selectedItemsList);
        adapter.setRecyclerItemClickListener(this);
        recyclerView.setAdapter(adapter);
    }

    private void doGroupCityCall() {
        loader.start();
        Call<Example> call = apiInterface.getCitysWeatherList();
        call.enqueue(new Callback<Example>() {
            @Override
            public void onResponse(Call<Example> call, Response<Example> response) {
                Log.d("Artur", response.body().toString());

                dataList = response.body().getList();
                if (dataList != null && !dataList.isEmpty()) {
                    initRecCityAdapter(dataList);
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

    private void doSeparateCityCall(final String cityName) {
        loader.start();
        //       final String[] tempature = {null};
        Call<SeparateCity> call = apiInterface.getCityWeather(cityName);
        call.enqueue(new Callback<SeparateCity>() {
            @Override
            public void onResponse(Call<SeparateCity> call, Response<SeparateCity> response) {
                //Log.d("Weather",response.body()+"");
                SeparateCity separateCity = response.body();
                if (separateCity != null && (separateCity.getList().size() > 0)) {

                    ((MainActivity)getActivity()).pushFragment(CityFragment.newInstance(cityName),true);
                    return;
                }

                Toast.makeText(getActivity(), R.string.type_correct, Toast.LENGTH_SHORT).show();
                toolbarImage.setVisibility(View.VISIBLE);
                toolbarTitle.setVisibility(View.VISIBLE);
                loader.end();
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
                ((MainActivity) getActivity()).pushFragment(FavoritesFragment.newInstance(), true);
                break;
        }
    }

    @Override
    public void onItemClick(View view, WeatherList weatherList, int position) {
        //doSeparateCityCall(weatherList.getName());
        switch (view.getId()) {
            case R.id.custom_check_box:
               // dataList.get(position).setChecked(weatherList.isChecked());
                selectedItemsList.add(String.valueOf(position));
                break;
            default:
                ((MainActivity) getActivity()).pushFragment(CityFragment.newInstance(weatherList.getName()), true);
                break;
        }

    }
}
