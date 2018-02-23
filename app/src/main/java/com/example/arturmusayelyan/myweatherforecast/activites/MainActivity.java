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
import com.example.arturmusayelyan.myweatherforecast.fragments.CityFragment;
import com.example.arturmusayelyan.myweatherforecast.fragments.MainFragment;
import com.example.arturmusayelyan.myweatherforecast.models.City;
import com.example.arturmusayelyan.myweatherforecast.models.WeatherList;
import com.example.arturmusayelyan.myweatherforecast.networking.ApiInterface;
import com.example.arturmusayelyan.myweatherforecast.views.Loader;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity  {
    public static String KEY_CITY = "cityKey";
    private RecyclerView recyclerView;
    private RecyclerCityAdapter adapter;
    private RecyclerView.LayoutManager layoutManager;
    private ApiInterface apiInterface;
    private SwipeRefreshLayout swipeRefreshLayout;
    private List<City> citysList;
    private View includeProgressView;
    private SearchView searchView;
    private List<WeatherList> dataList;
    private String localJsonString;

    private TextView toolbarTitle;
    private ImageView toolbarImage;
    private RelativeLayout searchIncludeLayout;
    private RelativeLayout relativeLayoutForRecycle;

    private Loader loader;
    private FragmentManager fragmentManager;
    private MainFragment mainFragment;
    private CityFragment cityFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
//        init();
//        doGroupCityCall();

        pushFragment(MainFragment.newInstance(),false);
    }



    public void addSeparateCityFragment(String cityName) {
//        fragmentManager=getSupportFragmentManager();
//        cityFragment=CityFragment.newInstance(cityName);
//        FragmentTransaction transaction = fragmentManager.beginTransaction();
//        MainFragment.vidibility = false;
//
//        transaction.add(R.id.base_fragment_container, CityFragment.newInstance(cityName));
//        transaction.commit();
        pushFragment(CityFragment.newInstance(cityName),true);
    }
    public void addMainFragment(){
//        fragmentManager = getSupportFragmentManager();
//        mainFragment = MainFragment.newInstance();
//        FragmentTransaction transaction = fragmentManager.beginTransaction();
//        transaction.add(R.id.base_fragment_container, mainFragment, "mainFragment");
//        MainFragment.vidibility = true;
//
//       // transaction.addToBackStack(null);
//        transaction.commit();
        pushFragment(MainFragment.newInstance(),false);
    }

    @Override
    public void onBackPressed() {
        // MainFragment mainFragment= (MainFragment) fragmentManager.findFragmentByTag("mainFragment");

//        if(!MainFragment.vidibility) {
//            FragmentTransaction transaction = fragmentManager.beginTransaction();
//            transaction.add(R.id.base_fragment_container, MainFragment.newInstance());
//            MainFragment.vidibility=true;
//            transaction.commit();
//        }
//        else if(MainFragment.vidibility) {
//            super.onBackPressed();
//        }

//        if (fragmentManager.getBackStackEntryCount() > 0) {
//            fragmentManager.popBackStackImmediate();
//            addMainFragment();
//
//            Toast.makeText(this,"Press back again to exit",Toast.LENGTH_SHORT).show();
//            return;
//        }

//        if(mainFragment!=null && mainFragment.isVisible()){
//            super.onBackPressed();
//        }
//        if(mainFragment!=null && cityFragment.isVisible()){
//            addMainFragment();
//        }

  //      backToHomeScreen();


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

    public void backToHomeScreen(){
        FragmentManager fragmentManager=getSupportFragmentManager();
        int backStackCount=getSupportFragmentManager().getBackStackEntryCount();
        for (int i = 0; i <backStackCount ; i++) {
            int backStackFragmentId=fragmentManager.getBackStackEntryAt(i).getId();

            fragmentManager.popBackStack(backStackFragmentId,FragmentManager.POP_BACK_STACK_INCLUSIVE);
        }
    }


    private void createCitysList() {
        citysList = new ArrayList<>();
//        citysList.add(new City("Yerevan", "616052"));
//        citysList.add(new City("Abovyan", "617026"));
//        citysList.add(new City("Vanadzor", "616530"));
//        citysList.add(new City("Hrazdan", "616629"));
//        citysList.add(new City("Masis", "823816"));
//        citysList.add(new City("Kapan", "174875"));
//        citysList.add(new City("Goris", "174895"));
//        citysList.add(new City("Gavar", "616599"));
//        citysList.add(new City("Gyumri", "616635"));
//        citysList.add(new City("Meghri", "174823"));
//        citysList.add(new City("Aparan", "616953"));
//        citysList.add(new City("Tashir", "616178"));
//        citysList.add(new City("Alaverdi", "616974"));
//        citysList.add(new City("Yeghegnadzor", "174710"));
//        citysList.add(new City("Dilijan", "616752"));
//        citysList.add(new City("Echmiadzin", "866096"));
    }


}
