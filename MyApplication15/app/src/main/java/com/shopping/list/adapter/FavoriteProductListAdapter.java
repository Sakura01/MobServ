package com.shopping.list.adapter;

import java.util.List;

import com.example.kawtar.myapplication.R;
import com.shopping.list.bean.FavoriteProductMapping;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class FavoriteProductListAdapter extends ArrayAdapter<FavoriteProductMapping> {

	@SuppressWarnings("unused")
	private final Context context;

	private final List<FavoriteProductMapping> values;

	public FavoriteProductListAdapter(final Context context,
			final List<FavoriteProductMapping> values) {
		super(context, R.layout.list_row, values);
		this.context = context;
		this.values = values;
	}

	@Override
	public View getView(final int position, final View convertView, final ViewGroup parent) {
		final TextView textView = (TextView) super.getView(position, convertView, parent);

		final FavoriteProductMapping favoriteProductMapping = this.values.get(position);

		final String textToShow = favoriteProductMapping.getQuantity() + " "
				+ favoriteProductMapping.getProduct().getUnit().getName() + " "
				+ favoriteProductMapping.getProduct().getName() + " ("
				+ favoriteProductMapping.getStore().getName() + ")";

		textView.setText(textToShow);

		return textView;
	}
}
