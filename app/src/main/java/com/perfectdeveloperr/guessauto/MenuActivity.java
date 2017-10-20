package com.perfectdeveloperr.guessauto;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;

import com.perfectdev.auto.R;

public class MenuActivity extends AppCompatActivity implements View.OnClickListener  {

    public final static String LOG_TAG="LOG_TAG";

    TextView start;
    TextView records;;
    TextView exit;
    Intent main;
    Intent score;
    MediaPlayer mp;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.menu_activity);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        start = (TextView) findViewById(R.id.start);
       /// Typeface tp =Typeface.createFromAsset(getResources().getAssets(),"fonts/shrift.ttf");
      //  start.setTypeface(tp);
        records = (TextView) findViewById(R.id.records);
      //  records.setTypeface(tp);
        exit = (TextView) findViewById(R.id.exit);
      //  exit.setTypeface(tp);
        start.setOnClickListener(this);
        records.setOnClickListener(this);
        exit.setOnClickListener(this);

        main = new Intent(this,MainActivity.class);
        score = new Intent(this,ScoreActivity.class);

    }
    @Override
    public void onClick(View view) {
        mp = MediaPlayer.create(this, R.raw.trans);
        mp.start();
        switch (view.getId()){
            case R.id.start:
                startActivity(main);
                break;
            case R.id.records:
                startActivity(score);
                break;
            case R.id.exit:
                Intent intent = new Intent(Intent.ACTION_MAIN);
                intent.addCategory(Intent.CATEGORY_HOME);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                break;
        }
    }


}
