package com.belatrix.wearablegyroscope;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;

import com.belatrix.wearablegyroscope.realtime.OrtcManager;
import com.belatrix.wearablegyroscope.realtime.Params;
import com.google.gson.Gson;

import ibt.ortc.extensibility.OrtcClient;

public class MainActivity extends AppCompatActivity implements SensorEventListener {

    private final String TAG = this.getClass().getCanonicalName();
//    private final Gson gson = new Gson();
    private SensorManager mSensorManager;
    private Sensor mGyroscopeSensor;
//    private OrtcClient mRealTimeClient;
    private float[] gravity = new float[3];
    private float[] linear_acceleration = new float[3];
    private final float alpha = 0.6f;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        mSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        mGyroscopeSensor = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
//        mRealTimeClient = OrtcManager.getInstance().getOrtcClient();

        setSupportActionBar(toolbar);
    }

    @Override
    protected void onResume() {
        super.onResume();

//        mRealTimeClient.connect("ddQu0z", "bMNLN9gzom1Z");
        mSensorManager.registerListener(this, mGyroscopeSensor, SensorManager.SENSOR_DELAY_NORMAL);
    }

    @Override
    protected void onPause() {
        super.onPause();

        mSensorManager.unregisterListener(this);
//        mRealTimeClient.disconnect();
    }

    @Override
    public void onSensorChanged(SensorEvent event) {

        // Isolate the force of gravity with the low-pass filter.
        gravity[0] = alpha * gravity[0] + (1 - alpha) * event.values[0];
        gravity[1] = alpha * gravity[1] + (1 - alpha) * event.values[1];
        gravity[2] = alpha * gravity[2] + (1 - alpha) * event.values[2];

        // Remove the gravity contribution with the high-pass filter.
        linear_acceleration[0] = event.values[0] - gravity[0];
        linear_acceleration[1] = event.values[1] - gravity[1];
        linear_acceleration[2] = event.values[2] - gravity[2];

        Params params = new Params();
        params.targetRotationX = linear_acceleration[0];
        params.targetRotationY = linear_acceleration[1];

//        String json = gson.toJson(params);

//        Log.d(TAG, json);
//        mRealTimeClient.send("my_channel", json);
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }
}
