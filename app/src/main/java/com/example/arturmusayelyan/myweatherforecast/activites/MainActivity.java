package com.example.arturmusayelyan.myweatherforecast.activites;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.arturmusayelyan.myweatherforecast.R;
import com.example.arturmusayelyan.myweatherforecast.adapters.RecyclerCityAdapter;
import com.example.arturmusayelyan.myweatherforecast.dataController.FavoritesController;
import com.example.arturmusayelyan.myweatherforecast.fragments.CityFragment;
import com.example.arturmusayelyan.myweatherforecast.fragments.FavoritesFragment;
import com.example.arturmusayelyan.myweatherforecast.fragments.MainFragment;
import com.example.arturmusayelyan.myweatherforecast.models.City;
import com.example.arturmusayelyan.myweatherforecast.models.WeatherList;
import com.example.arturmusayelyan.myweatherforecast.networking.ApiInterface;
import com.example.arturmusayelyan.myweatherforecast.views.Loader;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
//        init();
//        doGroupCityCall();

        pushFragment(MainFragment.newInstance(), false);
    }


    @Override
    public void onBackPressed() {
            super.onBackPressed();

    }

    public void pushFragment(Fragment fragment, boolean addToBackStack) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();

        if (addToBackStack) {
            transaction.add(R.id.base_fragment_container, fragment);
            transaction.addToBackStack(fragment.getClass().getSimpleName());
        } else {
            transaction.replace(R.id.base_fragment_container, fragment);
        }

        transaction.commit();
    }

    public void backToHomeScreen() {
        FragmentManager fragmentManager = getSupportFragmentManager();
        int backStackCount = getSupportFragmentManager().getBackStackEntryCount();
        for (int i = 0; i < backStackCount; i++) {
            int backStackFragmentId = fragmentManager.getBackStackEntryAt(i).getId();

            fragmentManager.popBackStack(backStackFragmentId, FragmentManager.POP_BACK_STACK_INCLUSIVE);
        }
    }


}
