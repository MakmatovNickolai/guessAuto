package com.perfectdeveloperr.guessauto;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;


import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.perfectdev.auto.R;
import com.perfectdeveloperr.guessauto.data.DBHelper;

import java.util.Random;
import java.util.regex.Pattern;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    public final static String LOG_TAG="LOG_TAG";
    public static final String APP_PREFERENCES = "mysettings";
    private static final String APP_PREFERENCES_SCORE = "score" ;
    public static String PACKAGE_NAME;
    public SharedPreferences mSettings;

    private Button button;
    private Button button5;
    private Button button3;
    private Button button4;
    private ImageView img;
    private ImageView[] hearts;
    private TextView txt;
    private ProgressBar progress;
    private SharedPreferences.Editor editor;
    private int nextImage;
    private Random rnd = new Random();
    private String[] images;

    private DBHelper dbHelper;

    private SQLiteDatabase db;

    private int nexim=0;
    private int score=0;
    private String but;
    private String temp;
    private CountDownTimer timer;
    private int attempt=0;
    private AlertDialog.Builder ad;
    private MediaPlayer mp;

    @Override
    protected void onStop(){
        super.onStop();
        db.close();
        dbHelper.close();


    }

    @Override
    protected void onDestroy(){
        super.onDestroy();

        timer.cancel();

    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_main);

        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        mSettings = getSharedPreferences(APP_PREFERENCES, Context.MODE_PRIVATE);

        MobileAds.initialize(getApplicationContext(),"ca-app-pub-5215138241700973~4827004632");
        AdView adView = (AdView) findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        adView.loadAd(adRequest);
        progress =(ProgressBar) findViewById(R.id.progressBar);
        button = (Button) findViewById(R.id.button);
        button5 = (Button) findViewById(R.id.button5);
        button3 = (Button) findViewById(R.id.button3);
        button4 = (Button) findViewById(R.id.button4);
        img = (ImageView) findViewById(R.id.imageView2);
        txt = (TextView)  findViewById(R.id.textView);

        ImageView heart1 = (ImageView) findViewById((R.id.imageView));
        ImageView heart2 = (ImageView) findViewById((R.id.imageView3));
        ImageView heart3 = (ImageView) findViewById((R.id.imageView4));
        hearts= new ImageView[]{heart1, heart2, heart3};

        button.setOnClickListener(this);
        button5.setOnClickListener(this);
        button3.setOnClickListener(this);
        button4.setOnClickListener(this);
        PACKAGE_NAME = getApplicationContext().getPackageName();

        ad = new AlertDialog.Builder(this);
        ad.setNegativeButton("ОК",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        mp = MediaPlayer.create(MainActivity.this, R.raw.trans);
                        mp.start();
                        dialog.cancel();
                        MainActivity.this.finish();
                        startActivity(new Intent(MainActivity.this,MenuActivity.class));
                    }
                });

        dbHelper = new DBHelper(this);

        timer =new CountDownTimer(15000, 50) {

            public void onTick(long millisUntilFinished) {
                progress.setProgress((int)millisUntilFinished / 150);
            }

            public void onFinish() {
                img.animate().alpha(0).setDuration(1000);
                Thread th = new Thread((new Runnable() {
                    @Override
                    public void run() {
                        h.sendEmptyMessageDelayed(1,1000);
                        h.removeCallbacks(null);
                    }
                    Handler h = new Handler() {
                        @Override
                        public void handleMessage(Message msg) {
                            img.animate().alpha(1).setDuration(400);
                            timer.start();
                            setRandomImage();
                            setButtonsText();
                            h.removeCallbacks(null);
                        }
                    };
                }));
                th.start();
                loseAttempt();
            }
        };
        db = dbHelper.getWritableDatabase();
        images = getAuto("autoID");
        swapArray(images);
        setRandomImage();
        setButtonsText();
        timer.start();

    }

    public void pressButton(final Button x){
        if(nexim==51){
            attempt=2;
            loseAttempt();
        }
        timer.cancel();
        img.animate().alpha(0).setDuration(1000);
        Thread th = new Thread((new Runnable() {
            @Override
            public void run() {
                h.sendEmptyMessageDelayed(1,1000);
                h.removeCallbacks(null);
            }
            Handler h = new Handler() {
                @Override
                public void handleMessage(Message msg) {
                    setRandomImage();
                    setButtonsText();
                    img.animate().alpha(1).setDuration(400);
                    timer.start();
                    x.setBackgroundResource(R.drawable.button_bg);
                    button.setClickable(true);
                    button3.setClickable(true);
                    button4.setClickable(true);
                    button5.setClickable(true);
                    h.removeCallbacks(null);
                }
            };
        }));
        th.start();
        String sqlQuery = "select autoDrawableName from mytable where autoID = ?";
        Cursor c = db.rawQuery(sqlQuery,new String[]{getResources().getResourceEntryName(nextImage)});
        int autoa = c.getColumnIndex("autoDrawableName");
        but = x.getText().toString();
        if (c.moveToFirst()) {
            temp =c.getString(autoa);
            c.close();
        }
        if(but.equals(temp)){
            mp = MediaPlayer.create(this, R.raw.success);
            mp.start();
            score+=(progress.getProgress());
            txt.setText(getResources().getString(R.string.score)+" "+score);
            x.setBackgroundResource(R.drawable.buttontrue);
        }
        else{
            mp = MediaPlayer.create(this, R.raw.fail);
            mp.start();
            x.setBackgroundResource(R.drawable.buttonfalse);;
            loseAttempt();
        }
    }

    public void loseAttempt() {
        attempt++;
        hearts[attempt-1].setImageResource(R.drawable.heart);
        if(attempt == 3){
            if(!mSettings.contains(APP_PREFERENCES_SCORE)){
                editor = mSettings.edit();
                editor.putInt(APP_PREFERENCES_SCORE, 0);
                editor.apply();
            }
            if(score > mSettings.getInt(APP_PREFERENCES_SCORE, 0)){

                ad.setIcon(R.drawable.starwin).setTitle(R.string.title).setMessage(getResources().getString(R.string.alert)+" "+score).setCancelable(false);
                editor = mSettings.edit();
                editor.putInt(APP_PREFERENCES_SCORE, score);
                editor.apply();
                attempt=0;
                AlertDialog alert = ad.create();
                alert.show();
                return;
            }
            else{
                ad.setIcon(R.drawable.star).setTitle(R.string.title2).setMessage(getResources().getString(R.string.alert2)+" "+score).setCancelable(false);
                AlertDialog alert = ad.create();
                alert.show();
                attempt=0;
                return;
            }
        }

    }

    @Override
    public void onClick(View view) {
        button.setClickable(false);
        button3.setClickable(false);
        button4.setClickable(false);
        button5.setClickable(false);
        switch (view.getId()) {
            case R.id.button:
                pressButton(button);
                break;
            case R.id.button3:
                pressButton(button3);
                break;
            case R.id.button4:
                pressButton(button4);
                break;
            case R.id.button5:
                pressButton(button5);
                break;
        }
    }

    public void setRandomImage() {
        nextImage = getResources().getIdentifier(images[nexim], "drawable", PACKAGE_NAME);
        img.setImageResource(nextImage);
        nexim++;
    }

    public String[] splitString(){
        db = dbHelper.getWritableDatabase();
        String sqlQueryww = "select buttons from mytable where autoID = ?";
        Cursor c = db.rawQuery(sqlQueryww,new String[]{getResources().getResourceEntryName(nextImage)});
        int au = c.getColumnIndex("buttons");
        String tempe = new String();
        if(c.moveToFirst()){
            tempe = c.getString(au);;
            c.close();
        }
        Pattern pattern = Pattern.compile(",");
        return  pattern.split(tempe);
    }

    public void setButtonsText(){
        String[] buttons = splitString();
        String[] butt= new String[4];
        System.arraycopy(buttons,0,butt,0,butt.length);
        swapArray(butt);
        button.setText(butt[0]);
        button3.setText(butt[1]);
        button4.setText(butt[2]);
        button5.setText(butt[3]);
    }

    public void swapArray(String[] array){
        for (int i=array.length-1; i>0; i--) {
            int j = rnd.nextInt(i+1);
            String temp = array[i];
            array[i] = array[j];
            array[j] = temp;
        }
    }

    public String[] getAuto(String x){
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        String[] temp = new String[51];
        Cursor c = db.query("mytable", new String[] {x}, null,null, null, null, null);
        if (c.moveToFirst()) {
            int imageColIndex = c.getColumnIndex(x);
            temp[0] = c.getString(imageColIndex);
            for (int i = 1;c.moveToNext()  ; i++ ){
                temp[i]=c.getString(imageColIndex);
            }
        }
        c.close();
        return temp;
    }

    public void toMenu(View view) {
        mp = MediaPlayer.create(this, R.raw.trans);
        mp.start();

        startActivity(new Intent(this,MenuActivity.class));
        finish();
    }
}

