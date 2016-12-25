package com.shinjaehun.annyeonghallasan;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    String TAG = MainActivity.class.getSimpleName();

    String url = "http://www.jjpolice.go.kr/jjpolice/police25/traffic.htm?act=rss";
    ArrayList<String> roads = new ArrayList<>();
    ArrayList<String> status = new ArrayList<>();

    ArrayList<RoadStatus> roadStatuses;

    TextView roadsTV;
    TextView statusTV;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);



        roadsTV = (TextView)findViewById(R.id.roads);
        statusTV = (TextView)findViewById(R.id.status);

        Button fetchBT = (Button)findViewById(R.id.fetch);

        fetchBT.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new fetchData().execute();
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

                if (roadStatuses != null) {
                    StringBuilder sb = new StringBuilder();
                    for (RoadStatus rs : roadStatuses) {
                        sb.append(rs.getName() + " : " + rs.getDescription() + "\n");
                    }
                    statusTV.setText(sb.toString());
                }
            }
        });



    }

    private class fetchData extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... voids) {


            Document doc = null;
            try {
                doc = Jsoup.connect(url).get();
            } catch (IOException e) {
                e.printStackTrace();
            }

            if (roadStatuses == null) {
                Elements dateData = doc.select("date");
//            if (!dateData.get(0).equals(roadStatuses.get(0).getDate())) {
                roadStatuses = new ArrayList<>();

                for (int i = 0; i < 10; i++) {
                    //10개의 도로
                    Elements roadsData = doc.select("title");
                    Elements statusData = doc.select("description");

                    roadStatuses.add(new RoadStatus(roadsData.get(i + 1).text().toString().trim(), statusData.get(i + 1).text().replaceAll("&nbsp;", "").toString().trim(), dateData.get(0).text().toString().trim()));
                    //도로명, description, 발표시간을 생성자로 하여 RoadStatus 생성
                }

//                Elements roadsData = doc.select("title");
//                for (int i = 0; i < roadsData.size(); i++) {
////                roads.add(roadsData.get(i).text().toString().trim());
////                Log.v(TAG, "Road : " + i + " " + roadsData.get(i).text().toString().trim());
//                }
//
//                Elements statusData = doc.select("description");
//                for (int i = 0; i < statusData.size(); i++) {
////                status.add(statusData.get(i).text().replaceAll("&nbsp;","").toString().trim());
////                Log.v(TAG, "Status : " + i + " " + statusData.get(i).text().replaceAll("&nbsp;","").toString().trim());
//                }
//
                Log.v(TAG, "새로 생성함");

                for (RoadStatus rs : roadStatuses) {
                    Log.v(TAG, rs.getName() + " : " + rs.getDescription() + " : " + rs.getDate());
                }

            }

            Log.v(TAG, "전과 동일함");




//            for ( int i = 0; i < status.size(); i++) {
//                Log.v(TAG, getByteString(status.get(i), 9, 6));
//            }


            return null;
        }
    }



//    private static String getByteString(String s, int startIdx, int bytes) {
//        return new String(s.getBytes(), startIdx, bytes);
//    }
}
