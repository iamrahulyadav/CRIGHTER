package com.shoaibnwar.crighter.Activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.shoaibnwar.crighter.Services.LockService;
import com.shoaibnwar.crighter.Preferences.SPref;
import com.shoaibnwar.crighter.R;

public class DashboardActivity extends AppCompatActivity {

    RelativeLayout tv_start;
    TextView tv_text_start;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        init();
        startButton();
    }
    private void init()
    {
        tv_start = (RelativeLayout) findViewById(R.id.tv_start);
        tv_text_start = (TextView) findViewById(R.id.tv_text_start);

        SharedPreferences sharedPreferences = getSharedPreferences(SPref.PREF_SERVICE, 0);
        boolean isServiceRunning = SPref.getServiceStatus(sharedPreferences);

        if (isServiceRunning){
            tv_text_start.setText("Stop");
        }else {
            tv_text_start.setText("Start");
        }
    }

    private void startButton()
    {
        tv_start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                if (tv_text_start.getText().toString().equals("Start")) {
                    startService(new Intent(getApplicationContext(), LockService.class));
                    Log.e("TAg", "Service Started");
                    tv_text_start.setText("Stop");
                    SharedPreferences sharedPreferences = getSharedPreferences(SPref.PREF_SERVICE, 0);
                    SPref.storingServiceStatus(sharedPreferences, true);
                    finish();
                }
               else if (tv_text_start.getText().toString().equals("Stop")){
                    stopService(new Intent(getApplicationContext(), LockService.class));
                    tv_text_start.setText("Start");
                    Log.e("TAg", "Service stop");
                    SharedPreferences sharedPreferences = getSharedPreferences(SPref.PREF_SERVICE, 0);
                    SPref.storingServiceStatus(sharedPreferences, false);
                }
            }
        });
    }
}
