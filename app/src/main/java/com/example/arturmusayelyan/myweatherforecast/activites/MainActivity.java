package com.example.arturmusayelyan.myweatherforecast.activites;

import android.os.Bundle;
import android.os.SystemClock;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.arturmusayelyan.myweatherforecast.R;
import com.example.arturmusayelyan.myweatherforecast.RecyclerCityClick;
import com.example.arturmusayelyan.myweatherforecast.adapters.RecyclerCityAdapter;
import com.example.arturmusayelyan.myweatherforecast.models.City;
import com.example.arturmusayelyan.myweatherforecast.models.Example;
import com.example.arturmusayelyan.myweatherforecast.models.SeparateCity;
import com.example.arturmusayelyan.myweatherforecast.models.WeatherList;
import com.example.arturmusayelyan.myweatherforecast.networking.ApiClient;
import com.example.arturmusayelyan.myweatherforecast.networking.ApiInterface;
import com.example.arturmusayelyan.myweatherforecast.views.Loader;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import net.yslibrary.android.keyboardvisibilityevent.util.UIUtil;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.StringWriter;
import java.io.UnsupportedEncodingException;
import java.io.Writer;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import es.dmoral.toasty.Toasty;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity implements RecyclerCityClick {
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

    private Loader loader;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        init();
        doGroupCityCall();

    }

    private void init() {
        recyclerView = findViewById(R.id.recycler_view);
        loader = findViewById(R.id.custom_loader);
//        includeProgressView = findViewById(R.id.progress_layout);
//        includeProgressView.setVisibility(View.VISIBLE);
        toolbarTitle = findViewById(R.id.toolbar_title);
        toolbarImage = findViewById(R.id.toolbar_image_view);
        searchIncludeLayout = findViewById(R.id.search_include_layout);

        searchView = findViewById(R.id.search_view);
        EditText searchEditText = (EditText) searchView.findViewById(android.support.v7.appcompat.R.id.search_src_text);
        searchEditText.setTextColor(getResources().getColor(R.color.white));
        searchEditText.setHint(getResources().getString(R.string.search_hint));
        searchEditText.setHintTextColor(getResources().getColor(R.color.yellow));
        searchEditText.setBackgroundColor(getResources().getColor(R.color.light_blue));
        searchView.setOnSearchClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toolbarTitle.setVisibility(View.GONE);
                toolbarImage.setVisibility(View.GONE);

            }
        });
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                UIUtil.hideKeyboard(MainActivity.this);
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
                UIUtil.hideKeyboard(MainActivity.this);
                recyclerView.smoothScrollToPosition(0);

                toolbarTitle.setVisibility(View.VISIBLE);
                toolbarImage.setVisibility(View.VISIBLE);
                return false;
            }
        });

        swipeRefreshLayout = findViewById(R.id.swipe_refresh_layout);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                doGroupCityCall();
                //doSeparateCityCall();
            }
        });
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);
        int colorResourceName = getResources().getIdentifier("blue", "color", getApplicationContext().getPackageName());
        Toasty.Config.getInstance().setTextColor(ContextCompat.getColor(this, colorResourceName)).apply();

        apiInterface = ApiClient.getApiClient().create(ApiInterface.class);
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

    private void initRecCityAdapter(List<WeatherList> dataList) {
        adapter = new RecyclerCityAdapter(dataList);
        adapter.setCityClickListener(this);
        recyclerView.setAdapter(adapter);
    }

    @Override
    public void cityClick(WeatherList weatherList) {
        //     Gson gson = new Gson();

//        Toast toast = Toasty.normal(this, "   " + weatherList.getName() + "   ");
//        toast.setGravity(Gravity.CENTER, 0, 0);
//        toast.show();
//        Intent intent = new Intent(this, WeatherActivity.class);
//        intent.putExtra(KEY_CITY, gson.toJson(city));
//        startActivity(intent);
    }

    private void doGroupCityCall() {
        loader.start();
        Call<Example> call = apiInterface.getCitysWeatherList();
        call.enqueue(new Callback<Example>() {
            @Override
            public void onResponse(Call<Example> call, Response<Example> response) {
                Log.d("Artur", response.body().toString());
//                Main mainData=response.body().getMain();
//
//                Double temp=mainData.getTemp();
//                int tempInt=Integer.valueOf(temp.intValue());
//                Log.d("Artur",tempInt+"");

                dataList = response.body().getList();
                if (dataList != null && !dataList.isEmpty()) {
                    initRecCityAdapter(dataList);
                }

                if (swipeRefreshLayout != null && swipeRefreshLayout.isRefreshing()) {
                    swipeRefreshLayout.setRefreshing(false);
                }
                progressStop();
            }

            @Override
            public void onFailure(Call<Example> call, Throwable t) {
                Toast.makeText(MainActivity.this, "Check your Internet Connection", Toast.LENGTH_LONG).show();

                if (swipeRefreshLayout != null && swipeRefreshLayout.isRefreshing()) {
                    swipeRefreshLayout.setRefreshing(false);
                }

                progressStop();
            }
        });
    }


    private void doSeparateCityCall(String cityName) {
        loader.start();
        final String[] tempature = {null};
        Call<SeparateCity> call = apiInterface.getCityWeather(cityName);
        call.enqueue(new Callback<SeparateCity>() {
            @Override
            public void onResponse(Call<SeparateCity> call, Response<SeparateCity> response) {
                //Log.d("Weather",response.body()+"");
                SeparateCity separateCity = response.body();
                if (separateCity != null && (separateCity.getList().size() > 0)) {
                    Double temp = separateCity.getList().get(0).getMain().getTemp();
                    int tempInt = Integer.valueOf(temp.intValue());
                     tempature[0] = String.valueOf(tempInt);

                }

                    Thread thread=new Thread(new Runnable() {
                        @Override
                        public void run() {
                            SystemClock.sleep(150);
                            MainActivity.this.runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    if(tempature[0]!=null) {
                                        Toast.makeText(MainActivity.this, tempature[0], Toast.LENGTH_SHORT).show();
                                        return;
                                    }
                                    Toast.makeText(MainActivity.this, "Type city name in correct", Toast.LENGTH_LONG).show();
                                }
                            });
                        }
                    });
                    thread.start();

                progressStop();
                }



//                if (swipeRefreshLayout != null && swipeRefreshLayout.isRefreshing()) {
//                    swipeRefreshLayout.setRefreshing(false);
//                }


            @Override
            public void onFailure(Call<SeparateCity> call, Throwable t) {
//                Log.d("AAAA", t.toString());
                Toast.makeText(MainActivity.this, t.toString(), Toast.LENGTH_LONG).show();
                progressStop();
            }
        });
    }


    private void progressStop() {
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                SystemClock.sleep(150);
                MainActivity.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        //includeProgressView.setVisibility(View.GONE);
                        loader.end();
                    }
                });
            }
        });
        thread.start();
       // loader.end();
    }

    private void readJsonFile() throws IOException {
        InputStream is = getResources().openRawResource(R.raw.city_list);
        Writer writer = new StringWriter();
        char[] buffer = new char[1024];
        try {
            Reader reader = new BufferedReader(new InputStreamReader(is, "UTF-8"));
            int n;
            while ((n = reader.read(buffer)) != -1) {
                writer.write(buffer, 0, n);
            }
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            is.close();
        }

        localJsonString = writer.toString();
        Log.d("Art", localJsonString);
    }

    private List<City> gsonParceToList(String gsonObject) {
        Gson gson = new Gson();
        Type exampleListType = new TypeToken<List<City>>() {
        }.getType();
        List<City> dataList = gson.fromJson(gsonObject, exampleListType);
        return dataList;
    }

}
