package com.example.arturmusayelyan.myweatherforecast.fragments;

import android.app.job.JobInfo;
import android.app.job.JobScheduler;
import android.content.ComponentName;
import android.content.Context;
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
import android.widget.Toast;
import android.widget.ToggleButton;

import com.example.arturmusayelyan.myweatherforecast.R;
import com.example.arturmusayelyan.myweatherforecast.dataController.ShPrefController;
import com.example.arturmusayelyan.myweatherforecast.services.NotificationJobService;

public class SettingsFragment extends Fragment implements CompoundButton.OnCheckedChangeListener, AdapterView.OnItemSelectedListener {
    private ToggleButton toggleButton;
    private Spinner spinner;
    private int spinnerSelectedId;
    private JobScheduler myJobScheduler;
    private static int JOB_ID = 0;
    public static String selectedCityName;
    public static boolean isAppAlive;
    public static boolean toggleButtonStateChecked;

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
        isAppAlive =true;
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
            //cancelJob();
            // Log.d("Names", ShPrefController.getAllFavoriteCitiesNameList(getActivity()).toString());
            toggleButtonStateChecked =true;
            spinner.setEnabled(true);
                scheduleJob();
        } else {
            //  Log.d("Names", ShPrefController.getAllFavoriteCitiesNameList(getActivity()).toString());
            toggleButtonStateChecked =false;
            spinner.setEnabled(false);
            cancelJob();
            Toast.makeText(getActivity(), "At this time you don’t receive notification about weather", Toast.LENGTH_LONG).show();
        }

    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        cancelJob();
        spinnerSelectedId = position;
        Log.d("ID", spinnerSelectedId + "");
        selectedCityName=spinner.getSelectedItem().toString();

        scheduleJob();
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    @Override
    public void onResume() {
        super.onResume();
        if (ShPrefController.getSpinnerSelectedItemId(getActivity()) != null) {
            spinnerSelectedId = Integer.parseInt(ShPrefController.getSpinnerSelectedItemId(getActivity()));
            if (ShPrefController.getAllFavoriteCitiesNameList(getActivity()).contains(ShPrefController.getSpinnerSelectedItemName(getActivity()))) {
                spinner.setSelection(spinnerSelectedId);
            }
        }
        toggleButton.setChecked(ShPrefController.getToggleButtonState(getActivity()));
    }

    @Override
    public void onPause() {
        super.onPause();
        ShPrefController.addSpinnerSelectedItemId(getActivity(), spinnerSelectedId);
        //ShPrefController.addSpinnerSelectedItemName(getActivity(), spinner.getAdapter().getItem(spinnerSelectedId).toString());
        ShPrefController.addSpinnerSelectedItemName(getActivity(), selectedCityName);

        ShPrefController.saveToggleButtonState(getActivity(), toggleButton.isChecked());
    }


    public void scheduleJob() {
        myJobScheduler = (JobScheduler) getActivity().getSystemService(Context.JOB_SCHEDULER_SERVICE);
        ComponentName serviceName = new ComponentName(getActivity().getPackageName(), NotificationJobService.class.getName());

        JobInfo.Builder builder=new JobInfo.Builder(JOB_ID,serviceName);
        builder.setRequiredNetworkType(JobInfo.NETWORK_TYPE_ANY);
        builder.setPeriodic(10000);

        JobInfo myJobInfo=builder.build();
        myJobScheduler.schedule(myJobInfo);
    }



    public void cancelJob() {
        if (myJobScheduler != null) {
            myJobScheduler.cancelAll();
            myJobScheduler = null;
        }
    }

    @Override
    public void onStop() {
        isAppAlive =false;
        super.onStop();
    }

    @Override
    public void onDestroy() {

        super.onDestroy();
    }
}
