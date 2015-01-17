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
import com.kawtar.listshopping.ProductToSend;

public class DetailsFinalListAdapter extends ArrayAdapter<ProductToSend> {
	
	private List<ProductToSend> items;
	private int layoutResourceId;
	private Context context;
	public DetailsFinalListAdapter(Context context, int layoutResourceId, List<ProductToSend> items) {
		super(context, layoutResourceId, items);
		this.layoutResourceId = layoutResourceId;
		this.context = context;
		this.items=new ArrayList<ProductToSend>();
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

		holder.name = (TextView)row.findViewById(R.id.name);
		holder.quantity = (TextView)row.findViewById(R.id.quantity);
        holder.unit = (TextView)row.findViewById(R.id.unity);
		holder.price = (TextView)row.findViewById(R.id.price);
		row.setTag(holder);

		setupItem(holder);
		return row;
	}

	private void setupItem(toShopItemHolder holder) {
		holder.name.setText(holder.offerItem.getName());
		holder.quantity.setText(String.valueOf(holder.offerItem.getQuantity()));
        holder.unit.setText(holder.offerItem.getUnit());
		holder.price.setText(String.valueOf(ResponseFromServer.round(holder.offerItem.getPrice(), 2) ));
	}

	public static class toShopItemHolder {
		ProductToSend offerItem;
		TextView name;
        TextView unit;
		TextView price;
		TextView quantity;
	}
	
}
