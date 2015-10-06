package com.belatrix.wearablegyroscope;

import android.app.Activity;
import android.os.Bundle;
import android.support.wearable.view.WatchViewStub;
import android.util.Log;
import android.view.View;
import android.widget.Button;

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
public class MainActivity extends Activity implements View.OnClickListener, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {

    public static final String TAG = MainActivity.class.getSimpleName();

    private Button btnSend;
    public static final String PATH_POSITION_UPDATE = "/WearableGyroscope/PositionUpdate";
    public static final String TAG_X_COORDINATE = "_coordinate_x";
    public static final String TAG_Y_COORDINATE = "_coordinate_y";

    GoogleApiClient mGoogleApiClient;
    private Node mNode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mGoogleApiClient = new GoogleApiClient.Builder(MainActivity.this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(Wearable.API)
                .build();
        final WatchViewStub stub = (WatchViewStub) findViewById(R.id.watch_view_stub);
        stub.setOnLayoutInflatedListener(new WatchViewStub.OnLayoutInflatedListener() {
            @Override
            public void onLayoutInflated(WatchViewStub stub) {
                btnSend = (Button) stub.findViewById(R.id.btn_send);
                btnSend.setOnClickListener(MainActivity.this);
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        mGoogleApiClient.connect();
    }

    @Override
    public void onClick(View v) {
        sendPositionData(15,20);
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

}

