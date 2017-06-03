package com.shinjaehun.annyeonghallasan.sync;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

/**
 * Created by shinjaehun on 2017-06-03.
 */

public class RoadSyncService extends Service {
    private final String LOG_TAG = RoadSyncService.class.getSimpleName();

    private static final Object rSyncAdapterLock = new Object();
    private static RoadSyncAdapter rSyncAdapter = null;

    @Override
    public void onCreate() {
        Log.v(LOG_TAG, "RoadSyncService Called!!");

        synchronized (rSyncAdapterLock) {
            if (rSyncAdapter == null) {
                rSyncAdapter = new RoadSyncAdapter(getApplicationContext(), true);
            }
        }
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return rSyncAdapter.getSyncAdapterBinder();
    }
}
