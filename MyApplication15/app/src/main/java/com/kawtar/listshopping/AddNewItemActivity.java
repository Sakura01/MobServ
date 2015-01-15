package com.kawtar.listshopping;

import java.util.ArrayList;
import java.util.List;

import com.example.kawtar.myapplication.R;
import com.kawtar.mainUI.MainActivity;
import com.kawtar.requestmarket.RequestActivity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Toast;

public class AddNewItemActivity extends Activity {

	private ToShopItemListAdapter adapter;
	private ListView itemToShopListView;
	private Intent mIntent;
    private ImageButton mAdd;
    private ImageButton mSubmit;
	private static List<Product> listToSend;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_list_view);
		
		setupListViewAdapter();
        mAdd=(ImageButton)findViewById(R.id.AddItem);
        mAdd.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                adapter.insert(new Product("", "","",1,1,1,1,"",false,false), 0);
            }
        });
        mSubmit=(ImageButton)findViewById(R.id.SubmitList);
        mSubmit.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v)
            {
                if(filterTextList(adapter.getShoppingList())==true)
                {
                    listToSend=adapter.getShoppingList();
                    mIntent = new Intent(AddNewItemActivity.this, RequestActivity.class);
                    startActivity(mIntent);
                }
                else
                {
                    createDialog("Error","Invalid format type for color");
                }


            }
        });
	}

	public void removeToShopItemOnClickHandler(View v) {
		Product itemToRemove = (Product)v.getTag();
		adapter.remove(itemToRemove);
	}

	private void setupListViewAdapter() {
		adapter = new ToShopItemListAdapter(AddNewItemActivity.this, R.layout.shop_item_list, new ArrayList<Product>());
		itemToShopListView = (ListView)findViewById(R.id.ItemsList);
		itemToShopListView.setAdapter(adapter);


	}

    public boolean checkFormat(String s)
    {
        for (int i = 0; i < s.length(); i++)
        {
            if (Character.isLetter(s.charAt(i))) {
                return true;
            }
        }
        return false;
    }
    public boolean filterTextList(List<Product> list)
    {
        boolean valid=false;
        for(int i=0;i<list.size();i++)
        {
            if(checkFormat(list.get(i).getColor()))
            {
                valid=true;
            }
        }
        return valid;
    }
    public void createDialog(final String title, String text) {
        AlertDialog.Builder ad = new AlertDialog.Builder(this);
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


    public static List<Product> getShoppingList()
	{
		return listToSend;
	}
	public static void setListRequest (List<Product> list)
	{
		listToSend=list;
	}

}
