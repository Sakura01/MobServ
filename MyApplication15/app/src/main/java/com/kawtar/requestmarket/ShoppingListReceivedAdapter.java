package com.kawtar.requestmarket;
import java.util.List;

import com.example.kawtar.myapplication.R;
import com.kawtar.listshopping.ProductToSend;


import android.app.Activity;
import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.ImageButton;
import android.widget.TextView;

public class ShoppingListReceivedAdapter extends ArrayAdapter<ProductToSend> {

	
	private List<ProductToSend> items;
	private int layoutResourceId;
	private Context context;
    private static final String[] color={"red","blue","black","brown","green","none"
    };
    private static final String[] brand={"evian","jocker","candia","bic","daddy","none"
    };
    private static final String[] name={"pencil","milk","sugar","orange juice","apple juice","water"
    };
	public ShoppingListReceivedAdapter(Context context, int layoutResourceId, List<ProductToSend> items) {
		super(context, layoutResourceId, items);
		this.layoutResourceId = layoutResourceId;
		this.context = context;
		this.items = items;
	}
	public List<ProductToSend> getShoppingList()
	{
		return items;
	}
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View row = convertView;
		toShopItemHolder holder = null;

		LayoutInflater inflater = ((Activity) context).getLayoutInflater();
		row = inflater.inflate(layoutResourceId, parent, false);

		holder = new toShopItemHolder();
		holder.toShopItem = items.get(position);
		holder.removeItem = (ImageButton)row.findViewById(R.id.removeItem);
		holder.removeItem.setTag(holder.toShopItem);


		holder.name = (AutoCompleteTextView)row.findViewById(R.id.name_product);
		setNameTextListeners(holder);
        ArrayAdapter<String> adapterName=new ArrayAdapter<String>(context, android.R.layout.simple_dropdown_item_1line,name);
        holder.name.setAdapter(adapterName);


		holder.quantity = (TextView)row.findViewById(R.id.quantity_product);
		setQuantityTextListeners(holder);

		row.setTag(holder);

		setupItem(holder);
		return row;
	}

	private void setupItem(toShopItemHolder holder) {

		holder.name.setText(holder.toShopItem.getName());

		holder.quantity.setText(String.valueOf((holder.toShopItem.getQuantity())));

	}

	public static class toShopItemHolder {
		ProductToSend toShopItem;
        AutoCompleteTextView name;
		TextView quantity;
		ImageButton removeItem;

	}

	private void setNameTextListeners(final toShopItemHolder holder) {
		holder.name.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {

					holder.toShopItem.setName(holder.name.getText().toString());

			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count, int after) { }

			@Override
			public void afterTextChanged(Editable s) { }
		});
	}

	private void setQuantityTextListeners(final toShopItemHolder holder) {
		holder.quantity.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				try{
					holder.toShopItem.setQuantity(Integer.parseInt(s.toString()));
				}catch (NumberFormatException e) {
					Log.e("TAG", "error reading quantity value: " + s.toString());
				}
			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count, int after) { }

			@Override
			public void afterTextChanged(Editable s) { }
		});
	}
}
