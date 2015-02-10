package com.example.kawtar.myapplication;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.PendingIntent;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;

import android.util.Log;
import android.view.WindowManager;
import android.widget.Toast;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.OnMapLongClickListener;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import com.kawtar.finalresponse.FinalResponseAdapter;
import com.kawtar.jsoncontrol.ResponseFromServer;
import com.kawtar.listshopping.ProductToSend;
import com.shopping.list.ShoppinglistActivity;

import android.location.Criteria;
import android.content.Context;
import android.location.Location;
import android.location.LocationListener;
import android.provider.Settings;

import java.util.List;

public class OutdoorMapActivity extends FragmentActivity implements LocationListener{

    public GoogleMap googleMap;
    public LocationManager locationManager;
    public PendingIntent pendingIntent;
    private static final LatLng MOUNTAIN_VIEW = new LatLng(37.4, -122.1);
    private static final LatLng SUPERMARKET = new LatLng(43.58041799999999, 7.12510199999997);
    private  LatLng MOI;
    public Location location;
    public boolean enabledNet=false;
    public boolean enabledGPS=false;
    private Intent proximityIntent;
    private double superMarketLatitude;
    private double superMarketLongitude;
    public static String superMarketMap;
    // The minimum distance to change Updates in meters
    private static final long MIN_DISTANCE_CHANGE_FOR_UPDATES = 10; // 10 meters

    // The minimum time between updates in milliseconds
    private static final long MIN_TIME_BW_UPDATES = 1000 * 60 * 1; // 1 minute
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.outdoor_map_activity_layout);
        String supermarketName=FinalResponseAdapter.mIntentO.getStringExtra("SuperMarketName");
        String result= ShoppinglistActivity.getResultServer();
        Log.i("RESULT",result);
        Log.i("MAP",supermarketName);
        if (result != null) {
            List<ResponseFromServer> offer = ResponseFromServer.parseJSONResult(result);
            //Toast.makeText(getApplicationContext(), "Details"+result, Toast.LENGTH_LONG).show();
            if (offer.size() != 0) {
                for (int i = 0; i < offer.size(); i++) {
                    if (offer.get(i).getSuperMarket().getName().equals(supermarketName))
                    {
                        superMarketLatitude=offer.get(i).getSuperMarket().getPositionX();
                        Log.i("superMarketLatitude",""+superMarketLatitude);
                        superMarketLongitude=offer.get(i).getSuperMarket().getPositionY();
                        Log.i("superMarketLongitude",""+superMarketLongitude);
                        superMarketMap=offer.get(i).getSuperMarket().getIndoorMapUrl();
                        Log.i("superMarketMap",superMarketMap);
                        Log.d("SuperMarket Map from outdoor Activity",superMarketMap);
                        break;

                    }
                }
            } else {
                createDialog("Error offer", "A problem has occured, sorry");
            }
        }
        else
        {
            createDialog("Error offer", "A problem has occured, sorry");
        }
        // Getting Google Play availability status
        int status = GooglePlayServicesUtil.isGooglePlayServicesAvailable(getBaseContext());

        // Showing status
        if(status!=ConnectionResult.SUCCESS){ // Google Play Services are not available

            int requestCode = 10;
            Dialog dialog = GooglePlayServicesUtil.getErrorDialog(status, this, requestCode);
            dialog.show();

        }else { // Google Play Services are available

            // Getting reference to the SupportMapFragment of activity_main.xml
            SupportMapFragment fm = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);

            // Getting GoogleMap object from the fragment
            googleMap = fm.getMap();

            // Enabling MyLocation Layer of Google Map
            googleMap.setMyLocationEnabled(true);


            // Getting LocationManager object from System Service LOCATION_SERVICE
            Criteria criteria = new Criteria();
            /*criteria.setAccuracy(Criteria.ACCURACY_FINE);
             *  criteria.setAltitudeRequired(true);
             *  criteria.setBearingRequired(true);
             *  criteria.setCostAllowed(false);
             *  criteria.setPowerRequirement(Criteria.POWER_LOW); */
            locationManager =   (LocationManager) getSystemService(Context.LOCATION_SERVICE);
            boolean enabledGPS = locationManager
                    .isProviderEnabled(LocationManager.GPS_PROVIDER);
            boolean enabledNet = locationManager
                    .isProviderEnabled(LocationManager.NETWORK_PROVIDER);
            // check if enabled and if not send user to the GSP settings
            // Better solution would be to display a dialog and suggesting to
            // go to the settings
            if (!enabledGPS && !enabledNet) {
                Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                startActivity(intent);
            }
            else {

                if (enabledNet) {
                    locationManager.requestLocationUpdates(
                            LocationManager.NETWORK_PROVIDER,
                            MIN_TIME_BW_UPDATES,
                            MIN_DISTANCE_CHANGE_FOR_UPDATES, this);
                    if (locationManager != null) {
                        location = locationManager
                                .getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
                        if (location != null) {
                            Toast.makeText(getBaseContext(),"Latitude:"+String.valueOf(location.getLatitude())+"\n"+"Longitude:"+String.valueOf(location.getLongitude()), Toast.LENGTH_LONG).show();
                            MOI=new LatLng(location.getLatitude(),location.getLongitude());
                        }
                        else
                        {
                            Toast.makeText(getBaseContext(), "Location not available", Toast.LENGTH_LONG).show();
                        }
                    }
                }
                // if GPS Enabled get lat/long using GPS Services
                else if (enabledGPS) {
                    if (location == null) {
                        locationManager.requestLocationUpdates(
                                LocationManager.GPS_PROVIDER,
                                MIN_TIME_BW_UPDATES,
                                MIN_DISTANCE_CHANGE_FOR_UPDATES, this);
                        if (locationManager != null) {
                            location = locationManager
                                    .getLastKnownLocation(LocationManager.GPS_PROVIDER);
                            if (location != null) {
                                Toast.makeText(getBaseContext(),"Latitude:"+String.valueOf(location.getLatitude())+"\n"+"Longitude:"+String.valueOf(location.getLongitude()), Toast.LENGTH_LONG).show();
                                MOI=new LatLng(location.getLatitude(),location.getLongitude());
                            }
                            else
                            {
                                Toast.makeText(getBaseContext(), "Location not available", Toast.LENGTH_LONG).show();
                            }
                        }
                    }
                }
                CameraUpdate center=CameraUpdateFactory.newLatLng(MOI);
                CameraUpdate zoomSuperMarket=CameraUpdateFactory.zoomTo(15);
                googleMap.moveCamera(center);
                googleMap.animateCamera(zoomSuperMarket);
                googleMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {

                    @Override
                    public void onMapClick(LatLng point) {
                // Removes the existing marker from the Google Map
                googleMap.clear();
                // Drawing marker on the map
                drawMarker(MOI, "SuperMarket", "Next Step", false);
                // Drawing circle on the map
                drawCircle(MOI);

                // This intent will call the activity ProximityActivity
                proximityIntent = new Intent("com.example.kawtar.myapplication.activity.proximity");
                // Creating a pending intent which will be invoked by LocationManager when the specified region is
                // entered or exited
                pendingIntent = PendingIntent.getActivity(getBaseContext(), 0, proximityIntent, PendingIntent.FLAG_ONE_SHOT);
                // Setting proximity alert
                // The pending intent will be invoked when the device enters or exits the region 20 meters
                // away from the marked point
                // The -1 indicates that, the monitor will not be expired
                locationManager.addProximityAlert(MOI.latitude, MOI.longitude, 20, -1, pendingIntent);
                Toast.makeText(getBaseContext(), "Proximity Alert is added", Toast.LENGTH_SHORT).show();
                    }
                });
                googleMap.setOnMapLongClickListener(new OnMapLongClickListener() {
                    @Override
                    public void onMapLongClick(LatLng point) {
                        proximityIntent = new Intent("com.example.kawtar.myapplication.activity.proximity");
                        pendingIntent = PendingIntent.getActivity(getBaseContext(), 0, proximityIntent, PendingIntent.FLAG_ONE_SHOT);

                        // Removing the proximity alert
                        locationManager.removeProximityAlert(pendingIntent);

                        // Removing the marker and circle from the Google Map
                        googleMap.clear();
                        Toast.makeText(getBaseContext(), "Proximity Alert is removed", Toast.LENGTH_LONG).show();
                    }
                });

            }
        }
    }
    /* Request updates at startup */
    @Override
    protected void onResume() {
        super.onResume();
        if (enabledGPS) {
            locationManager.requestLocationUpdates(
                    LocationManager.GPS_PROVIDER,
                    MIN_TIME_BW_UPDATES,
                    MIN_DISTANCE_CHANGE_FOR_UPDATES, this);
        }
        else if(enabledNet)
        {
            locationManager.requestLocationUpdates(
                    LocationManager.NETWORK_PROVIDER,
                    MIN_TIME_BW_UPDATES,
                    MIN_DISTANCE_CHANGE_FOR_UPDATES, this);
        }
    }

    /* Remove the locationlistener updates when Activity is paused */
    @Override
    protected void onPause() {
        super.onPause();
        locationManager.removeUpdates(this);
    }

    public void onLocationChanged(Location location)
    {
        double lat = (location.getLatitude());
        double lng = (location.getLongitude());
        Toast.makeText(getBaseContext(),"Latitude:"+String.valueOf(location.getLatitude())+"\n"+"Longitude:"+String.valueOf(location.getLongitude()), Toast.LENGTH_LONG).show();
        // Creating a LatLng object for the current location
        LatLng latLng = new LatLng(lat, lng);
        CameraPosition cameraPosition = new CameraPosition.Builder().target(MOUNTAIN_VIEW)
                // Sets the center of the map to Mountain View
                .zoom(17)
                        // Sets the zoom
                .bearing(90)
                        // Sets the orientation of the camera to east
                .tilt(30)
                        // Sets the tilt of the camera to 30 degrees
                .build();
        // Creates a CameraPosition from the builder
        googleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
        // Showing the current location in Google Map
        googleMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
        // Zoom in the Google Map
        //gMap.animateCamera(CameraUpdateFactory.zoomTo(15));
    }

    public void onStatusChanged(String provider, int status, Bundle extras)
    {

    }
    public void onProviderEnabled(String provider)
    {
        Toast.makeText(this, "Enabled new provider " + provider,Toast.LENGTH_SHORT).show();
    }
    public void onProviderDisabled(String provider)
    {
        Toast.makeText(this, "Disabled provider " + provider,Toast.LENGTH_SHORT).show();
    }
    private void drawMarker(LatLng point,String title,String snippet,boolean img){
        // Creating an instance of MarkerOptions
        if(img)
        {
            googleMap.addMarker(new MarkerOptions().position(point).title(title).snippet(snippet).icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_launcher)));
        }
        else
        {
            googleMap.addMarker(new MarkerOptions().position(point).title(title).snippet(snippet));
        }

    }


    private void drawCircle(LatLng point){

        // Instantiating CircleOptions to draw a circle around the marker
        CircleOptions circleOptions = new CircleOptions();

        // Specifying the center of the circle
        circleOptions.center(point);

        // Radius of the circle
        circleOptions.radius(20);

        // Border color of the circle
        circleOptions.strokeColor(Color.BLACK);

        // Fill color of the circle
        circleOptions.fillColor(0x30ff0000);

        // Border width of the circle
        circleOptions.strokeWidth(2);

        // Adding the circle to the GoogleMap
        googleMap.addCircle(circleOptions);

    }

    public  void createDialog(final String title, String text) {
        // Hide keyboard and show it at demand
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        AlertDialog.Builder ad = new AlertDialog.Builder(OutdoorMapActivity.this);
        // Button to create an alert dialog for setting the connection to the server
        ad.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {

                Intent intent=new Intent();
                setResult(RESULT_OK,intent);
                finish();

            }
        });
        // Set the alert dialog title, create it and show it
        ad.setTitle(title + ":" + text);
        AlertDialog alert1 = ad.create();
        alert1.show();
    }

}