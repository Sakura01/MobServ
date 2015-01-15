package com.shopping.list.adapter;

import java.util.Date;
import java.util.List;
import com.example.kawtar.myapplication.R;
import com.shopping.list.bean.Shoppinglist;
import com.shopping.list.helper.GMTToLocalTimeConverter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class HistoryOverviewAdapter extends ArrayAdapter<Shoppinglist> {

	@SuppressWarnings("unused")
	private final Context context;

	private final List<Shoppinglist> values;

	public HistoryOverviewAdapter(final Context context, final List<Shoppinglist> values) {
		super(context, R.layout.list_row, values);
		this.context = context;
		this.values = values;
	}

	@Override
	public View getView(final int position, final View convertView, final ViewGroup parent) {
		final TextView textView = (TextView) super.getView(position, convertView, parent);

		final Shoppinglist shoppinglistToBeShown = this.values.get(position);

		final Date finishedTime = GMTToLocalTimeConverter.convert(shoppinglistToBeShown
				.getFinishedTime());

		textView.setText(finishedTime.toLocaleString());

		return textView;
	}
}
