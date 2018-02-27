package com.example.arturmusayelyan.myweatherforecast.activites;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.WindowManager;

import com.example.arturmusayelyan.myweatherforecast.R;
import com.example.arturmusayelyan.myweatherforecast.dataController.AllCitiesController;
import com.example.arturmusayelyan.myweatherforecast.dataController.FavoritesController;
import com.example.arturmusayelyan.myweatherforecast.fragments.MainFragment;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
//        init();
//        doGroupCityCall();

        FavoritesController.getInstance().getFavoriteListFromSharedPref(this);
        AllCitiesController.getInstance().getAllCitiesListFromSharedPref(this);

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

    @Override
    protected void onDestroy() {
        super.onDestroy();
        FavoritesController.getInstance().saveFavoriteListToSharedPref(this);
        AllCitiesController.getInstance().saveAllCitiesListToSharedPref(this);
    }
}
