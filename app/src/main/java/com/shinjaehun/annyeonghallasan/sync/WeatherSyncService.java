package com.shinjaehun.annyeonghallasan.sync;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;

/**
 * Created by shinjaehun on 2017-05-27.
 */

public class WeatherSyncService extends Service {
    private static final Object wSyncAdapterLock = new Object();
    private static WeatherSyncAdapter wSyncAdapter = null;

    @Override
    public void onCreate() {
        synchronized (wSyncAdapterLock) {
            if (wSyncAdapter == null) {
                wSyncAdapter = new WeatherSyncAdapter(getApplicationContext(), true);
            }
        }

    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return wSyncAdapter.getSyncAdapterBinder();
    }
}
