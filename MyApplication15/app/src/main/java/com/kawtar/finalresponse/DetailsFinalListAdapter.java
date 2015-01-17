package com.kawtar.finalresponse;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.kawtar.myapplication.R;
import com.kawtar.jsoncontrol.ResponseFromServer;
import com.kawtar.listshopping.Product;

public class DetailsFinalListAdapter extends ArrayAdapter<Product> {
	
	private List<Product> items;
	private int layoutResourceId;
	private Context context;
	public DetailsFinalListAdapter(Context context, int layoutResourceId, List<Product> items) {
		super(context, layoutResourceId, items);
		this.layoutResourceId = layoutResourceId;
		this.context = context;
		this.items=new ArrayList<Product>();
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
		
		holder.status = (TextView)row.findViewById(R.id.status);
		holder.brand = (TextView)row.findViewById(R.id.brand);
		holder.name = (TextView)row.findViewById(R.id.name);
		holder.color= (TextView)row.findViewById(R.id.color);
		holder.quantity = (TextView)row.findViewById(R.id.quantity);
		holder.price = (TextView)row.findViewById(R.id.price);
		row.setTag(holder);

		setupItem(holder);
		return row;
	}

	private void setupItem(toShopItemHolder holder) {
		holder.name.setText(holder.offerItem.getName());
		holder.quantity.setText(String.valueOf(holder.offerItem.getQuantity()));
		holder.price.setText(String.valueOf(ResponseFromServer.round(holder.offerItem.getPrice(), 2) ));
	}

	public static class toShopItemHolder {
		Product offerItem;
		TextView name;
		TextView brand;
		TextView status;
		TextView color;
		TextView price;
		TextView quantity;
	}
	
}
