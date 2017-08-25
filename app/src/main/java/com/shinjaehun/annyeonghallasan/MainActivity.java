package com.shinjaehun.annyeonghallasan;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.shinjaehun.annyeonghallasan.sync.HallasanSyncAdapter;

import org.w3c.dom.Text;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class MainActivity extends AppCompatActivity implements SharedPreferences.OnSharedPreferenceChangeListener {

//    public static final String TIME_STAMP = "time_stamp";
//    public static String mTimeStamp;
//    //    public static boolean isDebugging = false;
//    private static SharedPreferences timePrefs;
    String LOG_TAG = MainActivity.class.getSimpleName();

//    ArrayList<String> roads = new ArrayList<>();
//    ArrayList<String> status = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

//        Calendar calendar = Calendar.getInstance();
//        mTimeStamp = new SimpleDateFormat("yyyyMMddHHmm").format(calendar.getTime());

//        timePrefs = PreferenceManager.getDefaultSharedPreferences(this);
//        String oldTimeStamp = timePrefs.getString(MainActivity.TIME_STAMP, null);
//
//        if (!mTimeStamp.equals(oldTimeStamp) || oldTimeStamp == null) {
//            Log.v(LOG_TAG, "Sync 합니다!!!!!! : 현재 타임스탬프는 " + mTimeStamp + " 예전 타임스탬프는 " + oldTimeStamp);
//
//            Boolean isDebugging = false;
//            HallasanSyncAdapter.syncImmediately(this, calendar, isDebugging);
//
//            SharedPreferences.Editor editor = timePrefs.edit();
//            editor.putString(TIME_STAMP, mTimeStamp);
//            editor.commit();
//
//        } else {
//            Log.v(LOG_TAG, "Sync는 이루어지지 않습니다 : 현재 타임스탬프는 " + mTimeStamp + " 예전 타임스탬프는 " + oldTimeStamp);
//        }

        Boolean isDebugging = false;

        Calendar calendar = Calendar.getInstance();
        int month = Integer.parseInt(new SimpleDateFormat("MM").format(calendar.getTime()));

        HallasanSyncAdapter.initializeSyncAdapter(this, isDebugging, month);

        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        RoadFragment roadFragment = RoadFragment.newInstance(isDebugging, month);
        fragmentTransaction.replace(R.id.roadFragment, roadFragment);
        WeatherFragment weatherFragment = new WeatherFragment();
        fragmentTransaction.replace(R.id.weatherFragment, weatherFragment);

//        if(!isNetworkAvailable(this)) {
//            //Network connection 관련한 오류 점검인데 updateErrorView를 onSharedPreferenceChanged에 다는 형식으로 수정할 수 있겠다는 생각이 든다.
//            TextView errorNetworkConnectionTV = (TextView)findViewById(R.id.text_network_error);
//            errorNetworkConnectionTV.setVisibility(View.VISIBLE);
//        } else {
//            RoadFragment roadFragment = RoadFragment.newInstance(isDebugging, month);
//            fragmentTransaction.replace(R.id.roadFragment, roadFragment);
//            WeatherFragment weatherFragment = new WeatherFragment();
//            fragmentTransaction.replace(R.id.weatherFragment, weatherFragment);
//        }
        fragmentTransaction.commit();


//        HallasanSyncAdapter.initalizeSyncAdapter(this);

//        Button fetchBT = (Button)findViewById(R.id.fetch);

//        fetchBT.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//
//                new FetchRoadStatusTask(MainActivity.this, isDebugging).execute();
//                //context에 this를 넣으면 View.OnClickListener가 넘어감
//                //getApplicationContext()를 넣으면 Activity Context가 아니라 Application Context가 넘어감
//
////                StringBuilder sb1 = new StringBuilder();
////
////                for (int i = 0; i < roads.size(); i++) {
////                    sb1.append(i + " " + roads.get(i) + "\n");
////                }
////
////                Log.v(LOG_TAG, "Roads SB : " + sb1.toString());
////
////                roadsTV.setText(sb1.toString());
////
////                StringBuilder sb2 = new StringBuilder();
////                for (int i = 0; i < status.size(); i++) {
////                    sb2.append(i + " " + status.get(i) + "\n");
////                }
////
////                statusTV.setText(sb2.toString());
//
//
//            }
//        });

    }

    @Override
    protected void onResume() {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        prefs.registerOnSharedPreferenceChangeListener(this);
        super.onResume();


//        SharedPreferences prefsSyncTime = getSharedPreferences(getString(R.string.pref_last_sync_time), Activity.MODE_PRIVATE);
//        String lastSyncTimeKey = getString(R.string.pref_last_sync_time_key);
//        long lastSyncTime = prefsSyncTime.getLong(lastSyncTimeKey, 0);
//
//        Log.v(LOG_TAG, "Last Sync Time : " + new SimpleDateFormat("yyyyMMddHHmm").format(new Date(lastSyncTime)));
//
//        Calendar calendar = Calendar.getInstance();
//
//        long now = calendar.getTime().getTime();
//
//        Log.v(LOG_TAG, "now : " + new SimpleDateFormat("yyyyMMddHHmm").format(new Date(now)));
//
//        long min = (now - lastSyncTime) / 60000;
//
//        Log.v(LOG_TAG, "MIN : "+ min);
//
//        if (lastSyncTime == 0 || min >= 30) {
//            SharedPreferences.Editor editor = prefsSyncTime.edit();
//            editor.putLong(lastSyncTimeKey, now);
//            editor.commit();
//
//            HallasanSyncAdapter.syncImmediately(this);
//        }
    }

    @Override
    protected void onPause() {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        prefs.unregisterOnSharedPreferenceChangeListener(this);
        super.onPause();
    }

    static public boolean isNetworkAvailable(Context c) {
        ConnectivityManager cm = (ConnectivityManager)c.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        return activeNetwork != null && activeNetwork.isConnectedOrConnecting();
    }

    private void updateErrorMessage() {
        TextView errorNetworkTV = (TextView)findViewById(R.id.text_network_error);
        errorNetworkTV.setVisibility(View.VISIBLE);

        int message = R.string.msg_server_error;

        SharedPreferences prefs = getSharedPreferences(getString(R.string.pref_server_status), Activity.MODE_PRIVATE);
        @HallasanSyncAdapter.ServerStatus int serverStatus = prefs.getInt(getString(R.string.pref_server_status_key), HallasanSyncAdapter.SERVER_STATUS_UNKNOWN);
        switch (serverStatus) {
            case HallasanSyncAdapter.WEATHER_STATUS_SERVER_DOWN:
                message = R.string.msg_weather_server_down;
                break;
            case HallasanSyncAdapter.WEATHER_STATUS_SERVER_INVALID:
                message = R.string.msg_weather_server_error;
                break;
            case HallasanSyncAdapter.ROAD_STATUS_SERVER_DOWN:
                message = R.string.msg_road_server_down;
                break;
            case HallasanSyncAdapter.ROAD_STATUS_SERVER_INVALID:
                message = R.string.msg_road_server_error;
                break;
            default:
                if (!isNetworkAvailable(this)) {
                    message = R.string.msg_server_not_connected;
                }
        }
        errorNetworkTV.setText(message);

    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String s) {
        if (s.equals(getString(R.string.pref_server_status_key))) {
            updateErrorMessage();
        }
    }

//    @Override
//    protected void onStart() {
//        super.onStart();
//
////
////        new FetchRoadStatusTask(MainActivity.this, calendar, isDebugging).execute();
////        new FetchWeatherTask(MainActivity.this, calendar).execute();
////        new FetchRoadTask(getApplicationContext(), mCalendar, isDebugging, )
//    }
//
//    @Override
//    protected void onResume() {
//        super.onResume();
//
//
//    }
//
//    @Override
//    protected void onPause() {
////        FetchRoadStatusTask.cleanMap();
//        //계속 깜빡이는 것도 나쁘진 않아 보여;;;
//        super.onPause();
//    }
//
////    @Override
////    protected void onDestroy() {
////        super.onDestroy();
////
//////        SharedPreferences.Editor editor = timePrefs.edit();
//////        editor.clear();
//////        editor.commit();
////
////    }
//
//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
////        return super.onCreateOptionsMenu(menu);
//        getMenuInflater().inflate(R.menu.menu_main, menu);
//        return true;
//    }
//
//
//
//    @Override
//    public boolean onOptionsItemSelected(MenuItem item)     {
//
//        Boolean isDebugging = false;
//        switch (item.getItemId()) {
//            case R.id.action_debug:
//
//                isDebugging = true;
//                break;
//            case R.id.action_fetch:
//                isDebugging = false;
//                break;
//        }
//
////        HallasanSyncAdapter.syncImmediately(this, isDebugging);
//
////        RoadFragment rf = (RoadFragment)getSupportFragmentManager().findFragmentById(R.id.roadFragment);
////        if (rf != null) {
////            rf.timeStampChanged();
////        }
////
////        WeatherFragment wf = (WeatherFragment)getSupportFragmentManager().findFragmentById(R.id.weatherFragment);
////        if (wf != null) {
////            wf.timeStampChanged();
////        }
//
////        if (!oldTimeStamp.equals(rightNow) || oldTimeStamp == null) {
////            Log.v(LOG_TAG, "Menu에서 Sync 합니다!!!!!! : 현재 타임스탬프는 " + rightNow + " 예전 타임스탬프는 " + oldTimeStamp);
////
////
////
////        } else {
////            Log.v(LOG_TAG, "Menu에서 Sync는 이루어지지 않았습니다 : 현재 타임스탬프는 " + rightNow + " 예전 타임스탬프는 " + oldTimeStamp);
////        }
//        return super.onOptionsItemSelected(item);
//    }
}
