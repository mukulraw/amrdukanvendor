package com.mrtecks.amrdukanvendor;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.messaging.FirebaseMessaging;

import java.util.Timer;
import java.util.TimerTask;

public class Splash extends AppCompatActivity {

    private static final String TAG = "Key hash";
    Timer t;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        FirebaseMessaging.getInstance().subscribeToTopic("amrdukanvendor").addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                Log.d("task", task.toString());
            }
        });

        startApp();

    }

    void startApp() {

        t = new Timer();

        t.schedule(new TimerTask() {
            @Override
            public void run() {

                final String uid = SharePreferenceUtils.getInstance().getString("id");

                if (uid.length() > 0) {

                    final String type = SharePreferenceUtils.getInstance().getString("type");

                    if (type.equals("admin")) {
                        Intent i = new Intent(Splash.this, MainActivity.class);
                        startActivity(i);
                        finishAffinity();
                    }else if (type.equals("Ecommerce"))
                    {
                        Intent i = new Intent(Splash.this, MainActivity3.class);
                        startActivity(i);
                        finishAffinity();
                    }else {
                        Intent i = new Intent(Splash.this, MainActivity2.class);
                        startActivity(i);
                        finishAffinity();
                    }

                } else {
                    Intent intent = new Intent(Splash.this, Login.class);
                    startActivity(intent);
                    finish();
                }


            }
        }, 1200);


    }

}