package com.example.kawtar.myapplication;

/**
 * Created by Kawtar on 11/25/2014.
 */
import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;

public class NotificationView extends Activity {
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.notification_outdoor_map_activity);

        TextView tv = (TextView) findViewById(R.id.tv_notification);
        Bundle data = getIntent().getExtras();
        tv.setText(data.getString("content"));
    }
}
