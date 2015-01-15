package com.kawtar.listshopping;


import java.util.ArrayList;
import java.util.List;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.util.Log;
import android.view.View;

import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;
import android.widget.TextView;
import android.view.View.OnClickListener;
import com.example.kawtar.myapplication.R;
import com.kawtar.mainUI.MainActivity;
import com.kawtar.mainUI.SplashActivity;

/**
 * SpeechRepeatActivity
 * - processes speech input
 * - presents user with list of suggested words
 */
public class VoiceRecognitionFillListActivity extends Activity implements OnClickListener {

    //variable for checking Voice Recognition support on user device
    private static final int VR_REQUEST = 999;
    public static String wordChosen=null;


    //ListView for displaying suggested words
    private ListView wordList;
    private Button tryAgain;
    //Log tag for output information
    private final String LOG_TAG = "SpeechRepeatActivity";

    /** Create the Activity, prepare to process speech and repeat */
    @Override
    public void onCreate(Bundle savedInstanceState) {

        //call superclass
        super.onCreate(savedInstanceState);
        //set content view
        setContentView(R.layout.recognition_activity_layout);
        //gain reference to word list
        wordList = (ListView) findViewById(R.id.word_list);
        tryAgain=(Button) findViewById(R.id.tryButton);

        //find out whether speech recognition is supported
        PackageManager packManager = getPackageManager();
        List<ResolveInfo> intActivities = packManager.queryIntentActivities
                (new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH), 0);
        if (intActivities.size() != 0) {

                        //listen for results
                        listenToSpeech();
                        tryAgain.setOnClickListener(this);
        }
        else
        {
            //speech recognition not supported, disable button and output message
            createDialog("Error","Speech recognition not supported!");
            tryAgain.setEnabled(false);
        }

        //detect user clicks of suggested words
        wordList.setOnItemClickListener(new OnItemClickListener() {

            //click listener for items within list
            public void onItemClick(AdapterView<?> parent, View view, int position, long id)
            {
                //cast the view
                TextView wordView = (TextView)view;
                //retrieve the chosen word
                wordChosen = (String) wordView.getText();
                //output for debugging
                Log.v(LOG_TAG, "chosen: "+wordChosen);
                //output Toast message
                Toast.makeText(VoiceRecognitionFillListActivity.this, "You said: "+wordChosen, Toast.LENGTH_SHORT).show();//**alter for your Activity name***
                Intent intent=new Intent();
                //intent.putExtra("Color",wordChosen);
                setResult(RESULT_OK, intent);
                finish();
            }
        });
    }

    public void createDialog(final String title, String text) {
        // Hide keyboard and show it at demand
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        AlertDialog.Builder ad = new AlertDialog.Builder(VoiceRecognitionFillListActivity.this);
        // Button to create an alert dialog for setting the connection to the server
        ad.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {

                    Intent activity=new Intent(VoiceRecognitionFillListActivity.this, AddNewItemActivity.class);
                    startActivity(activity);

            }
        });
        // Set the alert dialog title, create it and show it
        ad.setTitle(title + ":" + text);
        AlertDialog alert1 = ad.create();
        alert1.show();
    }
    /**
     * Instruct the app to listen for user speech input
     */
    private void listenToSpeech() {

        //start the speech recognition intent passing required data
        Intent listenIntent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        //indicate package
        listenIntent.putExtra(RecognizerIntent.EXTRA_CALLING_PACKAGE, getClass().getPackage().getName());
        //message to display while listening
        listenIntent.putExtra(RecognizerIntent.EXTRA_PROMPT, "Say a word!");
        //set speech model
        listenIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        //specify number of results to retrieve
        listenIntent.putExtra(RecognizerIntent.EXTRA_MAX_RESULTS, 10);

        //start listening
        startActivityForResult(listenIntent, VR_REQUEST);
    }

    /**
     * onActivityResults handles:
     *  - retrieving results of speech recognition listening
     *  - retrieving result of TTS data check
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        //check speech recognition result
        if (requestCode == VR_REQUEST && resultCode == RESULT_OK)
        {
            //store the returned word list as an ArrayList
            ArrayList<String> suggestedWords = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
            //set the retrieved list to display in the ListView using an ArrayAdapter
            wordList.setAdapter(new ArrayAdapter<String> (this, R.layout.word_reco, suggestedWords));
        }


        //call superclass method
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onClick(View v) {
        if(v.getId()==R.id.tryButton)
        {
            listenToSpeech();
        }
    }
    public void onBackPressed() {

        if (SplashActivity.CONDITION)
        {
            // do nothing
        }
        else
            super.onBackPressed();

    }
}
