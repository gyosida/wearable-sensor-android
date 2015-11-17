package com.belatrix.wearablegyroscope.services;

import android.content.Intent;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import com.belatrix.wearablegyroscope.MainActivity;
import com.belatrix.wearablegyroscope.realtime.OrtcManager;
import com.belatrix.wearablegyroscope.realtime.GyroscopeStat;
import com.google.android.gms.wearable.DataMap;
import com.google.android.gms.wearable.MessageEvent;
import com.google.android.gms.wearable.WearableListenerService;
import com.google.gson.Gson;

import ibt.ortc.extensibility.OrtcClient;

/**
 * Created by pcarrillo on 06/10/2015.
 */
public class GyroscopeWearableListener extends WearableListenerService {

    public static final String TAG = GyroscopeWearableListener.class.getSimpleName();
    public static final String PATH_POSITION_UPDATE = "/WearableGyroscope/PositionUpdate";
    public static final String TAG_X_COORDINATE = "_coordinate_x";
    public static final String TAG_Y_COORDINATE = "_coordinate_y";

    private final Gson gson = new Gson();
    private OrtcClient mRealTimeClient;

    @Override
    public void onCreate() {
        super.onCreate();
        mRealTimeClient = OrtcManager.getInstance().getOrtcClient();
        mRealTimeClient.connect("ddQu0z", "bMNLN9gzom1Z");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mRealTimeClient.disconnect();
    }

    @Override
    public void onMessageReceived(MessageEvent messageEvent) {
        super.onMessageReceived(messageEvent);
        if (messageEvent.getPath().equalsIgnoreCase(PATH_POSITION_UPDATE)) {
            Log.e("Data get", messageEvent.getData().toString());
            byte[] rawData = messageEvent.getData();
            DataMap positionDataMap = DataMap.fromByteArray(rawData);
            Log.e(TAG, "coordinate x " + String.valueOf(positionDataMap.getFloat(TAG_X_COORDINATE)));
            Log.e(TAG, "coordinate y " + String.valueOf(positionDataMap.getFloat(TAG_Y_COORDINATE)));
            // Send information to Real Time client
            GyroscopeStat gyroscopeStat = new GyroscopeStat();
            gyroscopeStat.targetRotationX = positionDataMap.getFloat(TAG_X_COORDINATE);
            gyroscopeStat.targetRotationY = positionDataMap.getFloat(TAG_Y_COORDINATE);

            String json = gson.toJson(gyroscopeStat);
            Log.d(TAG, json);
            notifyStats(gyroscopeStat);
            mRealTimeClient.send("my_channel", json);
        }
    }

    private void notifyStats(GyroscopeStat gyroscopeStat) {
        Intent intent = new Intent(MainActivity.STATS_ACTION);

        intent.putExtra(MainActivity.STATS_KEY, gyroscopeStat);
        LocalBroadcastManager.getInstance(this).sendBroadcast(intent);
    }
}
