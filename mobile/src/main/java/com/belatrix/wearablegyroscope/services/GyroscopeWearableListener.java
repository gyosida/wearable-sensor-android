package com.belatrix.wearablegyroscope.services;

import android.util.Log;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.wearable.DataEventBuffer;
import com.google.android.gms.wearable.DataMap;
import com.google.android.gms.wearable.MessageEvent;
import com.google.android.gms.wearable.Wearable;
import com.google.android.gms.wearable.WearableListenerService;

/**
 * Created by pcarrillo on 06/10/2015.
 */
public class GyroscopeWearableListener extends WearableListenerService {

    public static final String TAG = GyroscopeWearableListener.class.getSimpleName();
    public static final String PATH_POSITION_UPDATE = "/WearableGyroscope/PositionUpdate";
    public static final String TAG_X_COORDINATE = "_coordinate_x";
    public static final String TAG_Y_COORDINATE = "_coordinate_y";

    private static GoogleApiClient mGoogleApiClient;

    @Override
    public void onMessageReceived(MessageEvent messageEvent) {
        super.onMessageReceived(messageEvent);
        Log.e("Data get", messageEvent.getPath().toString());
        if (messageEvent.getPath().equalsIgnoreCase(PATH_POSITION_UPDATE)) {
            Log.e("Data get", messageEvent.getData().toString());
            byte[] rawData = messageEvent.getData();
            DataMap positionDataMap = DataMap.fromByteArray(rawData);
            Log.e("coordinate x", String.valueOf(positionDataMap.getFloat(TAG_X_COORDINATE)));
            Log.e("coordinate y", String.valueOf(positionDataMap.getFloat(TAG_Y_COORDINATE)));
//            if(mGoogleApiClient == null) mGoogleApiClient = new GoogleApiClient.Builder(this).addApi(Wearable.API).build();
            // start sending data to server.
        }
    }
}
