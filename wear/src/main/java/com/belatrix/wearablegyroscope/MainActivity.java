package com.belatrix.wearablegyroscope;

import android.app.Activity;
import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.support.wearable.view.DismissOverlayView;
import android.support.wearable.view.WatchViewStub;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.wearable.DataMap;
import com.google.android.gms.wearable.MessageApi;
import com.google.android.gms.wearable.Node;
import com.google.android.gms.wearable.NodeApi;
import com.google.android.gms.wearable.Wearable;

import java.util.Collection;
import java.util.HashSet;

/**
 * Created by pcarrillo on 06/10/2015.
 */
public class MainActivity extends Activity implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, SensorEventListener {

    public static final String TAG = MainActivity.class.getSimpleName();
    public static final String PATH_POSITION_UPDATE = "/WearableGyroscope/PositionUpdate";
    public static final String TAG_X_COORDINATE = "_coordinate_x";
    public static final String TAG_Y_COORDINATE = "_coordinate_y";

    private GoogleApiClient mGoogleApiClient;
    private Node mNode;
    private SensorManager mSensorManager;
    private Sensor mGyroscopeSensor;

    private TextView tvCoordinateX;
    private TextView tvCoordinateY;

    private float[] gravity = new float[3];
    private float[] linear_acceleration = new float[3];
    private final float alpha = 0.6f;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tvCoordinateX = (TextView) findViewById(R.id.tv_coordinate_x);
        tvCoordinateY = (TextView) findViewById(R.id.tv_coordinate_y);

        mSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        mGyroscopeSensor = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);

        buildGoogleApiClient();
    }

    private void buildGoogleApiClient() {
        mGoogleApiClient = new GoogleApiClient.Builder(MainActivity.this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(Wearable.API)
                .build();
    }

    @Override
    protected void onStart() {
        super.onStart();
        mGoogleApiClient.connect();
    }

    @Override
    protected void onResume() {
        super.onResume();
        mSensorManager.registerListener(this, mGyroscopeSensor, SensorManager.SENSOR_DELAY_NORMAL);
    }

    @Override
    protected void onPause() {
        super.onPause();
        mSensorManager.unregisterListener(this);
    }

    @Override
    protected void onStop() {
        super.onStop();
        mGoogleApiClient.disconnect();
    }

    @Override
    public void onConnected(Bundle bundle) {
        resolveNode();
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {

    }

    private void resolveNode() {
        Wearable.NodeApi.getConnectedNodes(mGoogleApiClient).setResultCallback(new ResultCallback<NodeApi.GetConnectedNodesResult>() {
            @Override
            public void onResult(NodeApi.GetConnectedNodesResult nodes) {
                for (Node node : nodes.getNodes()) {
                    mNode = node;
                }
            }
        });
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

        updateStats(linear_acceleration[0], linear_acceleration[1]);
    }

    private void updateStats(float x, float y) {
        tvCoordinateX.setText(String.valueOf(x));
        tvCoordinateY.setText(String.valueOf(y));

        sendPositionData(x, y);
    }

    private void sendPositionData(float x, float y) {
        if (mNode != null) {
            DataMap positionData = new DataMap();
            positionData.putFloat(TAG_X_COORDINATE, x);
            positionData.putFloat(TAG_Y_COORDINATE, y);
            Wearable.MessageApi.sendMessage(mGoogleApiClient, mNode.getId(),
                    PATH_POSITION_UPDATE, positionData.toByteArray()).setResultCallback(
                    new ResultCallback<MessageApi.SendMessageResult>() {

                        @Override
                        public void onResult(MessageApi.SendMessageResult sendMessageResult) {
                            Log.e(TAG, "SendRequireMessage:" + sendMessageResult.getStatus());
                            if (!sendMessageResult.getStatus().isSuccess()) {
                            }
                        }
                    }
            );
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }
}

