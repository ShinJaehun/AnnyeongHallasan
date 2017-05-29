package com.shinjaehun.annyeonghallasan;

import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class MainActivity extends AppCompatActivity {

    String LOG_TAG = MainActivity.class.getSimpleName();
    public static final String TIME_STAMP = "time_stamp";

    public static Calendar mCalendar;
    public static boolean isDebugging = false;
//    private static SharedPreferences timeStampPF;

//    ArrayList<String> roads = new ArrayList<>();
//    ArrayList<String> status = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mCalendar = Calendar.getInstance();

//        timeStampPF = PreferenceManager.getDefaultSharedPreferences(this);
//
//        if (timeStampPF.getString(TIME_STAMP, null) == null) {
//            SharedPreferences.Editor editor = timeStampPF.edit();
//            editor.putString(TIME_STAMP, new SimpleDateFormat("yyyyMMddHHmm").format(mCalendar.getTime()));
//            editor.commit();
//        }

        setContentView(R.layout.activity_main);

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
//        HallasanSyncAdapter.syncImmediately(getApplicationContext(), Calendar.getInstance(), isDebugging);
//
//        new FetchRoadStatusTask(MainActivity.this, calendar, isDebugging).execute();
//        new FetchWeatherTask(MainActivity.this, calendar).execute();
    }

    @Override
    protected void onPause() {
//        FetchRoadStatusTask.cleanMap();
        //계속 깜빡이는 것도 나쁘진 않아 보여;;;
        super.onPause();
    }

//    @Override
//    protected void onDestroy() {
//        SharedPreferences.Editor editor = timeStampPF.edit();
//        editor.clear();
//        editor.commit();
//
//        super.onDestroy();
//    }

    //    private static String getByteString(String s, int startIdx, int bytes) {
//        return new String(s.getBytes(), startIdx, bytes);
//    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
//        return super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }



    @Override
    public boolean onOptionsItemSelected(MenuItem item)     {
        Calendar calendar = Calendar.getInstance();
        switch (item.getItemId()) {
            case R.id.action_debug:
//                FetchRoadStatusTask.cleanMap();
                isDebugging = true;
//                new FetchRoadStatusTask(MainActivity.this, calendar, isDebugging).execute();
//                new FetchWeatherTask(MainActivity.this, calendar).execute();

//                Fragment fragment = null;
//                fragment = getSupportFragmentManager().findFragmentById(R.id.roadFragment);
//                final FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
//                ft.detach(fragment);
//                ft.attach(fragment);
//                ft.commit();
// Fragment만 변경하면 될 줄 알았는데 잘 안 된다... 바로 반영 안 됨

                return true;

            case R.id.action_fetch:
//                FetchRoadStatusTask.cleanMap();

                isDebugging = false;
//                new FetchRoadStatusTask(MainActivity.this, calendar, isDebugging).execute();
//                new FetchWeatherTask(MainActivity.this, calendar).execute();

                return true;

        }
        return super.onOptionsItemSelected(item);
    }
}
