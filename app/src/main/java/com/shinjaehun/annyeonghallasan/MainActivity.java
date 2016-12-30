package com.shinjaehun.annyeonghallasan;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    String TAG = MainActivity.class.getSimpleName();

//    ArrayList<String> roads = new ArrayList<>();
//    ArrayList<String> status = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button fetchBT = (Button)findViewById(R.id.fetch);

        fetchBT.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new FetchData(MainActivity.this).execute();
                //context에 this를 넣으면 View.OnClickListener가 넘어감
                //getApplicationContext()를 넣으면 Activity Context가 아니라 Application Context가 넘어감

//                StringBuilder sb1 = new StringBuilder();
//
//                for (int i = 0; i < roads.size(); i++) {
//                    sb1.append(i + " " + roads.get(i) + "\n");
//                }
//
//                Log.v(TAG, "Roads SB : " + sb1.toString());
//
//                roadsTV.setText(sb1.toString());
//
//                StringBuilder sb2 = new StringBuilder();
//                for (int i = 0; i < status.size(); i++) {
//                    sb2.append(i + " " + status.get(i) + "\n");
//                }
//
//                statusTV.setText(sb2.toString());


            }
        });



    }





//    private static String getByteString(String s, int startIdx, int bytes) {
//        return new String(s.getBytes(), startIdx, bytes);
//    }
}
