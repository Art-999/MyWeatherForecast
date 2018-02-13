package com.example.arturmusayelyan.myweatherforecast.activites;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.arturmusayelyan.myweatherforecast.R;

public class SplashActivity extends AppCompatActivity implements View.OnClickListener {
    private ProgressBar progressBar;
    private Button clickMeButton;
    private TextView poweredByTv,progressTv;
    private Handler handler;

    @SuppressLint("HandlerLeak")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        progressBar=findViewById(R.id.prog_bar_download);
        clickMeButton=findViewById(R.id.click_me_button);
        progressTv=findViewById(R.id.progress_tv);
        poweredByTv=findViewById(R.id.powered_tv);
        clickMeButton.setOnClickListener(this);
        progressBar.setMax(10);

        handler=new Handler(){
            @Override
            public void handleMessage(Message msg) {
                if(msg.what==0){
                    clickMeButton.setVisibility(View.GONE);
                    progressBar.setVisibility(View.VISIBLE);
                    poweredByTv.setVisibility(View.VISIBLE);
                    progressTv.setVisibility(View.VISIBLE);
                }
                if(msg.what>10){
                    startActivity(new Intent(SplashActivity.this,MainActivity.class));
                    finish();
                }
                if(msg.what!=0 && msg.what!=11){
                    progressBar.setProgress(msg.what);
                    progressTv.setText(msg.what*10+"%");
                }
            }
        };

    }



    @Override
    public void onClick(View v) {
        int colorResourceName = getResources().getIdentifier("blue", "color",getApplicationContext().getPackageName());
        clickMeButton.setBackgroundColor(ContextCompat.getColor(SplashActivity.this,colorResourceName));
        Thread thread=new Thread(new Runnable() {
            @Override
            public void run() {
                for (int i = 0; i <=11 ; i++) {
                    downloadFile();
                    handler.sendEmptyMessage(i);
                }
            }
        });
        thread.start();
    }
    public byte[] downloadFile() {
        SystemClock.sleep(480);
        return new byte[1024];
    }
}
