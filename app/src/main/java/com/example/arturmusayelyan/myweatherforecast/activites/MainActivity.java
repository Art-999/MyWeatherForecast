package com.example.arturmusayelyan.myweatherforecast.activites;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;

import com.example.arturmusayelyan.myweatherforecast.R;
import com.example.arturmusayelyan.myweatherforecast.fragments.ContactUsFragment;
import com.example.arturmusayelyan.myweatherforecast.fragments.FavoritesFragment;
import com.example.arturmusayelyan.myweatherforecast.fragments.MainFragment;
import com.example.arturmusayelyan.myweatherforecast.fragments.SettingsFragment;
import com.example.arturmusayelyan.myweatherforecast.networking.NetworkController;

import net.yslibrary.android.keyboardvisibilityevent.util.UIUtil;

public class MainActivity extends AppCompatActivity implements DrawerLayout.DrawerListener {
    public final static String MAIN_FRAGMENT_TAG = "mainFragmentTag";
    public final static String FAVORITE_FRAGMENT_TAG = "favoriteFragmentTag";
    public final static String CITY_FRAGMENT_TAG = "cityFragmentTag";
    private final static String CONTACT_US_FRAGMENT = "contactUsFragment";
    private final static String SETTINGS_FRAGMENT="settingsFragment";
    private DrawerLayout drawerLayout;
    private NavigationView navigationView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        init();
        pushFragment(MainFragment.newInstance(), false, MAIN_FRAGMENT_TAG);

        String locale=this.getResources().getConfiguration().locale.getDisplayName();
        Log.d("Locale",locale);
    }

    private void init() {
        drawerLayout = findViewById(R.id.drawer_layout);
        drawerLayout.addDrawerListener(this);
        navigationView = findViewById(R.id.navigation_view);
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                menuItem.setChecked(true);
                switch (menuItem.getItemId()) {
                    case R.id.main_list:
                        //UIUtil.hideKeyboard(MainActivity.this);
                        pushFragment(MainFragment.newInstance(), false, MAIN_FRAGMENT_TAG);
                        break;
                    case R.id.favorite_list:
                        //UIUtil.hideKeyboard(MainActivity.this);
                        pushFragment(FavoritesFragment.newInstance(), false, FAVORITE_FRAGMENT_TAG);
                        break;
                    case R.id.contact_us:
                       // UIUtil.hideKeyboard(MainActivity.this);
                        pushFragment(ContactUsFragment.newInstance(), false, CONTACT_US_FRAGMENT);
                        break;
                    case R.id.exit:
                        MainActivity.this.finish();
                        break;
                    case R.id.settings:
                        pushFragment(SettingsFragment.newInstance(),false,SETTINGS_FRAGMENT);
                        break;
                }

                drawerLayout.closeDrawers();
                return false;
            }
        });
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
            //fragment.setUserVisibleHint(true);
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

    @Override
    public void onDrawerSlide(@NonNull View drawerView, float slideOffset) {

    }

    @Override
    public void onDrawerOpened(@NonNull View drawerView) {
        UIUtil.hideKeyboard(MainActivity.this);
    }

    @Override
    public void onDrawerClosed(@NonNull View drawerView) {

    }

    @Override
    public void onDrawerStateChanged(int newState) {

    }
}
