package com.example.arturmusayelyan.myweatherforecast.services;

import android.annotation.TargetApi;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.job.JobParameters;
import android.app.job.JobService;
import android.content.Intent;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.example.arturmusayelyan.myweatherforecast.R;
import com.example.arturmusayelyan.myweatherforecast.activites.MainActivity;
import com.example.arturmusayelyan.myweatherforecast.dataController.ShPrefController;
import com.example.arturmusayelyan.myweatherforecast.fragments.SettingsFragment;
import com.example.arturmusayelyan.myweatherforecast.models.List;
import com.example.arturmusayelyan.myweatherforecast.models.SeparateCity;
import com.example.arturmusayelyan.myweatherforecast.networking.WebServiceManager;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


@TargetApi(Build.VERSION_CODES.LOLLIPOP)
@RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
public class NotificationJobService extends JobService {
    private final String CLEAR_SKY = "Clear";
    private final String CLOUDS = "Clouds";
    private final String RAIN = "Rain";
    private final String THUNDERSTORM = "Thunderstorm";
    private final String SNOW = "Snow";
    private final String MIST = "Mist";
    private final String HAZE = "Haze";
    private final String DRIZZLE = "DRIZZLE";


    @Override
    public boolean onStartJob(JobParameters params) {
        //String selectedCity = ShPrefController.getSpinnerSelectedItemName(this);
        Log.d("OnStartJob","worked1");
        String selectedCity = null;
        if (SettingsFragment.isAppAlive) {
            Log.d("OnStartJob","condition 1");
            selectedCity = SettingsFragment.selectedCityName;
        } else if (!SettingsFragment.isAppAlive && ShPrefController.getToggleButtonState(this)) {
            Log.d("OnStartJob","condition 2");
            selectedCity = ShPrefController.getSpinnerSelectedItemName(this);
        }

        if (selectedCity != null) {
            Log.d("OnStartJob","condition 3");
            if (SettingsFragment.isAppAlive && SettingsFragment.toogleButtonStateChacked) {
                Log.d("OnStartJob","condition 4");
                doSelectedCityCall(selectedCity);
            }else if(!SettingsFragment.isAppAlive && ShPrefController.getToggleButtonState(this)){
                Log.d("OnStartJob","condition 5");
                doSelectedCityCall(selectedCity);
            }

        }
        return false;
    }

    @Override
    public boolean onStopJob(JobParameters params) {
        return true;
    }

    public void doSelectedCityCall(final String selectedCity) {
        WebServiceManager.doCityWeatherCallByName(selectedCity).enqueue(new Callback<SeparateCity>() {
            @Override
            public void onResponse(Call<SeparateCity> call, Response<SeparateCity> response) {
                SeparateCity separateCity = response.body();
                if (separateCity != null) {
                    List currentCity = separateCity.getList().get(0);
                    String weatherDescription = currentCity.getWeather().get(0).getMain();
                    String weatherIcon = currentCity.getWeather().get(0).getIcon();
                    if (weatherDescription != null && weatherIcon != null) {
                        intNotifications(weatherDescription, weatherIcon);
                    }
                }
            }

            @Override
            public void onFailure(Call<SeparateCity> call, Throwable t) {

            }
        });
    }

    private void intNotifications(String weatherDescription, String weatherIcon) {
        PendingIntent contentPendingIntent = PendingIntent.getActivity(this, 0, new Intent(this, MainActivity.class), PendingIntent.FLAG_UPDATE_CURRENT);
        NotificationManager manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this).
                setContentTitle(getString(R.string.app_name)).setContentIntent(contentPendingIntent).setPriority(NotificationCompat.PRIORITY_HIGH).setDefaults(NotificationCompat.DEFAULT_ALL).setAutoCancel(true);
//                    .setContentText("it's rainy today take umbrella with you")
//                    .setSmallIcon(R.drawable.rainy);
        switch (weatherDescription) {
            case CLEAR_SKY:
                if (weatherIcon.endsWith("d")) {
                    builder.setSmallIcon(R.drawable.sun_day);
                    builder.setContentText("it's fine weather outside");
                } else {
                    builder.setSmallIcon(R.drawable.sun_night);
                    builder.setContentText("it's good time for evening walk");
                }
                break;
            case CLOUDS:
                builder.setSmallIcon(R.drawable.notif_clouds);
                builder.setContentText("it's cloudy day think before going outside");
                break;
            case RAIN:
                builder.setSmallIcon(R.drawable.rainy);
                builder.setContentText("it's rainy today take umbrella with you");
                break;
            case THUNDERSTORM:
                builder.setSmallIcon(R.drawable.notif_thunderstorm);
                builder.setContentText("it's thunderstorm don’t go out");
                break;
            case HAZE:
                builder.setSmallIcon(R.drawable.notif_haze);
                builder.setContentText("it's haze in outside");
                break;
            case SNOW:
                builder.setSmallIcon(R.drawable.notif_snow);
                builder.setContentText("ooo it's coming snow outside lets go playing with snow");
                break;
            case DRIZZLE:
                if (weatherIcon.endsWith("d")) {
                    builder.setSmallIcon(R.drawable.notif_drizzle_day);
                    builder.setContentText("it's drizzle day in outside");
                } else {
                    builder.setSmallIcon(R.drawable.notif_drizzle_night);
                    builder.setContentText("it's drizzle night in outside");
                }
                break;
            case MIST:
                builder.setSmallIcon(R.drawable.notif_mist);
                builder.setContentText("it's mist in outside");
                break;
        }

        manager.notify(0, builder.build());

    }
}
