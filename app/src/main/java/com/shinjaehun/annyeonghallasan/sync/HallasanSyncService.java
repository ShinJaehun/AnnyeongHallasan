package com.shinjaehun.annyeonghallasan.sync;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

/**
 * Created by shinjaehun on 2017-05-27.
 */

public class HallasanSyncService extends Service {
    private final String LOG_TAG = HallasanSyncService.class.getSimpleName();

    private static final Object wSyncAdapterLock = new Object();
    private static HallasanSyncAdapter wSyncAdapter = null;

    @Override
    public void onCreate() {
        Log.v(LOG_TAG, "SyncService Called!");

        synchronized (wSyncAdapterLock) {
            if (wSyncAdapter == null) {
                wSyncAdapter = new HallasanSyncAdapter(getApplicationContext(), true);
            }
        }

    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return wSyncAdapter.getSyncAdapterBinder();
    }
}
