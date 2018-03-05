package com.example.arturmusayelyan.myweatherforecast.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.arturmusayelyan.myweatherforecast.R;
import com.example.arturmusayelyan.myweatherforecast.dataController.AllCitiesController;
import com.example.arturmusayelyan.myweatherforecast.dataController.FavoritesController;
import com.example.arturmusayelyan.myweatherforecast.interfaces.OnSwipeTouchListener;
import com.example.arturmusayelyan.myweatherforecast.interfaces.RecyclerItemClickListener;
import com.example.arturmusayelyan.myweatherforecast.models.WeatherList;

import java.util.List;

/**
 * Created by artur.musayelyan on 13/02/2018.
 */

public class RecyclerCityAdapter extends RecyclerView.Adapter<RecyclerCityAdapter.MyViewHolder>  {
    private List<WeatherList> dataList;
    private RecyclerItemClickListener recyclerItemClickListener;
    private Context context;
//    ItemTouchHelper.SimpleCallback simpleCallback=new ItemTouchHelper.SimpleCallback(ItemTouchHelper.UP|ItemTouchHelper.DOWN, ItemTouchHelper.RIGHT|ItemTouchHelper.LEFT) {
//        @Override
//        public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
//            return false;
//        }
//
//        @Override
//        public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
//
//        }
//    };


    public RecyclerCityAdapter(Context context,List<WeatherList> dataList) {
        this.dataList = dataList;
        //dataList=AllCitiesController.getInstance().getAllCitiesList();
        this.context = context;
    }

    public void setRecyclerItemClickListener(RecyclerItemClickListener itemClickListener) {
        this.recyclerItemClickListener = itemClickListener;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_item, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {
        WeatherList currentWeather = dataList.get(position);
       // WeatherList currentWeather = AllCitiesController.getInstance().getAllCitiesList().get(position);
        String currentCityName = currentWeather.getName();
        if (currentCityName.length() > 10) {
            holder.cityTv.setTextSize(16);
        }
        holder.cityTv.setText(currentCityName);
        holder.tempratureTv.setText((int) Double.parseDouble(String.valueOf(currentWeather.getMain().getTemp())) + "º C");


        String icon = dataList.get(position).getWeather().get(0).getIcon();
       // String icon = AllCitiesController.getInstance().getAllCitiesList().get(position).getWeather().get(0).getIcon();
        downloadImage(icon, position, holder.weatherIcon, currentWeather);

        // holder.checkBox.setChecked(dataList.get(position).isChecked());

//        if (FavoritesController.getInstance().getFavoriteCitesIdList().indexOf(String.valueOf(AllCitiesController.getInstance().getAllCitiesList().get(position).getId())) >= 0) {
//            holder.checkBox.setChecked(true);
//        } else {
//            holder.checkBox.setChecked(false);
//        }

       //holder.checkBox.setChecked(AllCitiesController.getInstance().getAllCitiesList().get(position).isChecked());
     //   holder.checkBox.setChecked(AllCitiesController.getInstance().getWeatherListFromPrefernces(context).get(position).isChecked());
    }


    private void downloadImage(String icon, int position, ImageView weatherIcon, WeatherList weatherList) {
        Glide.with(context).load("http://openweathermap.org/img/w/" + icon + ".png").into(weatherIcon);
        weatherList.setIcon(icon);
        weatherList.setPosition(position);
    }


    @Override
    public int getItemCount() {
        return dataList.size();
       // return AllCitiesController.getInstance().getAllCitiesList().size();
    }

    public List<WeatherList> getDataList() {
        return dataList;
    }

    class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private TextView cityTv, tempratureTv;
        private ImageView weatherIcon, addNewRow;
        private CheckBox checkBox;

        public MyViewHolder(final View itemView) {
            super(itemView);
            cityTv = itemView.findViewById(R.id.row_city_tv);
            tempratureTv = itemView.findViewById(R.id.row_temp_tv);
            weatherIcon = itemView.findViewById(R.id.row_city_image);
            addNewRow = itemView.findViewById(R.id.add_new_row);
            checkBox = itemView.findViewById(R.id.custom_check_box);
            itemView.setOnClickListener(this);


            itemView.setOnTouchListener(new OnSwipeTouchListener(context) {
                @Override
                public void onSwipeLeft() {
                    //super.onSwipeLeft();
                    Toast.makeText(context,"Left worked",Toast.LENGTH_SHORT).show();
                    dataList.remove(dataList.get(getAdapterPosition()));
                   // AllCitiesController.getInstance().removeWeatherListObject(AllCitiesController.getInstance().getAllCitiesList().get(getAdapterPosition()));
                    //AllCitiesController.getInstance().removeObjectFromPreferences(context,dataList.get(getAdapterPosition()),getAdapterPosition());
                    Log.d("ShPreferences", AllCitiesController.getInstance().getWeatherListFromPrefernces(context).size() + "");

                    notifyItemRemoved(getAdapterPosition());
                    notifyItemRangeChanged(getAdapterPosition(), dataList.size());
                }

                @Override
                public void onSwipeRight() {
                    //super.onSwipeRight();
                    Toast.makeText(context,"Right worked",Toast.LENGTH_SHORT).show();
                    dataList.remove(dataList.get(getAdapterPosition()));
                  //  AllCitiesController.getInstance().removeWeatherListObject(AllCitiesController.getInstance().getAllCitiesList().get(getAdapterPosition()));
                   // AllCitiesController.getInstance().removeObjectFromPreferences(context,dataList.get(getAdapterPosition()),getAdapterPosition());
                    Log.d("ShPreferences", AllCitiesController.getInstance().getWeatherListFromPrefernces(context).size() + "");
                    notifyItemRemoved(getAdapterPosition());
                    notifyItemRangeChanged(getAdapterPosition(), dataList.size());
                }

                @Override
                public void onClick() {
                  //  super.onClick();
                    recyclerItemClickListener.onItemClick(itemView, AllCitiesController.getInstance().getAllCitiesList().get(getAdapterPosition()), getAdapterPosition());
                }
            });

            checkBox.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (checkBox.isChecked()) {
                      //  FavoritesController.getInstance().addID(String.valueOf(dataList.get(getAdapterPosition()).getId()));
                        Log.d("Art", FavoritesController.getInstance().favoriteCitesIdListInfo());

                        AllCitiesController.getInstance().getAllCitiesList().get(getAdapterPosition()).setChecked(true);
                    } else {

                      //  FavoritesController.getInstance().removeID(String.valueOf(dataList.get(getAdapterPosition()).getId()));
                        Log.d("Art", FavoritesController.getInstance().favoriteCitesIdListInfo());

                        AllCitiesController.getInstance().getAllCitiesList().get(getAdapterPosition()).setChecked(false);
                    }
                }
            });
        }


        @Override
        public void onClick(View view) {
           // recyclerItemClickListener.onItemClick(view, dataList.get(getAdapterPosition()), getAdapterPosition());
            recyclerItemClickListener.onItemClick(view, AllCitiesController.getInstance().getAllCitiesList().get(getAdapterPosition()), getAdapterPosition());
        }

    }
}
