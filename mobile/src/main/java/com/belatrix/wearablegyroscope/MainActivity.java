package com.belatrix.wearablegyroscope;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MotionEvent;
import android.view.View;
import android.webkit.WebView;
import android.widget.TextView;

import com.belatrix.wearablegyroscope.realtime.GyroscopeStat;

public class MainActivity extends AppCompatActivity {

    public static final String STATS_KEY = "_stats_key";
    public static final String STATS_ACTION = "_stats_action";

    private final String TAG = this.getClass().getCanonicalName();
    private TextView tvCoordinateX;
    private TextView tvCoordinateY;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        WebView wvCube = (WebView) findViewById(R.id.wv_cube);
        tvCoordinateX = (TextView) findViewById(R.id.tv_coordinate_x);
        tvCoordinateY = (TextView) findViewById(R.id.tv_coordinate_y);

        wvCube.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return true;
            }
        });
        wvCube.loadUrl(BuildConfig.CUBE_URL);
        wvCube.getSettings().setJavaScriptEnabled(true);
        setSupportActionBar(toolbar);
    }

    @Override
    protected void onStart() {
        super.onStart();
        LocalBroadcastManager.getInstance(this).registerReceiver(mStatReceiver, new IntentFilter(STATS_ACTION));
    }

    @Override
    protected void onStop() {
        super.onStop();
        LocalBroadcastManager.getInstance(this).unregisterReceiver(mStatReceiver);
    }

    private BroadcastReceiver mStatReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            GyroscopeStat gyroscopeStat = intent.getParcelableExtra(STATS_KEY);

            if (gyroscopeStat != null) {
                tvCoordinateX.setText(String.valueOf(gyroscopeStat.targetRotationX));
                tvCoordinateY.setText(String.valueOf(gyroscopeStat.targetRotationY));
            }
        }
    };

}
