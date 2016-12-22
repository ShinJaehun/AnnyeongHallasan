package com.shinjaehun.annyeonghallasan;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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
    String url = "http://www.jjpolice.go.kr/jjpolice/police25/traffic.htm";
    ArrayList<String> roads = new ArrayList<>();
    ArrayList<String> status = new ArrayList<>();

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
                StringBuilder sb1 = new StringBuilder();

                for (int i = 0; i < roads.size(); i++) {
                    sb1.append(i + " " + roads.get(i) + "\n");
                }

                roadsTV.setText(sb1.toString());

                StringBuilder sb2 = new StringBuilder();
                for (int i = 0; i < status.size(); i++) {
                    sb2.append(i + " " + status.get(i) + "\n");
                }

                statusTV.setText(sb2.toString());
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

//        for (Element roadsData : doc.select("tr th")) {
//            roads.add(String.valueOf(roadsData));
//        }

            Elements roadsData = doc.select("tr th");
            for (int i = 7; i < roadsData.size(); i++) {
                roads.add(String.valueOf(roadsData.get(i).text()));
            }

            Elements statusData = doc.select("tr td");
            for (int i = 0; i < statusData.size(); i++) {
                if (String.valueOf(statusData.get(i).text()).equals("정상")) {
                    status.add("정상");
                }
            }
            return null;
        }
    }
}
