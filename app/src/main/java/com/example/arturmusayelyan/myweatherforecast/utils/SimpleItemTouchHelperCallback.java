package com.example.arturmusayelyan.myweatherforecast.utils;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.widget.Toast;

import com.example.arturmusayelyan.myweatherforecast.adapters.RecyclerCityAdapter;

/**
 * Created by artur.musayelyan on 02/03/2018.
 */

public class SimpleItemTouchHelperCallback extends ItemTouchHelper.Callback  {
    private final RecyclerCityAdapter adapter;
    private Context context;

    public SimpleItemTouchHelperCallback(RecyclerCityAdapter adapter, Context context) {
        this.adapter = adapter;
        this.context = context;
    }

    @Override
    public int getMovementFlags(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
        return 0;
    }

    @Override
    public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
        return false;
    }

    @Override
    public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
        if (direction == ItemTouchHelper.LEFT) {
            Toast.makeText(context, "Swipe left ", Toast.LENGTH_SHORT).show();
        }
    }
}
