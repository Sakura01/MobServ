package com.kawtar.finalresponse;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.WindowManager;
import android.widget.ListView;
import android.widget.Toast;

import com.example.kawtar.myapplication.R;
import com.kawtar.jsoncontrol.ResponseFromServer;
import com.kawtar.mainUI.MainActivity;
import com.kawtar.mainUI.SplashActivity;
import com.shopping.list.ShoppinglistActivity;

public class FinalResponseActivity extends Activity {

	private FinalResponseAdapter adapter;
	private ListView itemOfferListView;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.final_response_activity_layout);
		
		setupListViewAdapter();
	}
	private void setupListViewAdapter() {
        adapter = new FinalResponseAdapter(FinalResponseActivity.this, R.layout.final_response_layout, new ArrayList<ResponseFromServer>());
        itemOfferListView = (ListView) findViewById(R.id.OffersList);
        itemOfferListView.setAdapter(adapter);
        String result=ShoppinglistActivity.getResultServer();
        if (result != null)
        {
            List<ResponseFromServer> offer =ResponseFromServer.parseJSONResult(result);
            Toast.makeText(getApplicationContext(), "Final" + result, Toast.LENGTH_LONG).show();
            if (offer.size() != 0) {
                for (int i = 0; i < offer.size(); i++) {
                    adapter.insert(offer.get(i), 0);
                }
            } else {
                createDialog("Error offer", "A problem has occured, sorry");
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
		AlertDialog.Builder ad = new AlertDialog.Builder(FinalResponseActivity.this);
		// Button to create an alert dialog for setting the connection to the server
		ad.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int id) {

					Intent activity=new Intent(FinalResponseActivity.this, MainActivity.class);
					startActivity(activity);
				
			}
		});
		// Set the alert dialog title, create it and show it
		ad.setTitle(title + ":" + text);
		AlertDialog alert1 = ad.create();
		alert1.show();
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
