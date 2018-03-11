package com.example.arturmusayelyan.myweatherforecast.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.arturmusayelyan.myweatherforecast.R;
import com.example.arturmusayelyan.myweatherforecast.dataController.AllCitiesController;
import com.example.arturmusayelyan.myweatherforecast.dataController.ShPrefController;
import com.example.arturmusayelyan.myweatherforecast.interfaces.MainFragmentItemClickListener;
import com.example.arturmusayelyan.myweatherforecast.interfaces.OnSwipeTouchListener;
import com.example.arturmusayelyan.myweatherforecast.interfaces.FavoriteFragmentItemClickListener;
import com.example.arturmusayelyan.myweatherforecast.models.WeatherList;

import java.util.List;

/**
 * Created by artur.musayelyan on 13/02/2018.
 */

public class RecyclerCityAdapter extends RecyclerView.Adapter<RecyclerCityAdapter.MyViewHolder> {
    private List<WeatherList> dataList;
    private MainFragmentItemClickListener mainFragmentItemClickListener;
    private Context context;

    public RecyclerCityAdapter(Context context, List<WeatherList> dataList) {
        this.dataList = dataList;
        this.context = context;
    }

    public void setRecyclerItemClickListener(MainFragmentItemClickListener mainFragmentItemClickListener) {
        this.mainFragmentItemClickListener = mainFragmentItemClickListener;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_item, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        WeatherList currentWeather = dataList.get(position);
        Log.d("Art", currentWeather.toString());
        String currentCityName = currentWeather.getName();
        if (currentCityName.length() > 10) {
            holder.cityTv.setTextSize(16);
        }
        holder.cityTv.setText(currentCityName);
        holder.temperatureTv.setText((int) Double.parseDouble(String.valueOf(currentWeather.getMain().getTemp())) + "º C");
        String icon = currentWeather.getWeather().get(0).getIcon();
        downloadImage(icon, holder.weatherIcon);

        holder.checkBox.setChecked(currentWeather.isFavorite());
        //holder.checkBox.setFavorite(AllCitiesController.getInstance().getWeatherListFromPrefernces(context).get(position).isFavorite());
    }


    private void downloadImage(String icon, ImageView weatherIcon) {
        Glide.with(context).load("http://openweathermap.org/img/w/" + icon + ".png").into(weatherIcon);
    }


    @Override
    public int getItemCount() {
        // Log.d("Art",dataList.size()+"");
        return dataList.size();
    }

    public List<WeatherList> getDataList() {
        return dataList;
    }

    class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private TextView cityTv, temperatureTv;
        private ImageView weatherIcon;
        private CheckBox checkBox;

        public MyViewHolder(final View itemView) {
            super(itemView);
            cityTv = itemView.findViewById(R.id.row_city_tv);
            temperatureTv = itemView.findViewById(R.id.row_temp_tv);
            weatherIcon = itemView.findViewById(R.id.row_city_image);
            checkBox = itemView.findViewById(R.id.custom_check_box);
            itemView.setOnClickListener(this);
//            itemView.setOnTouchListener(new OnSwipeTouchListener(context) {
//                @Override
//                public void onSwipeLeft() {
//                    Toast.makeText(context, "Left worked", Toast.LENGTH_SHORT).show();
//                    dataList.remove(dataList.get(getAdapterPosition()));
//
//                    notifyItemRemoved(getAdapterPosition());
//                    notifyItemRangeChanged(getAdapterPosition(), dataList.size());
//                }
//
//                @Override
//                public void onSwipeRight() {
//                    Toast.makeText(context, "Right worked", Toast.LENGTH_SHORT).show();
//                    dataList.remove(dataList.get(getAdapterPosition()));
//
//                    notifyItemRemoved(getAdapterPosition());
//                    notifyItemRangeChanged(getAdapterPosition(), dataList.size());
//                }
//
//                @Override
//                public void onClick() {
//                    mainFragmentItemClickListener.onMainFragmentClick(itemView, dataList.get(getAdapterPosition()));
//                }
//            });

            checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if (buttonView.isPressed()) {
                        WeatherList pressedItemWeatherList=dataList.get(getAdapterPosition());
                        if (isChecked) {
                            pressedItemWeatherList.setFavorite(true);
                            //ShPrefController.addFavorites(context, dataList.get(getAdapterPosition()).getName());
                        } else {
                            pressedItemWeatherList.setFavorite(false);
                           // ShPrefController.removeFavorite(context, dataList.get(getAdapterPosition()).getName());
                        }
                        mainFragmentItemClickListener.onMainFragmentClick(buttonView,pressedItemWeatherList);
                    }
                }
            });
        }


        @Override
        public void onClick(View view) {
            mainFragmentItemClickListener.onMainFragmentClick(itemView, dataList.get(getAdapterPosition()));
        }

    }
}
