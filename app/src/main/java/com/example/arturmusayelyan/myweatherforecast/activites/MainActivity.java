package com.example.arturmusayelyan.myweatherforecast.activites;

import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;

import com.example.arturmusayelyan.myweatherforecast.R;
import com.example.arturmusayelyan.myweatherforecast.fragments.FavoritesFragment;
import com.example.arturmusayelyan.myweatherforecast.fragments.MainFragment;
import com.example.arturmusayelyan.myweatherforecast.networking.NetworkController;

public class MainActivity extends AppCompatActivity {
    public final static String MAIN_FRAGMENT_TAG = "mainFragmentTag";
    public final static String FAVORITE_FRAGMENT_TAG = "favoriteFragmentTag";
    public final static String CITY_FRAGMENT_TAG = "cityFragmentTag";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        pushFragment(MainFragment.newInstance(), false, MAIN_FRAGMENT_TAG);
    }


    @Override
    public void onBackPressed() {
        if (NetworkController.isNetworkAvailable(this)) {
            MainFragment mainFragment = (MainFragment) getSupportFragmentManager().findFragmentByTag(MAIN_FRAGMENT_TAG);
            if (mainFragment != null && mainFragment.isVisible() && getSupportFragmentManager().getBackStackEntryCount() > 0) {
                mainFragment.upDateData();
            }
            FavoritesFragment favoritesFragment = (FavoritesFragment) getSupportFragmentManager().findFragmentByTag(FAVORITE_FRAGMENT_TAG);
            if (favoritesFragment != null && favoritesFragment.isVisible()) {
                favoritesFragment.upDateData();
            }
            super.onBackPressed();
        } else {
            final Snackbar snackbar = Snackbar.make(findViewById(R.id.toolbar_image_view), getResources().getString(R.string.check_connection), Snackbar.LENGTH_SHORT);
            snackbar.setAction("BACK", new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    MainActivity.super.onBackPressed();
                }
            });
            snackbar.setActionTextColor(Color.RED);
            View snackBarView = snackbar.getView();
            TextView snackBarTextView = snackBarView.findViewById(android.support.design.R.id.snackbar_text);
            snackBarTextView.setTextColor(Color.YELLOW);
            snackbar.show();
        }
    }

    public void pushFragment(Fragment fragment, boolean addToBackStack, String tag) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();

        if (addToBackStack) {
            transaction.add(R.id.base_fragment_container, fragment, tag);
            transaction.addToBackStack(fragment.getClass().getSimpleName());
        } else {
            transaction.replace(R.id.base_fragment_container, fragment, tag);
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
        //avelacrel em
        MainFragment mainFragment = (MainFragment) getSupportFragmentManager().findFragmentByTag(MAIN_FRAGMENT_TAG);
        if (mainFragment != null && mainFragment.isVisible() && getSupportFragmentManager().getBackStackEntryCount() > 0) {
            mainFragment.upDateData();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
