package com.shinjaehun.annyeonghallasan.sync;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.content.AbstractThreadedSyncAdapter;
import android.content.ContentProviderClient;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.SyncResult;
import android.os.Bundle;
import android.util.Log;

import com.shinjaehun.annyeonghallasan.R;
import com.shinjaehun.annyeonghallasan.data.HallasanContract;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.io.InputStream;
import java.util.Calendar;
import java.util.Vector;

/**
 * Created by shinjaehun on 2017-06-03.
 */

public class RoadSyncAdapter extends AbstractThreadedSyncAdapter {
    private final String LOG_TAG = RoadSyncAdapter.class.getSimpleName();

    private static Context mContext;
    private static String mTimeStamp;
    private static Boolean mIsDebugging;

    public RoadSyncAdapter(Context context, boolean autoInitialize) {
        super(context, autoInitialize);
    }

    @Override
    public void onPerformSync(Account account, Bundle bundle, String s, ContentProviderClient contentProviderClient, SyncResult syncResult) {
        Log.v(LOG_TAG, "RoadSyncAdatpter onPerformSync Called!");

        //이전에 insert된 값이 없다면 일단 fetch
        Vector<ContentValues> cVVector = new Vector<ContentValues>(13);

        try {
            Document doc = null;
            String url = "http://www.jjpolice.go.kr/jjpolice/police25/traffic.htm?act=rss";

            if (mIsDebugging) {
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

    }

    public static void syncImmediately(Context context, String timeStamp, Boolean isDebugging) {
        mContext = context;
        mTimeStamp = timeStamp;
        mIsDebugging = isDebugging;

        Bundle bundle = new Bundle();
        bundle.putBoolean(ContentResolver.SYNC_EXTRAS_EXPEDITED, true);
        bundle.putBoolean(ContentResolver.SYNC_EXTRAS_MANUAL, true);
        ContentResolver.requestSync(getSyncAccount(context), context.getString(R.string.content_authority), bundle);
    }

    public static Account getSyncAccount(Context context) {
        AccountManager accountManager = (AccountManager)context.getSystemService(Context.ACCOUNT_SERVICE);
        Account newAccount = new Account(context.getString(R.string.app_name), context.getString(R.string.sync_account_type));

        if (accountManager.getPassword(newAccount) == null) {
            if (!accountManager.addAccountExplicitly(newAccount, "", null)) {
                return null;
            }
        }
        return newAccount;
    }
}
