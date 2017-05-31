package com.shinjaehun.annyeonghallasan;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;

import com.shinjaehun.annyeonghallasan.FetchRoadTask;
import com.shinjaehun.annyeonghallasan.R;
import com.shinjaehun.annyeonghallasan.RoadFragment;
import com.shinjaehun.annyeonghallasan.data.HallasanContract;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Vector;

/**
 * Created by shinjaehun on 2017-06-01.
 */

public class FetchRoadDebuggingTask extends AsyncTask<Void, Void, Void> {

    private final String LOG_TAG = FetchRoadDebuggingTask.class.getSimpleName();

    private final Context mContext;
    private final Calendar mCalendar;
    private final String mTimeStamp;


    public FetchRoadDebuggingTask(Context context, Calendar calendar) {
        mContext = context;
        mCalendar = calendar;

        mTimeStamp = new SimpleDateFormat("yyyyMMddHHmm").format(mCalendar.getTime());
    }

    @Override
    protected Void doInBackground(Void... voids) {
        boolean inserted = false;
        String sortOrder = HallasanContract.RoadEntry._ID + " DESC";
        Uri roadWithDateUri = HallasanContract.RoadEntry.buildRoadUriWithDate(mTimeStamp);
        Cursor cursor = mContext.getContentResolver().query(roadWithDateUri, null, null, null, sortOrder);
        // 일단 MainActivity의 timeStamp로 query

        if (cursor.moveToFirst()) {
            do {
                String insertedTimeStamp = cursor.getString(RoadFragment.COL_ROAD_TIMESTAMP);
                if (mTimeStamp.equals(insertedTimeStamp))
                    inserted = true;
                //이전에 insert된 값이 존재하는가?
            } while (cursor.moveToNext());
        }

        if (!inserted) {
            //이전에 insert된 값이 없다면 일단 fetch
            Vector<ContentValues> cVVector = new Vector<ContentValues>(13);

            try {
                Document doc = null;
                String url = "http://www.jjpolice.go.kr/jjpolice/police25/traffic.htm?act=rss";

                    //XML 파일로 테스트하기
                    InputStream inputStream = mContext.getResources().openRawResource(R.raw.sample_data2);
                    try {
                        doc = Jsoup.parse(inputStream, "UTF-8", url);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }


                Elements dateData = doc.select("date");
                Elements roadTitleData = doc.select("title");
                Elements roadDescriptionData = doc.select("description");

                String base_date = dateData.get(0).text().toString().trim();

                ContentValues roadValues;
//        ArrayList<Road> roads = new ArrayList<>();
                //제주지방경찰청에서 제공하는 13개의 도로 DATA

                for (int i = 1; i < 14; i++) {
                    roadValues = new ContentValues();
                    String n = roadTitleData.get(i).text().replace("<![CDATA[", "").replace("]]>", "").toString().trim();
                    //정말 귀찮은데 도로명에 <![CDATA[~~~]> 이거 붙는 거 없애고

                    String name;
                    if (n.matches(".*[0-9]\\)$")) {
                        //"숫자)"로 끝나는 문자열이라면
                        name = n.substring(0, n.indexOf("("));
                        // 도로명 "1100도로(1139)"에서 도로 번호를 생략

                    } else {
                        name = n;
                        // 도로번호가 없는 도로명은 그대로 쓰기
                    }

                    roadValues.put(HallasanContract.RoadEntry.COLUMN_NAME, name);
                    roadValues.put(HallasanContract.RoadEntry.COLUMN_TIMESTAMP, mTimeStamp);
                    roadValues.put(HallasanContract.RoadEntry.COLUMN_BASE_DATE, base_date);

                    String description = roadDescriptionData.get(i).text().replaceAll("&nbsp;", "").replaceAll("&amp;nbsp;", "").toString().trim();
                    //description에 nbsp랑 amp 붙는거 너무 짜증나!

                    String[] v = description.split(":", -6);

//                    Log.v(LOG_TAG, location + " 크기는 " + v.length);
//                    Log.v(LOG_TAG, location + "," + v[0].trim() );
//                    Log.v(LOG_TAG, location + "," + v[1].trim() );
//                    Log.v(LOG_TAG, location + "," + v[2].trim() );
//                    Log.v(LOG_TAG, location + "," + v[3].trim() );
//                    Log.v(LOG_TAG, location + "," + v[4].trim() );
//                    Log.v(LOG_TAG, location + "," + v[5].trim() );
/*
                    통제하는 경우 description 내용
                    구간 : 제주대 입구 ~ 성판악 적설 : 1 결빙 : 대형 통재상항 :    소형 통재상항 : 체인

                    통제하지 않는 경우 descrition 내용
                    구간 : 정상 적설 : 결빙 : 대형 통재상항 :    소형 통재상항 :
*/
                    int restrict = HallasanContract.RoadEntry.RESTRICTION_DISABLED;
                    String section = "정상";
                    if (!v[1].contains("정상")) {
                        //통제하는 경우
                        restrict = HallasanContract.RoadEntry.RESTRICTION_ENABLED;
                        section = v[1].substring(0, v[1].indexOf("적설")).trim();
                        //통제구간 : "제주대 입구 ~ 성판악"
                    }
                    roadValues.put(HallasanContract.RoadEntry.COLUMN_RESTRICTION, restrict);
                    roadValues.put(HallasanContract.RoadEntry.COLUMN_SECTION, section);

                    Float snowfall = 0f;
                    if (v[2].matches(".*\\d+.*")) {
                        snowfall = Float.parseFloat(v[2].substring(0, v[2].indexOf("결빙")).trim());
                        //적설
                    }
                    roadValues.put(HallasanContract.RoadEntry.COLUMN_SNOWFALL, snowfall);

                    Float freezing = 0f;
                    if (v[3].matches(".*\\d+.*")) {
                        freezing = Float.parseFloat(v[3].substring(0, v[3].indexOf("대형")).trim());
                        //결빙
                    }
                    roadValues.put(HallasanContract.RoadEntry.COLUMN_FREEZING, freezing);

                    int chain = HallasanContract.RoadEntry.CHAIN_NONE;
                    if (v[4].contains("체인")) {
                        chain = HallasanContract.RoadEntry.CHAIN_SMALL;
                    }
                    if (v[5].contains("체인")) {
                        chain = HallasanContract.RoadEntry.CHAIN_BIG;
                    }
                    roadValues.put(HallasanContract.RoadEntry.COLUMN_CHAIN, chain);

                    cVVector.add(roadValues);
//                        mContext.getContentResolver().insert(HallasanContract.RoadEntry.CONTENT_URI, roadValues);
                }

            } catch (RuntimeException e) {
                Log.e(LOG_TAG, e.getMessage(), e);
                e.printStackTrace();
            }

            if (cVVector.size() > 0) {
                //fetch에 성공했으면 DB에 쓰기
                ContentValues[] cvArray = new ContentValues[cVVector.size()];
                cVVector.toArray(cvArray);
                int size = mContext.getContentResolver().bulkInsert(HallasanContract.RoadEntry.CONTENT_URI, cvArray);
                Log.v(LOG_TAG, "FetchRoadDebuggingTask에서 DB에 집어 넣은 다음 크기 " + size);

            }
        }

        return null;
    }
}
