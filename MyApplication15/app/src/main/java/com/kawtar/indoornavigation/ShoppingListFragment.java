package com.kawtar.indoornavigation;
/**
 * Created by Kawtar on 11/27/2014.
 */
import java.util.ArrayList;
import java.util.List;


import android.app.Activity;
import android.app.AlertDialog;
import android.app.Fragment;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Toast;

import com.example.kawtar.myapplication.OutdoorMapActivity;
import com.example.kawtar.myapplication.R;
import com.kawtar.finalresponse.FinalResponseAdapter;
import com.kawtar.jsoncontrol.ResponseFromServer;
import com.kawtar.listshopping.Product;
import com.kawtar.requestmarket.RequestActivity;

public class ShoppingListFragment extends Fragment {

    private  OnShoppingItemSelectedListener mListener;
    private final String TAG_SHOPPING_LIST_FRAGMENT="ShoppingListFragment";
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.v(TAG_SHOPPING_LIST_FRAGMENT, "onCreate()");
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Log.v(TAG_SHOPPING_LIST_FRAGMENT, "onActivityCreated().");
        Log.v("ListsavedInstanceState", savedInstanceState == null ? "true" : "false");

        //Generate list View from ArrayList
        displayListView();

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Log.v(TAG_SHOPPING_LIST_FRAGMENT, "onCreateView()");
        Log.v("ListContainer", container == null ? "true" : "false");
        Log.v("ListsavedInstanceState", savedInstanceState == null ? "true" : "false");
        if (container == null) {
            return null;
        }
        View view = inflater.inflate(R.layout.list_shopping_indoor_view, container, false);
        return view;
    }


    // Container Activity must implement this interface
    public interface OnShoppingItemSelectedListener {
        public void onShoppingItemSelected(Product mSelectedFromList);
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mListener = (OnShoppingItemSelectedListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString() + " must implement OnShoppingItemSelectedListener");
        }
    }

    private void displayListView() {
        String supermarketName=OutdoorMapActivity.superMarketMap;
        List<ResponseFromServer> offer= RequestActivity.getOfferFromServer();
            List<Product> list=new ArrayList<Product>();
            if(offer.size()!=0)
            {
                for(int i=0;i<offer.size();i++)
                {
                    if(offer.get(i).getSuperMarket().getIndoorMapUrl().equals(supermarketName))
                    {
                        list=offer.get(i).getList();
                    }
                }
                //create an ArrayAdaptar from the String Array
                ArrayAdapter<Product> dataAdapter = new ArrayAdapter<Product>(getActivity(),
                        R.layout.shopping_list_fragment, list);
                final ListView listView = (ListView) getView().findViewById(R.id.listofShoppingItems);
                // Assign adapter to ListView
                listView.setAdapter(dataAdapter);
                listView.setOnItemClickListener(new OnItemClickListener() {
                    public void onItemClick(AdapterView<?> parent, View view,
                                            int position, long id) {
                        // Send the URL to the host activity
                        Product mSelectedFromList = (Product) (listView.getItemAtPosition(position));
                        mSelectedFromList.setMarker(true);
                        mListener.onShoppingItemSelected(mSelectedFromList);
                    }
                });
            }
            else
            {
                createDialog("Error offer", "A problem has occured, sorry");
            }



    }
    public  void createDialog(final String title, String text) {
        AlertDialog.Builder ad = new AlertDialog.Builder(getActivity());
        // Button to create an alert dialog for setting the connection to the server
        ad.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {

                Intent activity=new Intent(getActivity(), RequestActivity.class);
                startActivity(activity);

            }
        });
        // Set the alert dialog title, create it and show it
        ad.setTitle(title + ":" + text);
        AlertDialog alert1 = ad.create();
        alert1.show();
    }
}