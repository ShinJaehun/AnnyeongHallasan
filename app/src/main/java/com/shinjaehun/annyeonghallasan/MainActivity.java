package com.shinjaehun.annyeonghallasan;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    String TAG = MainActivity.class.getSimpleName();
    boolean isDebugging = false;

//    ArrayList<String> roads = new ArrayList<>();
//    ArrayList<String> status = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

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
////                Log.v(TAG, "Roads SB : " + sb1.toString());
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
    protected void onPause() {
        FetchRoadStatusTask.cleanMap();
        //계속 깜빡이는 것도 나쁘진 않아 보여;;;

        super.onPause();
    }

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
        switch (item.getItemId()) {
            case R.id.action_debug:
                FetchRoadStatusTask.cleanMap();

                isDebugging = true;
                new FetchRoadStatusTask(MainActivity.this, isDebugging).execute();
                new FetchWeatherTask(MainActivity.this).execute();

                return true;

            case R.id.action_fetch:
                FetchRoadStatusTask.cleanMap();

                isDebugging = false;
                new FetchRoadStatusTask(MainActivity.this, isDebugging).execute();
                new FetchWeatherTask(MainActivity.this).execute();

                return true;

        }
        return super.onOptionsItemSelected(item);
    }
}
