package com.kawtar.listshopping;

import java.util.ArrayList;
import java.util.List;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.speech.RecognizerIntent;
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
import android.widget.Toast;

import com.example.kawtar.myapplication.R;

public class ToShopItemListAdapter extends ArrayAdapter<Product> {

	protected static final String LOG_TAG = ToShopItemListAdapter.class.getSimpleName();

    private Intent intent;
	private List<Product> items;
	private int layoutResourceId;
	private Context context;
    private static final String[] color={"red","blue","black","brown","green","none"
    };
    private static final String[] brand={"evian","jocker","candia","bic","daddy","none"
    };
    private static final String[] name={"pencil","milk","sugar","orange juice","apple juice","water"
    };
    private toShopItemHolder holder;
	public ToShopItemListAdapter(Context context, int layoutResourceId, List<Product> items) {
		super(context, layoutResourceId, items);
		this.layoutResourceId = layoutResourceId;
		this.context = context;
		this.items=new ArrayList<Product>();
		this.items = items;

    }
	public List<Product> getShoppingList()
	{
		return items;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View row = convertView;
		holder = null;
		LayoutInflater inflater = ((Activity) context).getLayoutInflater();
		row = inflater.inflate(layoutResourceId, parent, false);

		holder = new toShopItemHolder();
		holder.toShopItem = items.get(position);
		holder.removeItem = (ImageButton)row.findViewById(R.id.removeItem);
		holder.removeItem.setTag(holder.toShopItem);

		holder.brand = (AutoCompleteTextView)row.findViewById(R.id.brand_product);
		setBrandTextListeners(holder);
        ArrayAdapter<String> adapterBrand=new ArrayAdapter<String>(context, android.R.layout.simple_dropdown_item_1line,brand);
        holder.brand.setAdapter(adapterBrand);

		holder.name = (AutoCompleteTextView)row.findViewById(R.id.name_product);
		setNameTextChangeListener(holder);
        ArrayAdapter<String> adapterName=new ArrayAdapter<String>(context, android.R.layout.simple_dropdown_item_1line,name);
        holder.name.setAdapter(adapterName);

		holder.color =(AutoCompleteTextView)row.findViewById(R.id.color_product);
        if(VoiceRecognitionFillListActivity.wordChosen!=null)
        {
            holder.toShopItem.setColor(VoiceRecognitionFillListActivity.wordChosen);
            holder.color.setText(holder.toShopItem.getColor());
            VoiceRecognitionFillListActivity.wordChosen=null;
        }
        else
        {
            setColorTextChangeListener(holder);
            ArrayAdapter<String> adapter=new ArrayAdapter<String>(context, android.R.layout.simple_dropdown_item_1line,color);
            holder.color.setAdapter(adapter);
        }
		holder.quantity = (TextView)row.findViewById(R.id.quantity_product);
		setQuantityTextListeners(holder);
		row.setTag(holder);
		setupItem(holder);
		return row;
	}
	private void setupItem(toShopItemHolder holder)
    {
		holder.name.setText(holder.toShopItem.getName());
		holder.brand.setText(holder.toShopItem.getBrand());
        holder.color.setOnLongClickListener(new View.OnLongClickListener() {

            @Override
            public boolean onLongClick(View v) {
                try {
                    intent=new Intent(context,VoiceRecognitionFillListActivity.class);
                    getContext().startActivity(intent);
                    Toast.makeText(context, "Voice recognition started", Toast.LENGTH_SHORT).show();
                } catch (Exception e) {

                    Toast.makeText(context, "Voice recognition not supported", Toast.LENGTH_SHORT).show();
                    return false;
                }
                return false;
            }

        });
        holder.color.setText(holder.toShopItem.getColor());
		holder.quantity.setText(String.valueOf(holder.toShopItem.getQuantity()));
	}

	public static class toShopItemHolder {
		Product toShopItem;
        AutoCompleteTextView name;
        AutoCompleteTextView brand;
        AutoCompleteTextView color;
		TextView quantity;
		ImageButton removeItem;
	}
	
	private void setNameTextChangeListener(final toShopItemHolder holder) {
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
