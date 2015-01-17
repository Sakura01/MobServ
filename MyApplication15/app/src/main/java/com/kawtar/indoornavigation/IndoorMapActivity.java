package com.kawtar.indoornavigation;

import android.app.Activity;
import android.os.Bundle;
import android.app.FragmentTransaction;
import android.util.Log;
import android.widget.Toast;

import com.kawtar.indoornavigation.ShoppingListFragment.OnShoppingItemSelectedListener;
import com.kawtar.listshopping.ProductToSend;
import com.kawtar.mainUI.SplashActivity;


import static com.example.kawtar.myapplication.R.*;

public class IndoorMapActivity extends Activity implements OnShoppingItemSelectedListener {
    private final String TAG_INDOOR_ACTIVITY="MainActivity";
    boolean indoorMap = false;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.v(TAG_INDOOR_ACTIVITY, "onCreate()");
        Log.v("AndroidFragmentsavedInstanceState", savedInstanceState == null ? "true" : "false");
        setContentView(layout.indoor_activity_layout);
        if(savedInstanceState == null) {
            FragmentTransaction ft = getFragmentManager().beginTransaction();
            ShoppingListFragment listFragment = new ShoppingListFragment();
            ft.add(id.displayList, listFragment, "List_Fragment");
            ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
            ft.commit();
        }

        if(findViewById(id.displayIndoorMap) != null){
            indoorMap = true;
            getFragmentManager().popBackStack();

            MapFragment mapFragment = (MapFragment) getFragmentManager().findFragmentById(id.displayIndoorMap);
            if(mapFragment == null){
                FragmentTransaction ft = getFragmentManager().beginTransaction();
                mapFragment = new MapFragment();
                ft.replace(id.displayIndoorMap, mapFragment, "Detail_Fragment1");
                ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
                ft.commit();
            }
        }

    }

    public void onShoppingItemSelected(ProductToSend item) {
        Log.v(TAG_INDOOR_ACTIVITY,item.getName());
        Toast.makeText(getApplicationContext(), item.getName(), Toast.LENGTH_LONG).show();
        if(indoorMap){
            MapFragment mapFragment = (MapFragment)
                    getFragmentManager().findFragmentById(id.displayIndoorMap);
            mapFragment.drawMarker(item);
        }
        else{
            MapFragment mapFragment = new MapFragment();
            mapFragment.drawMarker(item);
            FragmentTransaction ft = getFragmentManager().beginTransaction();
            ft.replace(id.displayList, mapFragment, "Detail_Fragment2");
            ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
            ft.addToBackStack(null);
            ft.commit();
        }
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