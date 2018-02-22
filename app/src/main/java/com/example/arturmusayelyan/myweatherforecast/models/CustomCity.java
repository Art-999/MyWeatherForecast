package com.example.arturmusayelyan.myweatherforecast.models;

/**
 * Created by artur.musayelyan on 22/02/2018.
 */

public class CustomCity {
   private String name;
    private boolean checked;

    public CustomCity() {
    }

    public CustomCity(String name, boolean checked) {
        this.name = name;
        this.checked = checked;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isChecked() {
        return checked;
    }

    public void setChecked(boolean checked) {
        this.checked = checked;
    }

    @Override
    public String toString() {
        return "CustomCity{" + "name='" + name + '\'' + ", checked=" + checked + '}';
    }
}
