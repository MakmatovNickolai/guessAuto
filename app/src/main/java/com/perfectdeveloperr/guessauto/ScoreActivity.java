package com.perfectdeveloperr.guessauto;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.WindowManager;
import android.widget.TextView;

import com.perfectdev.auto.R;

public class ScoreActivity extends AppCompatActivity{

    TextView txt;

    private SharedPreferences mSettings;
    public static final String APP_PREFERENCES = "mysettings";
    private static final String APP_PREFERENCES_SCORE = "score" ;


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.score_activity);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        mSettings = getApplicationContext().getSharedPreferences(APP_PREFERENCES, Context.MODE_PRIVATE);

        txt = (TextView) findViewById(R.id.textView3);
        if (mSettings.contains(APP_PREFERENCES_SCORE)) {

            txt.setText(getResources().getString(R.string.record)+" "+mSettings.getInt(APP_PREFERENCES_SCORE,0));

        }

    }

    @Override
    protected void onResume(){
        super.onResume();


        if (mSettings.contains(APP_PREFERENCES_SCORE)) {

            txt.setText(getResources().getString(R.string.record)+" "+mSettings.getInt(APP_PREFERENCES_SCORE,0));

        }

    }

}
