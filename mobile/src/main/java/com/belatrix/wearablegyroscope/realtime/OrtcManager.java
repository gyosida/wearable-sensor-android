package com.belatrix.wearablegyroscope.realtime;

import ibt.ortc.api.Ortc;
import ibt.ortc.extensibility.OrtcClient;
import ibt.ortc.extensibility.OrtcFactory;

/**
 * Created by gianfranco on 10/6/15.
 */
public class OrtcManager {

    private static OrtcManager mInstance;
    private OrtcClient client;

    private OrtcManager() {
        init();
    }

    public static OrtcManager getInstance() {
        if (mInstance == null) {
            mInstance = new OrtcManager();
        }

        return mInstance;
    }

    private void init() {
        OrtcFactory factory = null;

        Ortc ortc = new Ortc();
        try {
            factory = ortc.loadOrtcFactory("IbtRealtimeSJ");

        } catch (Exception e) {

        }

        if (factory != null) {
            client = factory.createClient();
            client.setClusterUrl("http://ortc-developers.realtime.co/server/2.1/");
            client.setConnectionMetadata("AndroidApp");
        }
    }

    public OrtcClient getOrtcClient() {
        return client;
    }

}
