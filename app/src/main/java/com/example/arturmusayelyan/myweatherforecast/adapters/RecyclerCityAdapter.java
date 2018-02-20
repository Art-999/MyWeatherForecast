package com.example.arturmusayelyan.myweatherforecast.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.arturmusayelyan.myweatherforecast.R;
import com.example.arturmusayelyan.myweatherforecast.RecyclerCityClick;
import com.example.arturmusayelyan.myweatherforecast.models.WeatherList;

import java.util.List;

/**
 * Created by artur.musayelyan on 13/02/2018.
 */

public class RecyclerCityAdapter extends RecyclerView.Adapter<RecyclerCityAdapter.MyViewHolder> {
    private List<WeatherList> dataList;
    private RecyclerCityClick recyclerCityClick;

    public RecyclerCityAdapter(List<WeatherList> dataList) {
        this.dataList = dataList;
    }

    public void setCityClickListener(RecyclerCityClick cityClick) {
        this.recyclerCityClick = cityClick;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_item, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        WeatherList currentWeather = dataList.get(position);
        String currentWeatherName=currentWeather.getName();
        if(currentWeatherName.length()>10){
            holder.cityTv.setTextSize(16);
        }
        holder.cityTv.setText(currentWeatherName);
        holder.tempratureTv.setText((int)Double.parseDouble(String.valueOf(currentWeather.getMain().getTemp()))+"º C");
    }

    @Override
    public int getItemCount() {

        return dataList.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private TextView cityTv, tempratureTv;
        private ImageView weatherIcon;

        public MyViewHolder(View itemView) {
            super(itemView);
            cityTv = itemView.findViewById(R.id.row_city_tv);
            tempratureTv = itemView.findViewById(R.id.row_temp_tv);
            weatherIcon = itemView.findViewById(R.id.row_city_image);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            //recyclerCityClick.cityClick(cityTv.getText().toString());
            recyclerCityClick.cityClick(dataList.get(getAdapterPosition()).getName());
        }
    }
}
