package com.shinjaehun.annyeonghallasan;

import android.content.ContentValues;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.widget.ImageView;
import android.widget.TextView;

import com.shinjaehun.annyeonghallasan.data.HallasanContract;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Vector;

/**
 * Created by shinjaehun on 2017-05-28.
 */

public class FetchRoadTask extends AsyncTask<Object, Void, Void> {

    private final String LOG_TAG = FetchRoadTask.class.getSimpleName();

    private final Context mContext;
//    private final Calendar mCalendar;
    private final String mTimeStamp;
    private boolean isDebugging;

    final Animation animation;

    public FetchRoadTask(Context context, String timeStamp, Boolean isDebugging) {
        mContext = context;
//        mCalendar = calendar;
        this.isDebugging = isDebugging;
        mTimeStamp = timeStamp;
        animation = new AlphaAnimation((float) 0.5, 0);

//        mTimeStamp = new SimpleDateFormat("yyyyMMddHHmm").format(mCalendar.getTime());
//        SharedPreferences timeStampPF = PreferenceManager.getDefaultSharedPreferences(mContext);
//        mTimeStamp = timeStampPF.getString(MainActivity.TIME_STAMP, null);
    }

    @Override
    protected Void doInBackground(Object... params) {
//        boolean inserted = false;
//        String sortOrder = HallasanContract.RoadEntry._ID + " DESC";
//        Uri roadWithDateUri = HallasanContract.RoadEntry.buildRoadUriWithDate(mTimeStamp);
//        Cursor cursor = mContext.getContentResolver().query(roadWithDateUri, null, null, null, sortOrder);
//        // 일단 MainActivity의 timeStamp로 query
//
//        if (cursor.moveToFirst()) {
//            do {
//                String insertedTimeStamp = cursor.getString(RoadFragment.COL_ROAD_TIMESTAMP);
//                if (mTimeStamp.equals(insertedTimeStamp))
//                    inserted = true;
//                    //이전에 insert된 값이 존재하는가?
//            } while (cursor.moveToNext());
//        }

//        if (!inserted) {
            //이전에 insert된 값이 없다면 일단 fetch
            Vector<ContentValues> cVVector = new Vector<ContentValues>(13);

            try {
                Document doc = null;
                String url = "http://www.jjpolice.go.kr/jjpolice/police25/traffic.htm?act=rss";

                if (isDebugging) {
//                    //XML 파일로 테스트하기
                    InputStream inputStream = mContext.getResources().openRawResource(R.raw.sample_data1);
                    try {
                        doc = Jsoup.parse(inputStream, "UTF-8", url);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                } else {
//                    실제 URL로 테스트하기
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
                Log.v(LOG_TAG, "FetchRoadTask에서 " + mTimeStamp + "에 DB에 집어 넣은 다음 크기 " + size);
            }
//        }

//        Uri rUri = HallasanContract.RoadEntry.buildRoadUriWithDate(mTimeStamp);
//        Cursor c = mContext.getContentResolver().query(rUri, null, null, null, sortOrder);

//        return c;

                return null;
    }

//    @Override
//    protected void onPostExecute(Cursor cursor) {
//
//        ArrayList<ImageView> roadImgs = new ArrayList<>();
//
//        ImageView road_1100Iv;
//        ImageView road_516Iv;
//        ImageView road_pyeonghwaIv;
//        ImageView road_beonyeongIv;
//        ImageView road_hanchangIv;
//        ImageView road_namjoIv;
//        ImageView road_bijaIv;
//        ImageView road_seoseongIv;
//        ImageView road_sallok1Iv;
//        ImageView road_sallok2Iv;
//        ImageView road_myeongnimIv;
//        ImageView road_cheomdanIv;
//        ImageView road_aejoIv;
//        ImageView road_iljuIv;
//        TextView normalTV;
//
//        road_1100Iv = (ImageView)((MainActivity)mContext).findViewById(R.id.road_1100);
//        road_516Iv = (ImageView)((MainActivity)mContext).findViewById(R.id.road_516);
//        road_pyeonghwaIv = (ImageView) ((MainActivity)mContext).findViewById(R.id.road_pyeonghwa);
//        road_beonyeongIv = (ImageView) ((MainActivity)mContext).findViewById(R.id.road_beonyeong);
//        road_hanchangIv = (ImageView) ((MainActivity)mContext).findViewById(R.id.road_hanchang);
//        road_namjoIv = (ImageView) ((MainActivity)mContext).findViewById(R.id.road_namjo);
//        road_bijaIv = (ImageView) ((MainActivity)mContext).findViewById(R.id.road_bija);
//        road_seoseongIv = (ImageView) ((MainActivity)mContext).findViewById(R.id.road_seoseong);
//        road_sallok1Iv = (ImageView) ((MainActivity)mContext).findViewById(R.id.road_sallok1);
//        road_sallok2Iv = (ImageView) ((MainActivity)mContext).findViewById(R.id.road_sallok2);
//        road_myeongnimIv = (ImageView) ((MainActivity)mContext).findViewById(R.id.road_myeongnim);
//        road_cheomdanIv = (ImageView) ((MainActivity)mContext).findViewById(R.id.road_cheomdan);
//        road_aejoIv = (ImageView) ((MainActivity)mContext).findViewById(R.id.road_aejo);
//        road_iljuIv = (ImageView) ((MainActivity)mContext).findViewById(R.id.road_ilju);
//        normalTV = (TextView) ((MainActivity)mContext).findViewById(R.id.normal);
//
//        while (cursor.moveToNext()) {
//
//            String timeStamp = cursor.getString(RoadFragment.COL_ROAD_TIMESTAMP);
//
//            long roadId = cursor.getLong(RoadFragment.COL_ROAD_ID);
//            String location = cursor.getString(RoadFragment.COL_ROAD_NAME);
//
//            int restrict = cursor.getInt(RoadFragment.COL_ROAD_RESTRICTION);
//
//            Log.v(LOG_TAG, "ID : " + roadId + " 장소 : " + location + " 타임스탬프 : " + timeStamp + " 제한여부 : " + restrict);
//
//            if (restrict == HallasanContract.RoadEntry.RESTRICTION_ENABLED) {
//                switch (location) {
//                    case "1100도로":
//                        //여러 ImageView를 동시에 깜빡이게 하기 위해서는 같은 handler를 동시에
//                        roadImgs.add(road_1100Iv);
//                        break;
//                    case "5.16도로":
//                        roadImgs.add(road_516Iv);
//                        break;
//                    case "번영로":
//                        roadImgs.add(road_beonyeongIv);
//                        break;
//                    case "평화로":
//                        roadImgs.add(road_pyeonghwaIv);
//                        break;
//                    case "한창로":
//                        roadImgs.add(road_hanchangIv);
//                        break;
//                    case "남조로":
//                        roadImgs.add(road_namjoIv);
//                        break;
//                    case "비자림로":
//                        roadImgs.add(road_bijaIv);
//                        break;
//                    case "서성로":
//                        roadImgs.add(road_seoseongIv);
//                        break;
//                    case "제1산록도로":
//                        roadImgs.add(road_sallok1Iv);
//                        break;
//                    case "제2산록도로":
//                        roadImgs.add(road_sallok2Iv);
//                        break;
//                    case "명림로":
//                        roadImgs.add(road_myeongnimIv);
//                        break;
//                    case "첨단로":
//                        roadImgs.add(road_cheomdanIv);
//                        break;
//                    case "기타도로":
//                        if (cursor.getString(RoadFragment.COL_ROAD_SECTION).contains("애조로")) {
//                            roadImgs.add(road_aejoIv);
//                        }
//                        if (cursor.getString(RoadFragment.COL_ROAD_SECTION).contains("일주도로")) {
//                            roadImgs.add(road_iljuIv);
//                        }
//                        break;
//                    default:
//                        break;
//                }
//            }
//
//
////        FrameLayout roadStatusL = (FrameLayout)((MainActivity)mContext).findViewById(R.id.layout_road_status);
////        roadStatusL.setOnClickListener(new View.OnClickListener() {
////            @Override
////            public void onClick(View view) {
////                dialogInfo = new DialogInfo(mContext, clickListener, roads);
////                dialogInfo.setCanceledOnTouchOutside(false);
////                //dialogResult 외부 화면은 터치해도 반응하지 않음
////                dialogInfo.show();
////            }
////        });
//
//        }
//
//        if (roadImgs.size() == 0) {
//            normalTV.setVisibility(View.VISIBLE);
//            startBlink(normalTV);
//        } else {
//            for (ImageView i : roadImgs) {
//                i.setVisibility(View.VISIBLE);
//                startBlink(i);
//            }
//        }
//
//    }
//
//    private void startBlink(View i) {
//        animation.setDuration(500);
//        animation.setInterpolator(new LinearInterpolator());
//        animation.setRepeatCount(Animation.INFINITE);
//        animation.setRepeatMode(Animation.REVERSE);
//        i.startAnimation(animation);
//    }



}
