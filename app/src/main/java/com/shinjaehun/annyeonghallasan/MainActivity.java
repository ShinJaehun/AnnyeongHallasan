package com.shinjaehun.annyeonghallasan;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.shinjaehun.annyeonghallasan.sync.HallasanSyncAdapter;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class MainActivity extends AppCompatActivity {

    public static final String TIME_STAMP = "time_stamp";
    public static Calendar mCalendar;
    public static String mTimeStamp;
    //    public static boolean isDebugging = false;
    private static SharedPreferences timePrefs;
    String LOG_TAG = MainActivity.class.getSimpleName();

//    ArrayList<String> roads = new ArrayList<>();
//    ArrayList<String> status = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mCalendar = Calendar.getInstance();
        mTimeStamp = new SimpleDateFormat("yyyyMMddHHmm").format(mCalendar.getTime());


        timePrefs = PreferenceManager.getDefaultSharedPreferences(this);
        String oldTimeStamp = timePrefs.getString(MainActivity.TIME_STAMP, null);

        if (!mTimeStamp.equals(oldTimeStamp) || oldTimeStamp == null) {
            Log.v(LOG_TAG, "Sync 합니다!!!!!! : 현재 타임스탬프는 " + mTimeStamp + " 예전 타임스탬프는 " + oldTimeStamp);

            Boolean isDebugging = false;
            HallasanSyncAdapter.syncImmediately(getApplicationContext(), Calendar.getInstance(), isDebugging);

            SharedPreferences.Editor editor = timePrefs.edit();
            editor.putString(TIME_STAMP, mTimeStamp);
            editor.commit();

        } else {
            Log.v(LOG_TAG, "Sync는 이루어지지 않습니다 : 현재 타임스탬프는 " + mTimeStamp + " 예전 타임스탬프는 " + oldTimeStamp);
        }

        RoadFragment roadFragment = new RoadFragment();
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.roadFragment, roadFragment);
        WeatherFragment weatherFragment = new WeatherFragment();
        fragmentTransaction.replace(R.id.weatherFragment, weatherFragment);
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
    protected void onStart() {
        super.onStart();

//
//        new FetchRoadStatusTask(MainActivity.this, calendar, isDebugging).execute();
//        new FetchWeatherTask(MainActivity.this, calendar).execute();
//        new FetchRoadTask(getApplicationContext(), mCalendar, isDebugging, )
    }

    @Override
    protected void onResume() {
        super.onResume();


    }

    @Override
    protected void onPause() {
//        FetchRoadStatusTask.cleanMap();
        //계속 깜빡이는 것도 나쁘진 않아 보여;;;
        super.onPause();
    }

//    @Override
//    protected void onDestroy() {
//        super.onDestroy();
//
////        SharedPreferences.Editor editor = timePrefs.edit();
////        editor.clear();
////        editor.commit();
//
//    }

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
//        Calendar calendar = Calendar.getInstance();
//        Intent intent = getIntent();
//        String mainTimeStamp = new SimpleDateFormat("yyyyMMddHHmm").format(mCalendar.getTime());
//        String rightNow = new SimpleDateFormat("yyyyMMddHHmm").format(calendar.getTime());
//
//        switch (item.getItemId()) {
//            case R.id.action_debug:
////                FetchRoadStatusTask.cleanMap();
//                isDebugging = true;
////                new FetchRoadTask(getApplicationContext(), calendar, isDebugging).execute();
////                new FetchWeatherTask(getApplicationContext(), calendar).execute();
//
//                if (mainTimeStamp.equals(rightNow)) {
//                    Toast.makeText(this, "디버깅은 1분 후에 가능!", Toast.LENGTH_SHORT).show();
//                } else {
////                    finish();
////                    startActivity(intent);
//
////                    RoadFragment roadFragment = new RoadFragment();
////                    FragmentManager fragmentManager = getSupportFragmentManager();
////                    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
////                    fragmentTransaction.replace(R.id.roadFragment, roadFragment);
////                    fragmentTransaction.commit();
//                }
////                Fragment fragment = getSupportFragmentManager().findFragmentById(R.id.roadFragment);
////                final FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
////                ft.detach(fragment);
////                ft.attach(fragment);
////                ft.commit();
//// Fragment만 변경하면 될 줄 알았는데 잘 안 된다... 바로 반영 안 됨 -> AsyncTask 인스턴스는 오직 한번만 호출되기 때문!
//
//                return true;
//
//            case R.id.action_fetch:
////                FetchRoadStatusTask.cleanMap();
//
//                isDebugging = false;
////                new FetchRoadStatusTask(MainActivity.this, calendar, isDebugging).execute();
////                new FetchWeatherTask(MainActivity.this, calendar).execute();
//
//                if (mainTimeStamp.equals(rightNow)) {
//                    Toast.makeText(this, "디버깅은 1분 후에 가능!", Toast.LENGTH_SHORT).show();
//                } else {
////                    finish();
////                    startActivity(intent);
////                    RoadFragment roadFragment = new RoadFragment();
////                    FragmentManager fragmentManager = getSupportFragmentManager();
////                    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
////                    fragmentTransaction.replace(R.id.roadFragment, roadFragment);
////                    fragmentTransaction.commit();
//                }
//                return true;
//
//        }
//        return super.onOptionsItemSelected(item);
//    }
}
