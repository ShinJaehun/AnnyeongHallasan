package com.shinjaehun.annyeonghallasan.sync;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;

/**
 * Created by shinjaehun on 2017-05-27.
 */

public class WeatherAuthenticatorService extends Service {

    private WeatherAuthenticator mAuthenticator;

    @Override
    public void onCreate() {
        mAuthenticator = new WeatherAuthenticator(this);
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return mAuthenticator.getIBinder();
    }
}
