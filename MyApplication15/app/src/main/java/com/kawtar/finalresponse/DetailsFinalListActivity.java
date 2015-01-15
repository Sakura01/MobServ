package com.kawtar.finalresponse;

import java.util.ArrayList;
import java.util.List;

import com.example.kawtar.myapplication.R;
import com.kawtar.jsoncontrol.ResponseFromServer;
import com.kawtar.listshopping.AddNewItemActivity;
import com.kawtar.listshopping.Product;
import com.kawtar.mainUI.MainActivity;
import com.kawtar.mainUI.SplashActivity;
import com.kawtar.requestmarket.RequestActivity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;


public class DetailsFinalListActivity extends Activity{
	
	private DetailsFinalListAdapter adapter;
	private ListView itemOfferListView;
	private Button backButton;
	private List<Product> list;
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.details_list_activity);
		setupListViewAdapter();
        list=new ArrayList<Product>();
	}
	private void setupListViewAdapter() {
		adapter = new DetailsFinalListAdapter(DetailsFinalListActivity.this, R.layout.details_list_layout, new ArrayList<Product>());
		itemOfferListView = (ListView)findViewById(R.id.ItemsDetailedList);
		itemOfferListView.setAdapter(adapter);
        String supermarketName=FinalResponseAdapter.mIntentD.getStringExtra("SuperMarket");
        List<ResponseFromServer> offer= RequestActivity.getOfferFromServer();
        if(offer.size()!=0)
        {
            for(int i=0;i<offer.size();i++)
            {
                if(offer.get(i).getSuperMarket().getName().equals(supermarketName))
                {
                    String superMarket=offer.get(i).getSuperMarket().getName();
                    Toast.makeText(getApplicationContext(),"Name:"+ superMarket, Toast.LENGTH_LONG).show();
                    list=offer.get(i).getList();
                }
            }
            for(int j=0;j<list.size();j++)
            {
                Product product = list.get(j);
                adapter.insert(product, 0);
            }
        }
        else
        {
            createDialog("Error offer", "A problem has occured, sorry");
        }


	}
	public  void createDialog(final String title, String text) {
		// Hide keyboard and show it at demand
		getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
		AlertDialog.Builder ad = new AlertDialog.Builder(DetailsFinalListActivity.this);
		// Button to create an alert dialog for setting the connection to the server
		ad.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int id) {

					Intent activity=new Intent(DetailsFinalListActivity.this, MainActivity.class);
					startActivity(activity);
				
			}
		});
		// Set the alert dialog title, create it and show it
		ad.setTitle(title + ":" + text);
		AlertDialog alert1 = ad.create();
		alert1.show();
	}

}
