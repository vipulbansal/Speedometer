package com.vipulsoftwares.distancecalculator2016;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.ComponentName;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.ServiceConnection;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.IBinder;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

public class MainActivity extends AppCompatActivity {
    android.support.v7.widget.Toolbar toolbar;
    LocationService myService;
    static boolean status;
    LocationManager locationManager;
    static TextView dist,time,speed;
    Button start,pause,stop;
    static long startTime,endTime;
    ImageView image,help;
    static ProgressDialog locate;
    static int p=0;
    private ServiceConnection sc=new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            LocationService.LocalBinder binder=(LocationService.LocalBinder)service;
            myService=binder.getService();
            status=true;
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            status=false;
        }
    };

    void bindService()
    {
        if(status==true)
            return;
        Intent i=new Intent(getApplicationContext(),LocationService.class);
        bindService(i, sc, BIND_AUTO_CREATE);
        status=true;
        startTime=System.currentTimeMillis();
    }

    void unbindService()
    {
        if(status==false)
            return;
        Intent i=new Intent(getApplicationContext(),LocationService.class);
        unbindService(sc);
        status=false;
    }

    @Override
    protected void onResume() {
        super.onResume();

    }

    @Override
    protected void onStart() {
        super.onStart();

    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(status==true)
            unbindService();
    }

    @Override
    public void onBackPressed() {
        if(status==false)
            super.onBackPressed();
        else
            moveTaskToBack(true);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);


        AdView mAdView = (AdView) findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);

        AdView mAdView1 = (AdView) findViewById(R.id.adView1);
        AdRequest adRequest1 = new AdRequest.Builder().build();
        mAdView1.loadAd(adRequest1);

        AdView mAdView2 = (AdView) findViewById(R.id.adView2);
        AdRequest adRequest2 = new AdRequest.Builder().build();
        mAdView2.loadAd(adRequest2);

        toolbar = (android.support.v7.widget.Toolbar) findViewById(R.id.tool_bar);
        setSupportActionBar(toolbar);
        help=(ImageView)toolbar.findViewById(R.id.help);
        help.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Dialog dialog1 = new Dialog(MainActivity.this, R.style.DialogSlideAnim1);
                dialog1.setContentView(R.layout.dialog_help);
                dialog1.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog1.show();

            }
        });


        dist=(TextView)findViewById(R.id.distancetext);
        time=(TextView)findViewById(R.id.timetext);
        speed=(TextView)findViewById(R.id.speedtext);

        start=(Button)findViewById(R.id.start);
        pause=(Button)findViewById(R.id.pause);
        stop=(Button)findViewById(R.id.stop);

        image=(ImageView)findViewById(R.id.image);

        start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkGps();
                locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
                if (!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
                    //Toast.makeText(this, "GPS is Enabled in your devide", Toast.LENGTH_SHORT).show();
                    return;
                }
                if(status==false)
                    bindService();
                locate=new ProgressDialog(MainActivity.this);
                locate.setIndeterminate(true);
                locate.setCancelable(false);
                locate.setMessage("Getting Location...");
                locate.show();
                start.setVisibility(View.GONE);
                pause.setVisibility(View.VISIBLE);
                pause.setText("Pause");
                stop.setVisibility(View.VISIBLE);


            }
        });

        pause.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (pause.getText().toString().equalsIgnoreCase("pause")) {
                    pause.setText("Resume");
                    p = 1;

                }
                else if(pause.getText().toString().equalsIgnoreCase("Resume"))
                {
                    checkGps();
                    locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
                    if (!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
                        //Toast.makeText(this, "GPS is Enabled in your devide", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    pause.setText("Pause");
                    p=0;

                }
            }
        });


        stop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(status==true)
                    unbindService();
                start.setVisibility(View.VISIBLE);
                pause.setText("Pause");
                pause.setVisibility(View.GONE);
                stop.setVisibility(View.GONE);
                p=0;
            }
        });
    }


    void appear()
    {
        image.setVisibility(View.VISIBLE);
        final Animation posX = new TranslateAnimation(-100, 100, 0, 0);
        posX.setDuration(500);
        posX.setFillAfter(true);
        image.startAnimation(posX);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.about) {
            Dialog dialog1=new Dialog(MainActivity.this, R.style.DialogSlideAnim1);
            dialog1.setContentView(R.layout.dialog_help);
            dialog1.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            dialog1.show();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    void checkGps()
    {
        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);

        if (!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            //Toast.makeText(this, "GPS is Enabled in your devide", Toast.LENGTH_SHORT).show();

            showGPSDisabledAlertToUser();
        }
    }

    private void showGPSDisabledAlertToUser(){
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setMessage("Enable GPS to use application")
                .setCancelable(false)
                .setPositiveButton("Enable GPS",
                        new DialogInterface.OnClickListener(){
                            public void onClick(DialogInterface dialog, int id){
                                Intent callGPSSettingIntent = new Intent(
                                        android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                                startActivity(callGPSSettingIntent);
                            }
                        });
        alertDialogBuilder.setNegativeButton("Cancel",
                new DialogInterface.OnClickListener(){
                    public void onClick(DialogInterface dialog, int id){
                        dialog.cancel();
                    }
                });
        AlertDialog alert = alertDialogBuilder.create();
        alert.show();
    }

}
