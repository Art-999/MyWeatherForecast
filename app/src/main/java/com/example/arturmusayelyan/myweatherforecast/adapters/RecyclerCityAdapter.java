package com.example.arturmusayelyan.myweatherforecast.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.arturmusayelyan.myweatherforecast.R;
import com.example.arturmusayelyan.myweatherforecast.RecyclerCityClick;

import java.util.List;

/**
 * Created by artur.musayelyan on 13/02/2018.
 */

public class RecyclerCityAdapter extends RecyclerView.Adapter<RecyclerCityAdapter.MyViewHolder> {
    private List<String> cityList;
    private RecyclerCityClick recyclerCityClick;

    public RecyclerCityAdapter(List<String> cityList) {
        this.cityList = cityList;
    }
    public void setCityClickListener(RecyclerCityClick cityClick){
        this.recyclerCityClick=cityClick;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_item, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        String cityName = cityList.get(position);
        holder.cityTv.setText(cityName);
    }

    @Override
    public int getItemCount() {
        if (cityList != null) {
            return cityList.size();
        }
        return 0;
    }

    class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        private TextView cityTv;

        public MyViewHolder(View itemView) {
            super(itemView);
            cityTv = itemView.findViewById(R.id.row_city_tv);
            cityTv.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            recyclerCityClick.cityClick(cityTv.getText().toString());
        }
    }
}
