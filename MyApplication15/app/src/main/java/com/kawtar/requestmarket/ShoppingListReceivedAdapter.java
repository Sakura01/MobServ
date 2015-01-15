package com.kawtar.requestmarket;
import java.util.List;

import com.example.kawtar.myapplication.R;
import com.kawtar.listshopping.Product;
import com.kawtar.listshopping.ToShopItemListAdapter;

import android.app.Activity;
import android.content.Context;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.TextView;

public class ShoppingListReceivedAdapter extends ArrayAdapter<Product> {

	protected static final String LOG_TAG = ToShopItemListAdapter.class.getSimpleName();
	
	private List<Product> items;
	private int layoutResourceId;
	private Context context;
    private static final String[] color={"red","blue","black","brown","green","none"
    };
    private static final String[] brand={"evian","jocker","candia","bic","daddy","none"
    };
    private static final String[] name={"pencil","milk","sugar","orange juice","apple juice","water"
    };
	public ShoppingListReceivedAdapter(Context context, int layoutResourceId, List<Product> items) {
		super(context, layoutResourceId, items);
		this.layoutResourceId = layoutResourceId;
		this.context = context;
		this.items = items;
	}
	public List<Product> getShoppingList()
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

		holder.status = (TextView)row.findViewById(R.id.status_product);
		setStatusTextListeners(holder);
		holder.acceptItem = (CheckBox)row.findViewById(R.id.accept_product);

		setAcceptCheckBoxListeners(holder);
		
		holder.brand = (AutoCompleteTextView)row.findViewById(R.id.brand_product);
		setBrandTextListeners(holder);
        ArrayAdapter<String> adapterBrand=new ArrayAdapter<String>(context, android.R.layout.simple_dropdown_item_1line,brand);
        holder.brand.setAdapter(adapterBrand);

		holder.name = (AutoCompleteTextView)row.findViewById(R.id.name_product);
		setNameTextListeners(holder);
        ArrayAdapter<String> adapterName=new ArrayAdapter<String>(context, android.R.layout.simple_dropdown_item_1line,name);
        holder.name.setAdapter(adapterName);

		holder.color = (AutoCompleteTextView)row.findViewById(R.id.color_product);
        setColorTextChangeListener(holder);
        ArrayAdapter<String> adapter=new ArrayAdapter<String>(context, android.R.layout.simple_dropdown_item_1line,color);
        holder.color.setAdapter(adapter);

		holder.quantity = (TextView)row.findViewById(R.id.quantity_product);
		setQuantityTextListeners(holder);

		row.setTag(holder);

		setupItem(holder);
		return row;
	}

	private void setupItem(toShopItemHolder holder) {
		holder.status.setText(holder.toShopItem.getStatus());
		holder.name.setText(holder.toShopItem.getName());
		holder.brand.setText(holder.toShopItem.getBrand());
		holder.color.setText(holder.toShopItem.getColor());
		holder.quantity.setText(String.valueOf((holder.toShopItem.getQuantity())));
		holder.acceptItem.setChecked(holder.toShopItem.getAccept());
	}

	public static class toShopItemHolder {
		Product toShopItem;
		TextView status;
        AutoCompleteTextView name;
        AutoCompleteTextView brand;
        AutoCompleteTextView color;
		TextView quantity;
		ImageButton removeItem;
		CheckBox acceptItem;
	}
	private void setStatusTextListeners(final toShopItemHolder holder) 
	{

			holder.toShopItem.setStatus(holder.toShopItem.getStatus());
	}
	private void setAcceptCheckBoxListeners(final toShopItemHolder holder) {

			
			holder.acceptItem.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (((CheckBox) v).isChecked()) 
				{
					holder.toShopItem.setAccept(true);
			    }
			   else 
			   {
				   holder.toShopItem.setAccept(false);
			   }
			}
		});

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

	private void setBrandTextListeners(final toShopItemHolder holder) {
		holder.brand.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {

					holder.toShopItem.setBrand(holder.brand.getText().toString());

			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count, int after) { }

			@Override
			public void afterTextChanged(Editable s) { }
		});
	}
    private void setColorTextChangeListener(final toShopItemHolder holder) {
        holder.color.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                holder.toShopItem.setColor(holder.color.getText().toString());
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) { }

            @Override
            public void afterTextChanged(Editable s) {}
        });
    }
	private void setQuantityTextListeners(final toShopItemHolder holder) {
		holder.quantity.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				try{
					holder.toShopItem.setQuantity(Integer.parseInt(s.toString()));
				}catch (NumberFormatException e) {
					Log.e(LOG_TAG, "error reading quantity value: " + s.toString());
				}
			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count, int after) { }

			@Override
			public void afterTextChanged(Editable s) { }
		});
	}
}
