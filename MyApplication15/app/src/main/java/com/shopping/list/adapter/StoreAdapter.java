package com.shopping.list.adapter;

import java.util.List;

import com.example.kawtar.myapplication.R;
import com.shopping.list.bean.Store;
import com.shopping.list.helper.ProcessColorHelper;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class StoreAdapter extends ArrayAdapter<Store> {

	@SuppressWarnings("unused")
	private final Context context;

	private final List<Store> values;

	public StoreAdapter(final Context context, final List<Store> values) {
		super(context, R.layout.list_row, values);
		this.context = context;
		this.values = values;
	}

	@Override
	public View getView(final int position, final View convertView, final ViewGroup parent) {
		final TextView textView = (TextView) super.getView(position, convertView, parent);

		final Store storeToBeShown = this.values.get(position);
		final int colorToUse = ProcessColorHelper.getColorForProcess(
				storeToBeShown.getAlreadyCheckedProducts(), storeToBeShown.getCountProducts());

		textView.setText(storeToBeShown.toString());
		textView.setTextColor(colorToUse);

		return textView;
	}
}
