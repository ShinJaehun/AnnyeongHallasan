package com.shinjaehun.annyeonghallasan;

import android.content.ContentValues;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.shinjaehun.annyeonghallasan.data.HallasanContract;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Calendar;

/**
 * Created by shinjaehun on 2017-05-28.
 */

public class FetchRoadTask extends AsyncTask<Object, Void, Void> {

    private final String LOG_TAG = FetchRoadTask.class.getSimpleName();

    private final Context mContext;
    private final Calendar mCalendar;
    private String mTimeStamp;
    private boolean isDebugging;

    public FetchRoadTask(Context context, Calendar calendar, boolean isDebugging) {
        mContext = context;
        mCalendar = calendar;
        mTimeStamp = new SimpleDateFormat("yyyyMMddHHmm").format(calendar.getTime());
        this.isDebugging = isDebugging;
        Log.v(LOG_TAG, "현재시간" + " " + mTimeStamp);
    }

    @Override
    protected Void doInBackground(Object... params) {


        try {
            Document doc = null;
            String url = "http://www.jjpolice.go.kr/jjpolice/police25/traffic.htm?act=rss";

            if (isDebugging) {
                //XML 파일로 테스트하기
                InputStream inputStream = mContext.getResources().openRawResource(R.raw.sample_data1);
                try {
                    doc = Jsoup.parse(inputStream, "UTF-8", url);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } else {
                //실제 URL로 테스트하기
                try {
                    doc = Jsoup.connect(url).get();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            Elements dateData = doc.select("date");
            Elements roadTitleData = doc.select("title");
            Elements roadDescriptionData = doc.select("description");

            String base_date = dateData.get(0).text().toString().trim();

            ContentValues roadValues = new ContentValues();
//        ArrayList<Road> roads = new ArrayList<>();
            //제주지방경찰청에서 제공하는 13개의 도로 DATA

            for (int i = 1; i < 14; i++) {

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
                roadValues.put(HallasanContract.RoadEntry.COLUMN_TIMESTAMP, new SimpleDateFormat("yyyyMMddHHmm").format(mCalendar.getTime()));
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

                if (roadValues.size() > 0) {
                    mContext.getContentResolver().insert(HallasanContract.RoadEntry.CONTENT_URI, roadValues);
                }
            }
        } catch (RuntimeException e) {
            Log.e(LOG_TAG, e.getMessage(), e);
            e.printStackTrace();
        }


        return null;
    }
}
