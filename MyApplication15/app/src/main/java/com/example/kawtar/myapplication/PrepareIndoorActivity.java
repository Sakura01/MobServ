package com.example.kawtar.myapplication;

/**
 * Created by Kawtar on 12/3/2014.
 */
import java.io.InputStream;
import java.net.URL;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Activity;
import android.app.ProgressDialog;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

import com.kawtar.indoornavigation.IndoorMapActivity;
import com.kawtar.mainUI.SplashActivity;
import com.kawtar.networkcontrol.ReachabilityTest;

public class PrepareIndoorActivity extends Activity {
    public Bitmap bitmap;
    public ProgressDialog pDialog;
    public static Bitmap indoorMap;
    private Intent mIntent;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.preparation_indoor_activity);
        new ReachabilityTest(getApplicationContext(), "google.fr", 80, new ReachabilityTest.Callback() {
            public void onReachabilityTestPassed()
            {

                new LoadImage().execute(OutdoorMapActivity.superMarketMap);
            }
            @Override
            public void onReachabilityTestFailed() {
                createDialog("Network", "Please check your connection");
            }
        }).execute();

    }
    public void createDialog(final String title, String text) {
        AlertDialog.Builder ad = new AlertDialog.Builder(PrepareIndoorActivity.this);
        // Button to create an alert dialog for setting the connection to the server
        ad.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                if(title.equals("Network"))
                {
                    startActivityForResult(new Intent(android.provider.Settings.ACTION_SETTINGS), 0);
                }
                else{
                    Intent activity=new Intent(PrepareIndoorActivity.this, OutdoorMapActivity.class);
                    startActivity(activity);
                }
            }
        });
        // Set the alert dialog title, create it and show it
        ad.setTitle(title + ":" + text);
        AlertDialog alert1 = ad.create();
        alert1.show();
    }
    private class LoadImage extends AsyncTask<String, String, Bitmap> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(PrepareIndoorActivity.this);
            pDialog.setMessage("Loading the indoor Map ....");
            pDialog.show();
        }
        protected Bitmap doInBackground(String... args) {
            try {
                bitmap = BitmapFactory.decodeStream((InputStream)new URL(args[0]).getContent());
            } catch (Exception e) {
                e.printStackTrace();
            }
            return bitmap;
        }
        protected void onPostExecute(Bitmap image) {
            if(image != null){
                saveIndoorMap(image);
                pDialog.dismiss();
                mIntent=new Intent(PrepareIndoorActivity.this, IndoorMapActivity.class);
                startActivity(mIntent);
            }else{
                pDialog.dismiss();
                createDialog("Error","A problem occured while loading the super market map");
            }
        }
    }
    public void saveIndoorMap(Bitmap bmp)
    {
        indoorMap=bmp;
    }
    public static Bitmap getIndoorMap()
    {
        if(indoorMap==null)
        {
            Log.i("Prepare map", "Map nulle");
        }
        return indoorMap;
    }
    public void onBackPressed() {

        if (SplashActivity.CONDITION)
        {
            // do nothing
        }
        else
            super.onBackPressed();

    }
}