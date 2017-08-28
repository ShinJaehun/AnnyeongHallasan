package com.shinjaehun.annyeonghallasan;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.shinjaehun.annyeonghallasan.sync.HallasanSyncAdapter;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class MainActivity extends AppCompatActivity {

    String LOG_TAG = MainActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Boolean isDebugging = false;
        Calendar calendar = Calendar.getInstance();
        int month = Integer.parseInt(new SimpleDateFormat("MM").format(calendar.getTime()));

        HallasanSyncAdapter.initializeSyncAdapter(this, isDebugging, month);
        //month를 syncadapter로 넘기는 이유는 4월 ~ 11월 중에 roadProcess()를 실행시키지 않기 위함

        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        if(!isNetworkAvailable(this)) {
            //네트워크 상태에 따라 app 실행을 제한함
            TextView errorNetworkConnectionTV = (TextView)findViewById(R.id.text_network_error);
            errorNetworkConnectionTV.setVisibility(View.VISIBLE);
        } else {
            RoadFragment roadFragment = RoadFragment.newInstance(isDebugging, month);
            fragmentTransaction.replace(R.id.fragment_road, roadFragment);
            WeatherFragment weatherFragment = new WeatherFragment();
            fragmentTransaction.replace(R.id.fragment_weather, weatherFragment);
            //각 Fragment가 Loader로 동작하기 위해서는 xml 형태로만 로딩되면 안 되고 java 파일을 읽어야 함
        }
        fragmentTransaction.commit();

    }

    @Override
    protected void onResume() {
        super.onResume();

        //마지막으로 sync한 뒤 30분이 경과해야 onResume()에서 다시 sync할 수 있도록 제한함
        //경과한 시간이 30분이 되지 않으면 DB에 push할 수 없음
        SharedPreferences prefs = getSharedPreferences(getString(R.string.pref_last_sync_time), Activity.MODE_PRIVATE);
        String lastSyncTimeKey = getString(R.string.pref_last_sync_time_key);
        long lastSyncTime = prefs.getLong(lastSyncTimeKey, 0);

//        Log.v(LOG_TAG, "Last Sync Time : " + new SimpleDateFormat("yyyyMMddHHmm").format(new Date(lastSyncTime)));

        Calendar calendar = Calendar.getInstance();

        long now = calendar.getTime().getTime();

//        Log.v(LOG_TAG, "now : " + new SimpleDateFormat("yyyyMMddHHmm").format(new Date(now)));

        long min = (now - lastSyncTime) / 60000;

//        Log.v(LOG_TAG, "MIN : "+ min);

        if (lastSyncTime == 0 || min >= 30) {
            //경과한 시간이 30분이 넘었을 때는 현재 시각을 SharedPreferences에 남기고 DB에 push
            SharedPreferences.Editor editor = prefs.edit();
            editor.putLong(lastSyncTimeKey, now);
            editor.commit();

            HallasanSyncAdapter.syncImmediately(this);
            //syncadapter가 정상적으로 동작한다면 굳이 Activity가 화면에 보여질때 syncImmediately()할 필요가 없다.
            //삼성 device의 문제
        }
    }

    static public boolean isNetworkAvailable(Context c) {
        //네트워크 상태를 감지해서 알려줌
        ConnectivityManager cm = (ConnectivityManager)c.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        return activeNetwork != null && activeNetwork.isConnectedOrConnecting();
    }
}
