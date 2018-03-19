package com.example.arturmusayelyan.myweatherforecast.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.example.arturmusayelyan.myweatherforecast.R;

/**
 * Created by artur.musayelyan on 19/03/2018.
 */

public class ContactUsFragment extends Fragment implements View.OnClickListener {
    private Button submit;
    private EditText emailAddress;
    private EditText emailBody;

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
        emailBody = view.findViewById(R.id.message_field);
        submit.setOnClickListener(this);
    }

    private void sendEmail() {
        String message = emailBody.getText().toString().trim();
        String yourAddress=emailAddress.getText().toString().trim();

        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.putExtra(Intent.EXTRA_EMAIL, new String[]{"musayelyan099@gmail.com"});
        intent.putExtra(Intent.EXTRA_TEXT, message);
        intent.setType("message/rfc822");
       // intent.setData(Uri.parse(yourAddress));
       // startActivity(intent);
        startActivity(Intent.createChooser(intent, "Select Email Sending App"));
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
