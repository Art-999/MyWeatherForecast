package com.example.arturmusayelyan.myweatherforecast.adapters;

import android.content.Context;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.arturmusayelyan.myweatherforecast.R;
import com.example.arturmusayelyan.myweatherforecast.RecyclerItemClickListener;
import com.example.arturmusayelyan.myweatherforecast.models.WeatherList;

import java.util.ArrayList;

/**
 * Created by artur.musayelyan on 26/02/2018.
 */

public class FavoriteCitiesAdapter extends RecyclerView.Adapter<FavoriteCitiesAdapter.MyViewHolder> {
    private ArrayList<WeatherList> favoriteCitiesList;
    private Context context;
    private RecyclerItemClickListener recyclerItemClickListener;

    public FavoriteCitiesAdapter(FragmentActivity context, ArrayList<WeatherList> favoriteCitiesList) {
        this.context = context;
        this.favoriteCitiesList = favoriteCitiesList;
    }

    public void setRecyclerItemClickListener(RecyclerItemClickListener itemClickListener) {
        this.recyclerItemClickListener = itemClickListener;
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
        WeatherList currentWeather = favoriteCitiesList.get(position);

        String currentCityName = currentWeather.getName();
        if (currentCityName.length() > 10) {
            holder.cityTv.setTextSize(16);
        }
        holder.cityTv.setText(currentCityName);
        holder.tempratureTv.setText((int) Double.parseDouble(String.valueOf(currentWeather.getMain().getTemp())) + "º C");

        String icon = favoriteCitiesList.get(position).getWeather().get(0).getIcon();
        downloadImage(icon, holder.weatherIcon);

        // holder.checkBox.setChecked(favoriteCitiesList.get(position).isChecked());
        holder.checkBox.setChecked(true);
    }


    @Override
    public int getItemCount() {
        return favoriteCitiesList.size();
       // return FavoritesController.getInstance().getFavoriteCitesIdList().size();
    }


    class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private TextView cityTv, tempratureTv;
        private ImageView weatherIcon;
        private CheckBox checkBox;

        public MyViewHolder(View itemView) {
            super(itemView);
            cityTv = itemView.findViewById(R.id.row_city_tv);
            tempratureTv = itemView.findViewById(R.id.row_temp_tv);
            weatherIcon = itemView.findViewById(R.id.row_city_image);
            checkBox = itemView.findViewById(R.id.custom_check_box);
            itemView.setOnClickListener(this);
            checkBox.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    recyclerItemClickListener.onItemClick(v, favoriteCitiesList.get(getAdapterPosition()), getAdapterPosition());


                    if (checkBox.isChecked()) {
                        //  favoriteCitiesList.get(getAdapterPosition()).setChecked(true);
                    } else {
                        //favoriteCitiesList.get(getAdapterPosition()).setChecked(false);
                    }
                }
            });
        }

        @Override
        public void onClick(View v) {
            recyclerItemClickListener.onItemClick(v, favoriteCitiesList.get(getAdapterPosition()), getAdapterPosition());
        }
    }

    public ArrayList<WeatherList> getList() {
        return favoriteCitiesList;
    }

}
