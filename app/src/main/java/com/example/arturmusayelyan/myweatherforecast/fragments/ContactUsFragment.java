package com.example.arturmusayelyan.myweatherforecast.fragments;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.example.arturmusayelyan.myweatherforecast.R;
import com.example.arturmusayelyan.myweatherforecast.dataController.ShPrefController;

/**
 * Created by artur.musayelyan on 19/03/2018.
 */

public class ContactUsFragment extends Fragment implements View.OnClickListener {
    private Button submit;
    private EditText emailAddress;
    private EditText emailBodyEditText;
    private String emailBody;
    private String reportMessage;

    public ContactUsFragment() {

    }

    public static ContactUsFragment newInstance() {
        Bundle args = new Bundle();
        ContactUsFragment fragment = new ContactUsFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_contactus, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        init(view);
    }

    private void init(View view) {
        submit = view.findViewById(R.id.submit);
        emailAddress = view.findViewById(R.id.email_field);
        emailBodyEditText = view.findViewById(R.id.message_field);
        submit.setOnClickListener(this);
    }

    private String getEmailBodyFromPreferences() {
        return ShPrefController.getReportMessage(getActivity());
    }

    private void sendEmail() {
        Intent emailIntent = new Intent(Intent.ACTION_SENDTO, Uri.fromParts("mailto", "musayelyan099@gmail.com", null));
        emailIntent.putExtra(Intent.EXTRA_SUBJECT, "Subject");
        emailBody = emailBodyEditText.getText().toString().trim();
        emailIntent.putExtra(Intent.EXTRA_TEXT, emailBody);
//        startActivity(Intent.createChooser(emailIntent, "Send email..."));
//        String message = emailBodyEditText.getText().toString().trim();
//        String yourAddress=emailAddress.getText().toString().trim();
//
//        Intent intent = new Intent(Intent.ACTION_SENDTO);
//        intent.putExtra(Intent.EXTRA_EMAIL, new String[]{"musayelyan099@gmail.com"});
//        intent.putExtra(Intent.EXTRA_TEXT, message);
//        //intent.setType("message/rfc822");
//        intent.setType("text/plain");
        // intent.setData(Uri.parse(yourAddress));
        // startActivity(intent);
        startActivity(Intent.createChooser(emailIntent, "Select Email Sending App"));
        ShPrefController.cleanReportMessage(getActivity());
        reportMessage=null;
//        startActivity(emailIntent);
//        startActivity(intent);
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onStart() {
            reportMessage = getEmailBodyFromPreferences();
        Log.d("Example", "On start worked");
        if (reportMessage != null && reportMessage.length() > 0) {
            emailBodyEditText.setText(reportMessage);
            emailBodyEditText.requestFocus(reportMessage.length());
        }
        super.onStart();
    }
    public boolean isFragmentUIActive() {
        return isAdded() && !isDetached() && isVisible();
    }

    @Override
    public void onPause() {
       // UIUtil.hideKeyboard(getActivity());

        reportMessage=emailBodyEditText.getText().toString().trim();
        ShPrefController.addReportMessage(getActivity(), reportMessage);
        super.onPause();
    }

    @Override
    public void onStop() {
        super.onStop();
        Log.d("Example", "On stop worked");
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.submit:
                sendEmail();
                break;
        }
    }
}
