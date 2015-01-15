package com.kawtar.mainUI;

import java.util.ArrayList;
import java.util.List;

import com.example.kawtar.myapplication.R;
import com.kawtar.gpstracker.GPSTracker;
import com.kawtar.listshopping.AddNewItemActivity;
import com.kawtar.mainUI.controls.SeekbarWithIntervals;


import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.Toast;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;

public class MainActivity extends Activity {
	private SeekbarWithIntervals SeekbarWithIntervals = null;
	private ImageButton mButtonSubmit;
	private EditText mEditTextBudget;
	private String mBudget;
	private static int mDistanceRange;
	private static int  mbudget;
	private Intent mIntent;
	private GPSTracker gps;
	private static double mUserLongitude;
	private static double mUserLatitude;
    public SharedPreferences mPreferences;
    public static final String	PREFERENCES_B	= "PREFERENCES_BUDGET";
    public static final String	PREFERENCES_DR			= "PREFERENCES_DISTANCE_RANGE";
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
        /*
		 * Animate the settings activity. The view is pushed from left to right
		 */
        //overridePendingTransition(R.anim.pull_in_from_left, R.anim.hold);
        // Gets a SharedPreferences instance that points to the default file that is used by the preference framework in the given context
        mPreferences = PreferenceManager.getDefaultSharedPreferences(MainActivity.this);


		List<String> seekbarIntervals = getIntervals();
		getSeekbarWithIntervals().setIntervals(seekbarIntervals);
		
		getSeekbarWithIntervals().setOnSeekBarChangeListener(seekBarChangeListener);
		
		mEditTextBudget = (EditText) findViewById(R.id.EditBudget);
        updateUIFromPreferences();
		mButtonSubmit = (ImageButton) findViewById(R.id.buttonSubmit);
		mButtonSubmit.setOnClickListener(new View.OnClickListener() {
			public void onClick(View view) {
				mBudget = mEditTextBudget.getText().toString();
				int budget = mBudget.length();
				if (budget > 0)
				{
					try 
					{
							mbudget=Integer.parseInt(mBudget );
                            // @see function savePreference()
                            savePreferences();
							// create class object
			                gps = new GPSTracker(MainActivity.this);
			                if(gps.canGetLocation())
			                {
								setUserLatitude(gps.getLatitude());
								setUserLongitude(gps.getLongitude());

								mIntent = new Intent(MainActivity.this, AddNewItemActivity.class);
								startActivity(mIntent);
							}
							else
							{
			                    // can't get location
			                    // GPS or Network is not enabled
			                    // Ask user to enable GPS/network in settings
			                    gps.showSettingsAlert();
							}
							
					}
					catch (NumberFormatException e) 
					{
						createDialog("Error", "Budget should be an integer");
					}
				}	
				else
				{
					createDialog("Error", "Please specify a budget");
				}
			}
		});

	}
	OnSeekBarChangeListener	seekBarChangeListener	= new OnSeekBarChangeListener() {

		public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

		}

		public void onStartTrackingTouch(SeekBar seekBar) {

		}

		public void onStopTrackingTouch(SeekBar seekBar) {

			mDistanceRange = getSeekbarWithIntervals().getProgress();

		}

	};
	public void createDialog(String title, final String text) {
		// Hide keyboard and show it at demand
		getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
		AlertDialog.Builder ad = new AlertDialog.Builder(MainActivity.this);
		// Button to create an alert dialog for setting the connection to the server
		ad.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int id) {

				Toast.makeText(getApplicationContext(),text, Toast.LENGTH_LONG).show();
			}
		});
		// Set the alert dialog title, create it and show it
		ad.setTitle(title + ":" + text);
		AlertDialog alert1 = ad.create();
		alert1.show();
	}
	public static int getBudget()
	{
		return mbudget;
	}
	public static int getDistanceRange()
	{
		return mDistanceRange;
	}
	public static double getUserLatitude()
	{
		return mUserLatitude;
	}
	public static double getUserLongitude()
	{
		return mUserLongitude;
	}
	public void setUserLatitude(double latitude)
	{
		mUserLatitude=latitude;
	}
	public void setUserLongitude(double longitude)
	{
		mUserLongitude=longitude;
	}
	private List<String> getIntervals() {
		return new ArrayList<String>() {{
			add("1");
			add("2");
			add("3");
			add("4");
			add("5");
			add("6");
			add("7");
			add("8");
		}};
	}

	private SeekbarWithIntervals getSeekbarWithIntervals() {
		if (SeekbarWithIntervals == null) {
			SeekbarWithIntervals = (SeekbarWithIntervals) findViewById(R.id.seekbarWithIntervals);
		}
		
		return SeekbarWithIntervals;
	}
    private void updateUIFromPreferences() {
        // Load old values of settings

        int budget = mPreferences.getInt(PREFERENCES_B, 1);

        int distanceRange = mPreferences.getInt(PREFERENCES_DR, 1);

        // Set the new values and update the UI
        mEditTextBudget.setText(String.valueOf(budget));
        mDistanceRange =distanceRange;
        getSeekbarWithIntervals().setProgress(distanceRange);

    }
    private void savePreferences() {
		/*
		 * Load the settings saved last time
		 */
        String budget = mEditTextBudget.getText().toString();
        /*
		 * Save the settings' preferences
		 */
        SharedPreferences.Editor editor = mPreferences.edit();
        if (budget.length() == 0)
        {
            int editBudget = 1;
            editor.putInt(PREFERENCES_B, editBudget);
        }
        else
        {
            int editBudget = Integer.parseInt(budget);
            editor.putInt(PREFERENCES_B, editBudget);
        }

        int distanceRange = getSeekbarWithIntervals().getProgress();


        editor.putInt(PREFERENCES_DR, distanceRange);
        editor.commit();
    }
}
