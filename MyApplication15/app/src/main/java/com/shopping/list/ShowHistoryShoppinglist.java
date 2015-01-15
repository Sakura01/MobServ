package com.shopping.list;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.TextView;

import com.example.kawtar.myapplication.R;
import com.shopping.list.adapter.HistoryShoppinglistAdapter;
import com.shopping.list.bean.History;
import com.shopping.list.constant.DBConstants;
import com.shopping.list.datasource.ShoppinglistDataSource;

import java.util.List;

public class ShowHistoryShoppinglist extends AbstractShoppinglistActivity {

	private int clickedShoppinglistId;

	private Context context;

	private ShoppinglistDataSource datasource;

	private List<History> historyList;

	private HistoryShoppinglistAdapter historyShoppinglistAdapter;

	private ListView listViewHistory;

	private TextView textViewTitle;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(final Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.datasource = super.getDatasource();
		this.context = super.getContext();

		this.setContentView(R.layout.history_shoppinglist);

		// get the values from calling activity
		this.clickedShoppinglistId = this.getIntent().getIntExtra(DBConstants.COL_SHOPPINGLIST_ID,
				-1);
		final String clickedShoppinglistFinishedTime = this.getIntent().getStringExtra(
				DBConstants.COL_SHOPPINGLIST_FINISHED_TIME);

		// update title TextView with clickedShoppinglist dates
		this.textViewTitle = (TextView) this.findViewById(R.id.titleHistoryShoppinglist);
		this.textViewTitle.setText(clickedShoppinglistFinishedTime);

		this.historyList = this.datasource.getHistoryByShoppinglistId(this.clickedShoppinglistId);

		this.historyShoppinglistAdapter = new HistoryShoppinglistAdapter(this.context,
				this.historyList);

		this.listViewHistory = (ListView) this.findViewById(R.id.listViewHistoryShoppinglist);
		this.listViewHistory.setAdapter(this.historyShoppinglistAdapter);

	}

	@Override
	public boolean onCreateOptionsMenu(final Menu menu) {
		final MenuInflater inflater = this.getMenuInflater();
		inflater.inflate(R.menu.actionbar_menu_history, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(final MenuItem item) {
		switch (item.getItemId()) {

		case android.R.id.home:
			Intent intent = new Intent(this, ShowHistoryOverviewActivity.class);
			intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			startActivity(intent);
			break;

		// AddProductbutton - Actionbar
		case R.id.actionbarDeleteHistory:
			final AlertDialog.Builder alertBox = new AlertDialog.Builder(this);
			alertBox.setMessage("you really want to delete the history?");
			alertBox.setPositiveButton("Yes", new OnClickListener() {

				public void onClick(final DialogInterface dialog, final int which) {
					ShowHistoryShoppinglist.this.datasource.deleteHistory();

					ShowHistoryShoppinglist.this.historyShoppinglistAdapter.clear();
				}
			});

			alertBox.setNegativeButton("No", new OnClickListener() {

				public void onClick(final DialogInterface dialog, final int which) {
					// do nothing here
				}
			});

			alertBox.show();

			break;
		default:
			break;
		}

		return false;
	}

	@Override
	public void onResume() {
		super.onResume();

		this.historyList = this.datasource.getHistoryByShoppinglistId(this.clickedShoppinglistId);
		this.historyShoppinglistAdapter = new HistoryShoppinglistAdapter(this.context,
				this.historyList);
		this.listViewHistory = (ListView) this.findViewById(R.id.listViewHistoryShoppinglist);
		this.listViewHistory.setAdapter(this.historyShoppinglistAdapter);
	}
}
