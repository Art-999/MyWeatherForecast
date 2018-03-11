package com.example.arturmusayelyan.myweatherforecast.adapters;

import android.content.Context;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.arturmusayelyan.myweatherforecast.R;
import com.example.arturmusayelyan.myweatherforecast.interfaces.FavoriteFragmentItemClickListener;
import com.example.arturmusayelyan.myweatherforecast.models.WeatherList;

import java.util.ArrayList;

/**
 * Created by artur.musayelyan on 26/02/2018.
 */

public class FavoriteCitiesAdapter extends RecyclerView.Adapter<FavoriteCitiesAdapter.MyViewHolder> {
    private ArrayList<WeatherList> favoriteCitiesWeatherList;
    private Context context;
    private FavoriteFragmentItemClickListener favoriteFragmentItemClickListener;

    public FavoriteCitiesAdapter(FragmentActivity context, ArrayList<WeatherList> favoriteCitiesList) {
        this.context = context;
        this.favoriteCitiesWeatherList = favoriteCitiesList;
    }

    public void setFavoriteFragmentItemClickListener(FavoriteFragmentItemClickListener favoriteFragmentItemClickListener) {
        this.favoriteFragmentItemClickListener = favoriteFragmentItemClickListener;
    }

    private void downloadImage(String icon, ImageView weatherIcon) {
        Glide.with(context).load("http://openweathermap.org/img/w/" + icon + ".png").into(weatherIcon);
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.favorite_row_item, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        WeatherList currentWeather = favoriteCitiesWeatherList.get(position);

        String currentCityName = currentWeather.getName();
        if (currentCityName.length() > 10) {
            holder.cityTv.setTextSize(16);
        }
        holder.cityTv.setText(currentCityName);
        holder.temperatureTv.setText((int) Double.parseDouble(String.valueOf(currentWeather.getMain().getTemp())) + "º C");

        String icon = favoriteCitiesWeatherList.get(position).getWeather().get(0).getIcon();
        downloadImage(icon, holder.weatherIcon);

        // holder.checkBox.setFavorite(favoriteCitiesWeatherList.get(position).isFavorite());
        holder.checkBox.setChecked(true);
    }


    @Override
    public int getItemCount() {
        return favoriteCitiesWeatherList.size();
    }


    class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private TextView cityTv, temperatureTv;
        private ImageView weatherIcon;
        private CheckBox checkBox;

        public MyViewHolder(View itemView) {
            super(itemView);
            cityTv = itemView.findViewById(R.id.row_city_tv);
            temperatureTv = itemView.findViewById(R.id.row_temp_tv);
            weatherIcon = itemView.findViewById(R.id.row_city_image);
            checkBox = itemView.findViewById(R.id.custom_check_box);
            itemView.setOnClickListener(this);
            checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if(buttonView.isPressed()){
                        favoriteFragmentItemClickListener.onFavoriteFragmentItemClick(buttonView,favoriteCitiesWeatherList.get(getAdapterPosition()),getAdapterPosition());
                    }
                }
            });
        }

        @Override
        public void onClick(View v) {
            favoriteFragmentItemClickListener.onFavoriteFragmentItemClick(v, favoriteCitiesWeatherList.get(getAdapterPosition()), getAdapterPosition());
        }
    }

    public ArrayList<WeatherList> getList() {
        return favoriteCitiesWeatherList;
    }

}
