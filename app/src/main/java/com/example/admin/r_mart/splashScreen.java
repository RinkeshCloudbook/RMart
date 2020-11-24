package com.example.admin.r_mart;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ProgressBar;

public class splashScreen extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);
        ProgressBar pgsBar = (ProgressBar)findViewById(R.id.pBar);

        Thread thread = new Thread(){
            public void run(){
                try{
                    super.run();
                    sleep(2500);
                }catch (Exception e){

                }finally {

                    Intent intent = new Intent(splashScreen.this,MainActivity.class);
                    startActivity(intent);
                    finish();
                }
            }
        };
        thread.start();
    }
}
