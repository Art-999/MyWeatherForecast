package com.example.arturmusayelyan.myweatherforecast.adapters;

import android.content.Context;
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

import java.util.List;

/**
 * Created by artur.musayelyan on 13/02/2018.
 */

public class RecyclerCityAdapter extends RecyclerView.Adapter<RecyclerCityAdapter.MyViewHolder> {
    private List<WeatherList> dataList;
    private RecyclerItemClickListener recyclerItemClickListener;
    private Context context;


    public RecyclerCityAdapter(List<WeatherList> dataList, Context context) {
        //setHasStableIds(true);
        this.dataList = dataList;
        this.context = context;
    }

    public void setRecyclerItemClickListener(RecyclerItemClickListener itemClickListener) {
        this.recyclerItemClickListener = itemClickListener;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_item, parent, false);
        MyViewHolder holder = new MyViewHolder(view);
        //holder.setIsRecyclable(false);
        return holder;
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {
        WeatherList currentWeather = dataList.get(position);

        String currentCityName = currentWeather.getName();
        if (currentCityName.length() > 10) {
            holder.cityTv.setTextSize(16);
        }
        holder.cityTv.setText(currentCityName);
        holder.tempratureTv.setText((int) Double.parseDouble(String.valueOf(currentWeather.getMain().getTemp())) + "º C");


        // Glide.with(context).load("http://openweathermap.org/img/w/"+dataList.get(holder.getAdapterPosition()).getWeather().get(0).getIcon()+".png").into(holder.weatherIcon);
        // holder.weatherIcon.setImageDrawable(null);

        //  holder.weatherIcon.setImageDrawable(null);
//                listener(new RequestListener<Drawable>() {
//            @Override
//            public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
//                return false;
//            }
//
//            @Override
//            public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
////holder.weatherIcon.setImageDrawable(resource);
//                holder.weatherIcon.setImageDrawable(null);
//                return false;
//            }
//        }).into(holder.weatherIcon);

//        if (customCitiesList != null && customCitiesList.size() >=0) {
//            //holder.checkBox.setChecked(true);
//            addChecks(holder.checkBox, currentCityName);
//        }
        String icon = dataList.get(position).getWeather().get(0).getIcon();
        downloadImage(icon, position, holder.weatherIcon, currentWeather);

        holder.checkBox.setChecked(dataList.get(position).isChecked());
    }
    private void downloadImage(String icon,int position,ImageView weatherIcon,WeatherList weatherList){
        Glide.with(context).load("http://openweathermap.org/img/w/" + icon + ".png").into(weatherIcon);
        weatherList.setIcon(icon);
        weatherList.setPosition(position);
    }




    @Override
    public int getItemCount() {

        return dataList.size();
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
//            checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
//                @Override
//                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
//                    if(buttonView.isChecked()) {
//                        dataList.get(getAdapterPosition()).setChecked(isChecked);
//                      //  recyclerItemClickListener.onItemClick(buttonView, dataList.get(getAdapterPosition()), getAdapterPosition());
//                      //  selectedItemList.add(getAdapterPosition(),String.valueOf(getAdapterPosition()));
//
//                        selectedItemList.add(String.valueOf(getAdapterPosition()));
//                       // Log.d("Art",selectedItemList.toString());
//                        recyclerItemClickListener.onItemClick(buttonView,dataList.get(getAdapterPosition()),getAdapterPosition());
//                    }
//                    else {
//                    //   selectedItemList.remove(getAdapterPosition());
//                        dataList.get(getAdapterPosition()).setChecked(isChecked);
//                        selectedItemList.remove(String.valueOf(getAdapterPosition()));
//                      //  Log.d("Art",selectedItemList.toString());
//                        recyclerItemClickListener.onItemClick(buttonView,dataList.get(getAdapterPosition()),getAdapterPosition());
//                    }
//                }
//            });
            checkBox.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(checkBox.isChecked()){
                        dataList.get(getAdapterPosition()).setChecked(true);
                    }else {
                        dataList.get(getAdapterPosition()).setChecked(false);
                    }
                    recyclerItemClickListener.onItemClick(v,dataList.get(getAdapterPosition()),getAdapterPosition());
                }
            });
        }


        @Override
        public void onClick(View view) {
            recyclerItemClickListener.onItemClick(view,dataList.get(getAdapterPosition()),getAdapterPosition());
            }


    }
}
