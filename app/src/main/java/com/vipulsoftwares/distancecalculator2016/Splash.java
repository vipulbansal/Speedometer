package com.vipulsoftwares.distancecalculator2016;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.v7.app.AppCompatActivity;

/**
 * Created by vipul on 11/22/2015.
 */
public class Splash extends Activity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash);
        new Thread()
        {
            @Override
            public void run() {
                super.run();
                try {
                    sleep(1000);
                } catch (Exception e) {

                }
                finally {
                    startActivity(new Intent(Splash.this,MainActivity.class));
                    finish();
                }
            }
        }.start();
    }
}
