package com.kawtar.requestmarket;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicHeader;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;

import com.example.kawtar.myapplication.R;
import com.kawtar.finalresponse.FinalResponseActivity;
import com.kawtar.jsoncontrol.RequestList;
import com.kawtar.jsoncontrol.ResponseFromServer;
import com.kawtar.listshopping.Product;
import com.kawtar.mainUI.MainActivity;
import com.kawtar.mainUI.SplashActivity;
import com.kawtar.networkcontrol.ReachabilityTest;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;


public class RequestActivity extends Activity {


	private Intent				mIntent;
	private static final int	QUIT						= 0;
	private ImageButton mButtonSubmit;
	private ImageButton  mButtonCancel;
	private String  result; 
	private ShoppingListReceivedAdapter adapter;
	private ListView itemToShopListView;
	private RequestList reqlist;
    private List<ResponseFromServer> off;
	private static List<ResponseFromServer> offer;
	private int cpt=0;
	private static boolean counterState=false;
	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
		setContentView(R.layout.list_check_layout);
		setupListViewAdapter();
		mButtonSubmit = (ImageButton) findViewById(R.id.buttonSubmitList);
        adapter.clear();
        new PostTask().execute();

		mButtonSubmit.setOnClickListener(new View.OnClickListener() {
			public void onClick(View view) {
				new ReachabilityTest(getApplicationContext(), "google.fr", 80, new ReachabilityTest.Callback() {
					public void onReachabilityTestPassed() 
					{

						new PostTask().execute();
					}
					@Override
					public void onReachabilityTestFailed() {
						createDialog("Network", "Please check your connection");
					}
				}).execute();
			}
		});
		mButtonCancel = (ImageButton) findViewById(R.id.buttonCancel);
		mButtonCancel.setOnClickListener(new View.OnClickListener() {
			public void onClick(View view) {
				Intent intent = new Intent();
				setResult(RESULT_OK, intent);
				finish();
			}
		});

	}

	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (requestCode == 1)
		{
			if (resultCode == QUIT)
			{
				mIntent = new Intent(RequestActivity.this, MainActivity.class);
				startActivity(mIntent);
			}
		}
	}
	public void onResume() {
        super.onResume();
    }
    public void onPause() {
        super.onPause();
    }
    @Override  
    protected void onStop() {  
         super.onStop();  
 }   
	public void removeToShopItemOnClickHandler(View v) {
		Product itemToRemove = (Product)v.getTag();
		adapter.remove(itemToRemove);
		List<Product> list=new ArrayList<Product>();
		//list=AddNewItemActivity.getShoppingList();
		//list.remove(itemToRemove);
		//itemToRemove.setAccept(false);
		//if(list.size()==0)
		//{
		//	createDialog("Warning", "Your list is empty dude");
		//}
		//else
		//{
		//	AddNewItemActivity.setListRequest(list);
		//}
	}

	private void setupListViewAdapter() {
		adapter = new ShoppingListReceivedAdapter(RequestActivity.this, R.layout.response_layout, new ArrayList<Product>());
		itemToShopListView = (ListView)findViewById(R.id.ItemsListReceived);
		itemToShopListView.setAdapter(adapter);
		
	}
	public static boolean getCounterState()
	{
		return counterState;
	}
	public void setCounterState(boolean state)
	{
		counterState=state;
	}
	public void onBackPressed() {

		if (SplashActivity.CONDITION)
		{
			// do nothing
		}
		else
			super.onBackPressed();

	}
    public void createDialog(final String title, String text) {
		// Hide keyboard and show it at demand
		getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
		AlertDialog.Builder ad = new AlertDialog.Builder(RequestActivity.this);
		// Button to create an alert dialog for setting the connection to the server
		ad.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int id) {
				if(title.equals("Network"))
				{
					startActivityForResult(new Intent(android.provider.Settings.ACTION_SETTINGS), 0);
				}
				else
				{
                    Intent intent=new Intent();
                    setResult(RESULT_OK,intent);
                    finish();
				}

			}
		});
		// Set the alert dialog title, create it and show it
		ad.setTitle(title + ":" + text);
		AlertDialog alert1 = ad.create();
		alert1.show();
	}
    public static  List<ResponseFromServer> getOfferFromServer()
    {
    	return  offer;
    }
    public void setOfferFromServer(List<ResponseFromServer> offer)
    {
    	this.offer=offer;
    }
	private class PostTask extends AsyncTask<Void, Void, Void> {
    	private ProgressDialog	pDialog;
    	private int		reqStatus;
    	private String URL="http://smartshoppingproject.appspot.com/request";
    	protected void onPreExecute() {
			/**
			 * Set a process dialog for showing the uploading's progress
			 */
			pDialog = new ProgressDialog(RequestActivity.this);
			pDialog.setMessage("Sending request to the server...");
			pDialog.show();
			super.onPreExecute();
		}

		@Override
		protected Void doInBackground(Void... params) {

			reqStatus = requestList();

			runOnUiThread(new Runnable() {
				@Override
				public void run() {

					if (reqStatus==HttpStatus.SC_OK) {
                        Log.i("What the server sends back:", result);

                        if (result != null)
                        {
                            if (getCounterState()) {
                                List<Product>listToRequest=new ArrayList<Product>();
                                off=ResponseFromServer.parseJSONResult(result);
                                if ( off!= null) {
                                    for(int j=0;j<off.size();j++)
                                    {
                                        for(int i=0;i<off.get(j).getList().size();i++)
                                        {
                                            //Log.i("Reçue",off.get(j).getList().get(i).getBrand()+"|"+off.get(j).getList().get(i).getName());
                                        }
                                    }
                                    setOfferFromServer(off);
                                    if (getOfferFromServer().size() != 0) {
                                        adapter.clear();
                                        for (int i = 0; i < getOfferFromServer().size(); i++) {
                                            for (int j = 0; j < getOfferFromServer().get(i).getList().size(); j++) {
                                                //Log.i("List offers",getOfferFromServer().get(i).getList().get(j).getBrand()+"|"+getOfferFromServer().get(i).getList().get(j).getName());
                                                adapter.insert(getOfferFromServer().get(i).getList().get(j), 0);
                                                listToRequest.add(getOfferFromServer().get(i).getList().get(j));
                                            }

                                        }
                                       // AddNewItemActivity.setListRequest(listToRequest);
                                    }
                                }
                                else {
                                    createDialog("Warning", "There is no offer, sorry dude");
                                }

                            }
                            else {
                                List<Product> li = new ArrayList<Product>();
                                if (ResponseFromServer.parseJSONResultFalse(result) != null) {
                                    li = ResponseFromServer.parseJSONResultFalse(result);
                                    if (li.size() != 0) {
                                        adapter.clear();
                                        for (int i = 0; i < li.size(); i++) {
                                            Log.i("List received not approuved yet", "" + li.get(i).getName());
                                            adapter.insert(li.get(i), 0);
                                        }
                                        //AddNewItemActivity.setListRequest(li);
                                    }
                                } else {
                                    createDialog("Warning", "There is no offer, sorry dude");
                                }
                            }

                        }
                        else {
                            createDialog("Warning", "There is no offer, sorry dude");
                        }
					}
					else
					{
						// Show the alert dialog
						createDialog("Request", "An error has occured,please try again" + reqStatus);
					}
				}
			});
			return null;
		}

		@Override

		protected void onPostExecute(Void res) {

			if (null != pDialog && pDialog.isShowing())
			{
				pDialog.dismiss();
				if(reqlist.getAcceptFlag())
				{
					mIntent=new Intent(RequestActivity.this,FinalResponseActivity.class);
					startActivity(mIntent);
				}
			}
			super.onPostExecute(res);
		}
	public int requestList()
	{
		int code=0;
		DefaultHttpClient httpClient = new DefaultHttpClient();
		HttpConnectionParams.setConnectionTimeout(httpClient.getParams(), 10000); //Timeout Limit
        HttpPost request = new HttpPost(URL);
        HttpResponse response;
        List<Product> list=new ArrayList<Product>();
        //list=AddNewItemActivity.getShoppingList();
        
        if(list.size()==0)
        {
        	createDialog("Warning", "Your list is empty dude");
        }
        else
        {
        	for(int i=0;i<list.size();i++)
        	{
        		//if(list.get(i).getAccept()==true)
	        	{
	        		cpt++;
	        	}
        	}
    		if(cpt==list.size())
    		{
    			reqlist=new RequestList(list,MainActivity.getDistanceRange(), MainActivity.getBudget(),MainActivity.getUserLatitude(),MainActivity.getUserLongitude(),true);
    			setCounterState(true);
    		}
    		else
        	{
        		reqlist=new RequestList(list,MainActivity.getDistanceRange(), MainActivity.getBudget(),MainActivity.getUserLatitude(),MainActivity.getUserLongitude(),false);
        		setCounterState(false);
        		cpt=0;
        	}
        	
        }
  try {

      StringEntity se = new StringEntity(reqlist.toJSON());  
      Log.i("list in async task to send",""+reqlist.toJSON().toString());
      se.setContentType(new BasicHeader(HTTP.CONTENT_TYPE, "application/json"));
      request.setEntity(se);
      response = httpClient.execute(request);
      code=response.getStatusLine().getStatusCode();
      result = EntityUtils.toString(response.getEntity());
  } catch (ClientProtocolException e) {
   // TODO Auto-generated catch block
   e.printStackTrace();
  } catch (IOException e) {
   // TODO Auto-generated catch block
   e.printStackTrace();
  }
return code;
	}

}

	

}
