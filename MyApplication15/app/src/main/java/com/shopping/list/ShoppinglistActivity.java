package com.shopping.list;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import com.example.kawtar.myapplication.R;
import com.kawtar.finalresponse.FinalResponseActivity;
import com.kawtar.jsoncontrol.RequestList;
import com.kawtar.listshopping.ProductToSend;
import com.kawtar.mainUI.MainActivity;
import com.shopping.list.adapter.ShoppinglistProductMappingAdapter;
import com.shopping.list.adapter.StoreAdapter;

import com.shopping.list.bean.ShoppinglistProductMapping;
import com.shopping.list.bean.Store;
import com.shopping.list.constant.ConfigurationConstants;
import com.shopping.list.constant.DBConstants;
import com.shopping.list.constant.GlobalValues;
import com.shopping.list.datasource.ShoppinglistDataSource;
import com.shopping.list.helper.ProcessColorHelper;

import android.app.AlertDialog;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.content.SharedPreferences;

import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.PopupMenu;
import android.widget.PopupMenu.OnMenuItemClickListener;
import android.widget.TextView;

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


public class ShoppinglistActivity extends AbstractShoppinglistActivity {

	private Button buttonAddToHistoryAlphabeticallyView;

	private Button buttonAddToHistoryStoreView;
	
	private Button buttonSubmitList;

	private Context context;

	private ShoppinglistDataSource datasource;

	private TextView labelProcessAlphabetically;

	private ListView listAlphabetically;

	private ListView listStore;

	private ShoppinglistProductMappingAdapter shoppinglistMappingAdapter;

	private List<ShoppinglistProductMapping> shoppinglistProductMappingsToShow;

	private StoreAdapter storeListAdapter;

	private List<Store> storesToShowInOverview;

	private int viewType;

    //Com with server
    private String  result;

    private RequestList reqlist;

    //Launch intent for results

    private Intent mIntent;

    private static final int	QUIT= 0;

    private static String response;

	/**
	 * because this activity is the "Home" of the app, but we have two different
	 * viewTypes, here are the actions to perform when the viewtype =
	 * alphabetically
	 */
	public void actionsToPerformInAlphabeticallyViewType() {
		// get the mappings (storeId = -1 because there's no store specified)
		this.shoppinglistProductMappingsToShow = this.datasource.getProductsOnShoppingList(-1);

		this.shoppinglistMappingAdapter = new ShoppinglistProductMappingAdapter(this,
				this.shoppinglistProductMappingsToShow);

		// show the process
		this.setProcessTextInAlphabeticallyView();

		// show historybutton?
		this.setVisibilityOfHistoryButton();
		this.listAlphabetically = (ListView) this
				.findViewById(R.id.listShoppinglistProductMappingsAlphabetically);
		this.listAlphabetically.setAdapter(this.shoppinglistMappingAdapter);
		//handle clicks on send list to server
			this.buttonSubmitList = (Button) this
					.findViewById(R.id.buttonSubmitList);
        this.buttonSubmitList.setOnClickListener(new View.OnClickListener() {
            public void onClick(final View v) {
                new PostTask().execute();
            }
        });
		// handle clicks on addToHistory button
		this.buttonAddToHistoryAlphabeticallyView = (Button) this
				.findViewById(R.id.buttonAddToHistoryAlphabetOverview);
		this.buttonAddToHistoryAlphabeticallyView.setOnClickListener(new View.OnClickListener() {

			public void onClick(final View v) {
				final AlertDialog.Builder alertBox = new AlertDialog.Builder(
						ShoppinglistActivity.this.context);
				alertBox.setMessage(ShoppinglistActivity.this
						.getString(R.string.msg_really_add_shoppinglist_to_history));
				alertBox.setPositiveButton(ShoppinglistActivity.this.getString(R.string.msg_yes),
						new OnClickListener() {

							public void onClick(final DialogInterface dialog, final int which) {
								ShoppinglistActivity.this.datasource.addAllToHistory();
								ShoppinglistActivity.this.datasource
										.deleteAllShoppinglistProductMappings();
								ShoppinglistActivity.this.datasource.createNewShoppinglist();
								ShoppinglistActivity.this.refreshLayout();
							}
						});

				alertBox.setNegativeButton(ShoppinglistActivity.this.getString(R.string.msg_no),
						new OnClickListener() {

							public void onClick(final DialogInterface dialog, final int which) {
								// do nothing here
							}
						});

				alertBox.show();
			}

		});

		// handle long clicks on the list items
		this.listAlphabetically.setOnItemLongClickListener(new OnItemLongClickListener() {

			public boolean onItemLongClick(final AdapterView<?> arg0, final View v,
					final int position, final long id) {
				final PopupMenu popup = new PopupMenu(ShoppinglistActivity.this.context, v);
				final MenuInflater inflater = popup.getMenuInflater();
				inflater.inflate(R.menu.popupmenu_products_overview, popup.getMenu());
				popup.show();

				// handle clicks on the popup-buttons
				popup.setOnMenuItemClickListener(new OnMenuItemClickListener() {

					public boolean onMenuItemClick(final MenuItem item) {
						ShoppinglistProductMapping shoppinglistProductMapping = ShoppinglistActivity.this.shoppinglistMappingAdapter
								.getItem(position);

						switch (item.getItemId()) {

						// buttonEditProduct - Popup (longClick)
						case R.id.popupEditProduct:

							// switch to the addProductActivity
							final Intent intent = new Intent(ShoppinglistActivity.this.context,
									EditProductActivity.class);

							// put the values of the mapping in the
							// intent, so they can used by the other
							// activity
							intent.putExtra(DBConstants.COL_SHOPPINGLIST_PRODUCT_MAPPING_ID,
									shoppinglistProductMapping.getId());
							intent.putExtra(DBConstants.COL_SHOPPINGLIST_PRODUCT_MAPPING_QUANTITY,
									shoppinglistProductMapping.getQuantity());
							intent.putExtra(DBConstants.COL_UNIT_ID, shoppinglistProductMapping
									.getProduct().getUnit().getId());
							intent.putExtra(DBConstants.COL_PRODUCT_NAME,
									shoppinglistProductMapping.getProduct().getName());
							intent.putExtra(DBConstants.COL_STORE_ID, shoppinglistProductMapping
									.getStore().getId());

							ShoppinglistActivity.this.startActivityForResult(intent, 0);

							// show historybutton?
							ShoppinglistActivity.this.setVisibilityOfHistoryButton();

							return true;

							// buttonDeleteProduct - Popup (longClick)
						case R.id.popupDeleteProduct:
							// delete from mapping
							shoppinglistProductMapping = ShoppinglistActivity.this.shoppinglistMappingAdapter
									.getItem(position);
							ShoppinglistActivity.this.datasource
									.deleteShoppinglistProductMapping(shoppinglistProductMapping
											.getId());
							ShoppinglistActivity.this.shoppinglistMappingAdapter
									.remove(shoppinglistProductMapping);

							// update the process
							ShoppinglistActivity.this.setProcessTextInAlphabeticallyView();

							// show historybutton?
							ShoppinglistActivity.this.setVisibilityOfHistoryButton();

							return true;

						default:
							return false;
						}
					}
				});

				return false;
			}

		});

		// handle "normal" clicks on the list items
		this.listAlphabetically.setOnItemClickListener(new OnItemClickListener() {

			public void onItemClick(final AdapterView<?> arg0, final View v, final int position,
					final long id) {

				final ShoppinglistProductMapping clickedMapping = ShoppinglistActivity.this.shoppinglistMappingAdapter
						.getItem(position);

				if (clickedMapping.isChecked() == GlobalValues.NO) {

					ShoppinglistActivity.this.shoppinglistProductMappingsToShow.get(
							ShoppinglistActivity.this.shoppinglistProductMappingsToShow
									.indexOf(clickedMapping)).setChecked(GlobalValues.YES);
					ShoppinglistActivity.this.datasource.markShoppinglistProductMappingAsChecked(clickedMapping.getId());

				} else if (clickedMapping.isChecked() == GlobalValues.YES) {

					ShoppinglistActivity.this.shoppinglistProductMappingsToShow.get(
							ShoppinglistActivity.this.shoppinglistProductMappingsToShow
									.indexOf(clickedMapping)).setChecked(GlobalValues.NO);
					ShoppinglistActivity.this.datasource
							.markShoppinglistProductMappingAsUnchecked(clickedMapping.getId());
				}

				ShoppinglistActivity.this.shoppinglistMappingAdapter.notifyDataSetChanged();

				// update the process
				ShoppinglistActivity.this.setProcessTextInAlphabeticallyView();

				// show historybutton?
				ShoppinglistActivity.this.setVisibilityOfHistoryButton();
			}

		});
	}

	/**
	 * because this activity is the "Home" of the app, but we have two different
	 * viewTypes, here are the actions to perform when the viewtype = store
	 */
	public void actionsToPerformInStoreViewType() {

		// show the stores in the view
		this.storesToShowInOverview = this.datasource.getStoresForOverview();

		this.storeListAdapter = new StoreAdapter(this, this.storesToShowInOverview);

		this.listStore = (ListView) this.findViewById(R.id.listViewStore);
		this.listStore.setAdapter(this.storeListAdapter);
		// show historybutton?
		this.setVisibilityOfHistoryButton();

		// handle clicks on addToHistory button
		this.buttonAddToHistoryStoreView = (Button) this
				.findViewById(R.id.buttonAddToHistoryStoreOverview);
		//handle clicks on send list to server
		this.buttonSubmitList = (Button) this
				.findViewById(R.id.buttonSubmitList);
        this.buttonSubmitList.setOnClickListener(new View.OnClickListener() {
            public void onClick(final View v) {
                new PostTask().execute();
            }
        });

		this.buttonAddToHistoryStoreView.setOnClickListener(new View.OnClickListener() {

			public void onClick(final View v) {
				final AlertDialog.Builder alertBox = new AlertDialog.Builder(
						ShoppinglistActivity.this.context);
				alertBox.setMessage(ShoppinglistActivity.this
						.getString(R.string.msg_really_add_shoppinglist_to_history));
				alertBox.setPositiveButton(ShoppinglistActivity.this.getString(R.string.msg_yes),
						new OnClickListener() {

							public void onClick(final DialogInterface dialog, final int which) {
								ShoppinglistActivity.this.datasource.addAllToHistory();
								ShoppinglistActivity.this.datasource
										.deleteAllShoppinglistProductMappings();
								ShoppinglistActivity.this.datasource.createNewShoppinglist();
								ShoppinglistActivity.this.refreshLayout();
							}
						});

				alertBox.setNegativeButton(ShoppinglistActivity.this.getString(R.string.msg_no),
						new OnClickListener() {

							public void onClick(final DialogInterface dialog, final int which) {
								// do nothing here
							}
						});

				alertBox.show();
			}

		});

		// handle long clicks on the list items
		this.listStore.setOnItemLongClickListener(new OnItemLongClickListener() {

			public boolean onItemLongClick(final AdapterView<?> arg0, final View v,
					final int position, final long id) {

				// show popup menu
				final PopupMenu popup = new PopupMenu(ShoppinglistActivity.this.context, v);
				final MenuInflater inflater = popup.getMenuInflater();
				inflater.inflate(R.menu.popupmenu_store_overview, popup.getMenu());
				popup.show();

				// handle clicks on the popup-buttons
				popup.setOnMenuItemClickListener(new OnMenuItemClickListener() {

					public boolean onMenuItemClick(final MenuItem item) {

						switch (item.getItemId()) {
						case R.id.popupDeleteStoreEntries:
							// delete from mapping
							final Store storeToDeleteProductsFrom = ShoppinglistActivity.this.storeListAdapter
									.getItem(position);
							ShoppinglistActivity.this.datasource
									.deleteProductsFromStoreList(storeToDeleteProductsFrom.getId());
							ShoppinglistActivity.this.storeListAdapter
									.remove(storeToDeleteProductsFrom);

							return true;
						default:
							return false;
						}
					}

				});

				return false;
			}

		});

		// handle "normal" clicks on the list items
		this.listStore.setOnItemClickListener(new OnItemClickListener() {

			public void onItemClick(final AdapterView<?> arg0, final View v, final int position,
					final long id) {

				final Store clickedStore = ShoppinglistActivity.this.storeListAdapter
						.getItem(position);

				// call another Activity to show the products of the clicked
				// store
				final Intent intent = new Intent(v.getContext(), StoreProductsActivity.class);
				intent.putExtra(DBConstants.COL_STORE_ID, clickedStore.getId());
				intent.putExtra(DBConstants.COL_STORE_NAME, clickedStore.getName());
				ShoppinglistActivity.this.startActivityForResult(intent, 0);
			}

		});
	}

	/** Called when the activity is first created. */
	@Override
	public void onCreate(final Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.context = super.getContext();
		this.datasource = super.getDatasource();
		this.refreshLayout();
	}

	@Override
	public boolean onCreateOptionsMenu(final Menu menu) {
		final MenuInflater inflater = this.getMenuInflater();
		inflater.inflate(R.menu.actionbar_menu, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(final MenuItem item) {
		switch (item.getItemId()) {

		// AddProductbutton - Actionbar
		case R.id.actionbarAddProduct:
			// switch to the addProductActivity
			final Intent intent = new Intent(this, AddProductActivity.class);
			this.startActivityForResult(intent, 0);
			break;

		// ManageStoresButton - Actionbar
		case R.id.actionbarManageStores:
			final Intent intentManageStores = new Intent(this, ManageStoresActivity.class);
			this.startActivityForResult(intentManageStores, 0);
			break;

		// ManageUnitsButton - Actionbar
		case R.id.actionbarManageUnits:
			final Intent intentManageUnits = new Intent(this, ManageUnitsActivity.class);
			this.startActivityForResult(intentManageUnits, 0);
			break;

		// ManageFavoritesButton - Actionbar
		case R.id.actionbarManageFavorites:
			final Intent intentManageFavorites = new Intent(this, ManageFavoritesActivity.class);
			this.startActivityForResult(intentManageFavorites, 0);
			break;

		// ViewHistory - Actionbar
		case R.id.actionbarShowHistory:
			// switch to the UserConfigurationActivity
			final Intent intentHistoryOverview = new Intent(this, ShowHistoryOverviewActivity.class);
			this.startActivityForResult(intentHistoryOverview, 0);
			break;

		// ShareList - Actionbar
		//case R.id.actionbarShareList:
		//	List<Store> stores = ShoppinglistActivity.this.datasource
		//			.getStoresForOverview();
		//	String text = "";

		//	for (int i = 0; i < stores.size(); i++) {

		//		text = text + getString(R.string.export_at_store)
		//				+ " " + stores.get(i).getName() + ":\n";
		//		List<ShoppinglistProductMapping> shoppinglistProductMappingsToSend = ShoppinglistActivity.this.datasource
		//				.getProductsOnShoppingList(stores.get(i).getId());

		//		for (final ShoppinglistProductMapping mapping : shoppinglistProductMappingsToSend) {
		//			if (mapping.isChecked() == GlobalValues.NO) {
		//				text = text + "- " + mapping.toString() + "\n";
          //              Log.i("DATA",text);
			//		}
			//	}
			//}
			//Intent sendIntent = new Intent();
			//sendIntent.setAction(Intent.ACTION_SEND);
			//sendIntent.putExtra(Intent.EXTRA_TEXT, text);
			//sendIntent.setType("text/plain");
			//startActivity(sendIntent);
			//break;
			
		// deleteShoppinglistMappingsButton - Actionbar
		case R.id.actionbarDeleteShoppinglist:
			final AlertDialog.Builder alertBox = new AlertDialog.Builder(this);
			alertBox.setMessage(this.getString(R.string.msg_really_delete_shoppinglist));
			alertBox.setPositiveButton(this.getString(R.string.msg_yes), new OnClickListener() {

				public void onClick(final DialogInterface dialog, final int which) {
					ShoppinglistActivity.this.datasource.deleteAllShoppinglistProductMappings();
					ShoppinglistActivity.this.datasource.createNewShoppinglist();
					ShoppinglistActivity.this.refreshLayout();
				}
			});

			alertBox.setNegativeButton(this.getString(R.string.msg_no), new OnClickListener() {

				public void onClick(final DialogInterface dialog, final int which) {
					// do nothing here
				}
			});

			alertBox.show();

			break;

		// OptionsMenu - Actionbar
		//case R.id.actionbarOptions:
			// switch to the UserConfigurationActivity
			//final Intent intentUserConfiguration = new Intent(this, UserConfigurationActivity.class);
			//this.startActivityForResult(intentUserConfiguration, 0);
			//break;

		default:
			break;
		}

		return false;
	}

	@Override
	public void onResume() {
		super.onResume();
		this.refreshLayout();
	}

	/**
	 * refreshes the whole layout (incl. data)
	 */
	private void refreshLayout() {
		this.setViewType();
        //sort Alpha
		if (this.viewType == ConfigurationConstants.ALPHABETICALLY_VIEW) {

			this.setContentView(R.layout.overview_alphabet);
			this.actionsToPerformInAlphabeticallyViewType();

			// update the process
			this.setProcessTextInAlphabeticallyView();

		}
        //sort by Markets
  //      else if (this.viewType == ConfigurationConstants.STORE_VIEW) {

//			this.setContentView(R.layout.overview_store);
//			this.actionsToPerformInStoreViewType();

		//}
	}

	/**
	 * sets the textView with the actual process in alphabetically View
	 */
	private void setProcessTextInAlphabeticallyView() {
		// update the title with the actual status
		final int allMappingsCount = this.shoppinglistProductMappingsToShow.size();
		int checkedMappingsCount = 0;

		for (final ShoppinglistProductMapping mapping : this.shoppinglistProductMappingsToShow) {
			if (mapping.isChecked() == GlobalValues.YES) {
				checkedMappingsCount++;
			}
		}

		final int colorToShow = ProcessColorHelper.getColorForProcess(checkedMappingsCount,
				allMappingsCount);
        //List not in details
		this.labelProcessAlphabetically = (TextView) this
				.findViewById(R.id.labelAlphabeticallyOverviewStatus);
		this.labelProcessAlphabetically.setText("( " + checkedMappingsCount + " / "
				+ allMappingsCount + " )");
		this.labelProcessAlphabetically.setTextColor(colorToShow);

	}

	/**
	 * sets the viewType
	 */
	private void setViewType() {
		SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(this);
		//int listTypePref = Integer.parseInt(sharedPref.getString(UserConfigurationActivity.KEY_PREF_LIST_TYPE, UserConfigurationActivity.KEY_PREF_LIST_TYPE_DEFAULT));
		//this.viewType = listTypePref;
        this.viewType =ConfigurationConstants.ALPHABETICALLY_VIEW;
	}

	/**
	 * sets the visibility of buttonAddToHistory TRUE when all items are checked
	 */
	private void setVisibilityOfHistoryButton() {
		// update the title with the actual status
		int allMappingsCount = 0;
		int checkedMappingsCount = 0;
		this.setViewType();
        this.buttonSubmitList= (Button) this
                .findViewById(R.id.buttonSubmitList);
		if (this.viewType == ConfigurationConstants.STORE_VIEW) {
			for (final Store store : this.storesToShowInOverview) {
				allMappingsCount = allMappingsCount + store.getCountProducts();
				checkedMappingsCount = checkedMappingsCount + store.getAlreadyCheckedProducts();
			}

			this.buttonAddToHistoryStoreView = (Button) this
					.findViewById(R.id.buttonAddToHistoryStoreOverview);


			if ((allMappingsCount > 0) && (checkedMappingsCount == allMappingsCount)) 
			{
				this.buttonAddToHistoryStoreView.setVisibility(View.VISIBLE);
				this.buttonSubmitList.setVisibility(View.VISIBLE);

			} 
			else {
				this.buttonAddToHistoryStoreView.setVisibility(View.INVISIBLE);
				this.buttonSubmitList.setVisibility(View.INVISIBLE);
			}

		} else if (this.viewType == ConfigurationConstants.ALPHABETICALLY_VIEW) {
			allMappingsCount = this.shoppinglistProductMappingsToShow.size();
			for (final ShoppinglistProductMapping mapping : this.shoppinglistProductMappingsToShow) {
				if (mapping.isChecked() == GlobalValues.YES) {
					checkedMappingsCount++;
				}
			}

			this.buttonAddToHistoryAlphabeticallyView = (Button) this
					.findViewById(R.id.buttonAddToHistoryAlphabetOverview);

			if ((allMappingsCount > 0) && (checkedMappingsCount == allMappingsCount)) {
				this.buttonAddToHistoryAlphabeticallyView.setVisibility(View.VISIBLE);
				this.buttonSubmitList.setVisibility(View.VISIBLE);
			} 
			else 
			{
				this.buttonAddToHistoryAlphabeticallyView.setVisibility(View.INVISIBLE);
				this.buttonSubmitList.setVisibility(View.INVISIBLE);
			}
		}

	}
    public void createDialog(final String title, String text) {
        // Hide keyboard and show it at demand
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        AlertDialog.Builder ad = new AlertDialog.Builder(ShoppinglistActivity.this);
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
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1)
        {
            if (resultCode == QUIT)
            {
                mIntent = new Intent(ShoppinglistActivity.this, MainActivity.class);
                startActivity(mIntent);
            }
        }
    }
    private void setResultServer(String res)
    {
        response=res;
    }
    public static String getResultServer()
    {
        return response;
    }
    private class PostTask extends AsyncTask<Void, Void, Void> {
        private ProgressDialog pDialog;
        private int		reqStatus;
        private String URL="http://smartshoppingproject.appspot.com/request";
        protected void onPreExecute() {
            /**
             * Set a process dialog for showing the uploading's progress
             */
            pDialog = new ProgressDialog(ShoppinglistActivity.this);
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

                    if (reqStatus== HttpStatus.SC_OK) {
                        Log.i("What the server sends back:", result);

                        if (result!= null)
                        {
                             Log.i("TAG",result);
                             setResultServer(result);
                        }
                        else
                        {
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
                mIntent=new Intent(ShoppinglistActivity.this,FinalResponseActivity.class);
                startActivity(mIntent);
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
            //List<ProductToSend> list=new ArrayList<ProductToSend>();
            //ProductToSend product=new ProductToSend("milk",1,0,0,0,false);
            //list.add(product);
            List<String>li=new ArrayList<String>();
            List<ProductToSend>listToSubmit=new ArrayList<ProductToSend>();
            for (final ShoppinglistProductMapping mapping : shoppinglistProductMappingsToShow) {
                if (mapping.isChecked() == GlobalValues.YES) {
                    Log.i("DATA", mapping.toString());
                    li.add(mapping.toString());
                }
            }
            for(int i=0;i<li.size();i++)
            {
                String p=li.get(i).toString();
                Log.i("DATA_LIST",p);
                String[] splited = p.split("\\s+");
                //Log.i("Name",splited[2]);
                //Log.i("Unit",splited[1]);
                //Log.i("Quanti",splited[0]);
                ProductToSend productSend=new ProductToSend(splited[2],splited[2],Integer.parseInt(splited[0]),splited[1],0,0,0,false);
                listToSubmit.add(productSend);
            }
            //ProductToSend productSend=new ProductToSend("milk",1,0,0,0,false);

            reqlist=new RequestList(listToSubmit, MainActivity.getDistanceRange(), MainActivity.getBudget(),MainActivity.getUserLatitude(),MainActivity.getUserLongitude());
            Log.i("DistR", ""+MainActivity.getDistanceRange());
            Log.i("Budg", ""+MainActivity.getBudget());
            Log.i("Lat", ""+MainActivity.getUserLatitude());
            Log.i("Long", ""+MainActivity.getUserLongitude());
            Log.i("product", ""+listToSubmit.get(0).getName());

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