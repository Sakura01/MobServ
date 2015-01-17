package com.kawtar.finalresponse;

import java.util.ArrayList;
import java.util.List;

import com.example.kawtar.myapplication.OutdoorMapActivity;
import com.example.kawtar.myapplication.R;
import com.kawtar.jsoncontrol.ResponseFromServer;
import com.kawtar.listshopping.Product;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;


public class FinalResponseAdapter extends ArrayAdapter<ResponseFromServer> {

	protected static final String LOG_TAG = FinalResponseAdapter.class.getSimpleName();
	public static Intent mIntentO;
	public static Intent mIntentD;
	private List<ResponseFromServer> items;
	private int layoutResourceId;
	private Context context;
    public static List<Product> li;
	public FinalResponseAdapter(Context context, int layoutResourceId, List<ResponseFromServer> items) {
		super(context, layoutResourceId, items);
		this.layoutResourceId = layoutResourceId;
		this.context = context;
		this.items=new ArrayList<ResponseFromServer>();
		this.items = items;
	}
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View row = convertView;
		toShopItemHolder holder = null;

		LayoutInflater inflater = ((Activity) context).getLayoutInflater();
		row = inflater.inflate(layoutResourceId, parent, false);

		holder = new toShopItemHolder();
		holder.offerItem = items.get(position);
		holder.acceptOffer = (Button)row.findViewById(R.id.AcceptOffer);
		
		holder.detailsOffer = (Button)row.findViewById(R.id.ShowDetails);
		
		holder.superMarket = (TextView)row.findViewById(R.id.SuperMarket);

		holder.total = (TextView)row.findViewById(R.id.Total);
		holder.distance = (TextView)row.findViewById(R.id.Distance);
	

		row.setTag(holder);

		setupItem(holder);
		return row;
	}
	private void setupItem(final toShopItemHolder holder) {
		holder.superMarket.setText("Supermarket:"+holder.offerItem.getSuperMarket().getName());
		holder.total.setText("Total:"+String.valueOf(holder.offerItem.getTotal())+"€");
		holder.distance.setText("Distance:"+String.valueOf(holder.offerItem.getDistance())+"km");
		holder.acceptOffer.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {

				mIntentO = new Intent(context, OutdoorMapActivity.class);
				getContext().startActivity(mIntentO);
				mIntentO.putExtra("SuperMarketName",holder.offerItem.getSuperMarket().getName());
			}
		});
		holder.detailsOffer.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
                li=new ArrayList<Product>();
				mIntentD = new Intent(context,DetailsFinalListActivity.class);
				getContext().startActivity(mIntentD);
                mIntentD.putExtra("SuperMarket",holder.offerItem.getSuperMarket().getName());
			}
		});

	}

	public static class toShopItemHolder {
		ResponseFromServer offerItem;
		TextView superMarket;
		TextView total;
		TextView distance;
		Button acceptOffer;
		Button detailsOffer;
	}
	
}
