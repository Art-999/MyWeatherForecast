package com.example.arturmusayelyan.myweatherforecast.fragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CompoundButton;
import android.widget.Spinner;
import android.widget.ToggleButton;

import com.example.arturmusayelyan.myweatherforecast.R;
import com.example.arturmusayelyan.myweatherforecast.dataController.ShPrefController;

public class SettingsFragment extends Fragment implements CompoundButton.OnCheckedChangeListener, AdapterView.OnItemSelectedListener {
    private ToggleButton toggleButton;
    private Spinner spinner;
    private int spinerSelectedId;

    public SettingsFragment() {

    }

    public static SettingsFragment newInstance() {
        Bundle args = new Bundle();
        SettingsFragment fragment = new SettingsFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_settings, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        init(view);
    }

    private void init(View view) {
        toggleButton = (ToggleButton) view.findViewById(R.id.toggle_button);
        spinner = view.findViewById(R.id.spinner);
        toggleButton.setOnCheckedChangeListener(this);

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, ShPrefController.getAllFavoriteCitiesNameList(getActivity()));
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(this);
        spinner.setEnabled(false);
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        if (isChecked) {
            // Log.d("Names", ShPrefController.getAllFavoriteCitiesNameList(getActivity()).toString());
            spinner.setEnabled(true);
        } else {
            //  Log.d("Names", ShPrefController.getAllFavoriteCitiesNameList(getActivity()).toString());
            spinner.setEnabled(false);
        }

    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        spinerSelectedId = position;
        Log.d("ID", spinerSelectedId + "");
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    @Override
    public void onResume() {
        super.onResume();
        if (ShPrefController.getSpinnerSelectedItemId(getActivity()) != null) {
            spinerSelectedId = Integer.parseInt(ShPrefController.getSpinnerSelectedItemId(getActivity()));
            if(ShPrefController.getAllFavoriteCitiesNameList(getActivity()).contains(ShPrefController.getSpinnerSelectedItemName(getActivity()))){
                spinner.setSelection(spinerSelectedId);
            }

        }
    }

    @Override
    public void onPause() {
        super.onPause();
        ShPrefController.addSpinnerSelectedItemId(getActivity(), spinerSelectedId);
        ShPrefController.addSpinnerSelectedItemName(getActivity(),spinner.getAdapter().getItem(spinerSelectedId).toString());
    }
}
