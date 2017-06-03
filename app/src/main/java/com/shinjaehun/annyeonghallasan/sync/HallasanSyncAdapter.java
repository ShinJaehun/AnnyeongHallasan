package com.shinjaehun.annyeonghallasan.sync;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.content.AbstractThreadedSyncAdapter;
import android.content.ContentProviderClient;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.SyncResult;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;

import com.shinjaehun.annyeonghallasan.R;
import com.shinjaehun.annyeonghallasan.data.HallasanContract;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLDecoder;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Vector;

/**
 * Created by shinjaehun on 2017-05-27.
 */

public class HallasanSyncAdapter extends AbstractThreadedSyncAdapter {
    private static final String LOG_TAG = HallasanSyncAdapter.class.getSimpleName();

    private static Context mContext;
    private static Calendar mCalendar;
    private static String mTimeStamp;
    private static Boolean mDebugging;

    // Interval at which to sync with the weather, in milliseconds.
    // 60 seconds (1 minute)  180 = 3 hours
//    public static final int SYNC_INTERVAL = 60 * 180;
//    public static final int SYNC_INTERVAL = 60;
//    public static final int SYNC_FLEXTIME = SYNC_INTERVAL/3;
//
//    private static final long DAY_IN_MILLIS = 1000 * 60 * 60 * 24;
//    private static final int WEATHER_NOTIFICATION_ID = 3004;


    public HallasanSyncAdapter(Context context, boolean autoInitialize) {
        super(context, autoInitialize);
    }

    @Override
    public void onPerformSync(Account account, Bundle bundle, String s, ContentProviderClient contentProviderClient, SyncResult syncResult) {
        Log.v(LOG_TAG, "WeatherSyncAdatpter onPerformSync Called!");

        roadProcess();
        weatherProcess();
    }

    private void weatherProcess() {

        String min = new SimpleDateFormat("mm").format(mCalendar.getTime());
//        String min = "10";
        if (Integer.parseInt(min) < 30) {
            //30분 이전이면 basetime은 한 시간 전

//            Log.v(LOG_TAG, "변경 전 시간 : " + new SimpleDateFormat("HH").format(mCalendar.getTime()) + "00");

            mCalendar.set(Calendar.HOUR_OF_DAY, mCalendar.get(Calendar.HOUR_OF_DAY) - 1);

            if (Integer.parseInt(new SimpleDateFormat("HH").format(mCalendar.getTime())) < 0) {
//            if (Integer.parseInt("00") <= 0) {
                //자정 이전은 전날 값으로 계산
                mCalendar.set(Calendar.DATE, mCalendar.get(Calendar.DATE) - 1);
                mCalendar.set(Calendar.HOUR_OF_DAY, 23);
                mCalendar.set(Calendar.MINUTE, 0);
                mCalendar.set(Calendar.SECOND, 0);
            }
        }

        String baseDate = new SimpleDateFormat("yyyyMMdd").format(mCalendar.getTime());
        String baseTime = new SimpleDateFormat("HH").format(mCalendar.getTime()) + "00";
        Log.v(LOG_TAG, "The base weather data's time: " + baseDate + " " + baseTime);

//        ArrayList<WeatherReport> weatherReports = new ArrayList<>();
//        weatherReports.add(new WeatherReport("한라산", fetchWeatherJson(baseDate, baseTime, 53, 35))); // 33.364235 126.545517
//        weatherReports.add(new WeatherReport("어리목", fetchWeatherJson(baseDate, baseTime, 52, 36))); // 33.391859 126.4933766
//        weatherReports.add(new WeatherReport("영실", fetchWeatherJson(baseDate, baseTime, 52, 34))); // 33.339573 126.478188
//        weatherReports.add(new WeatherReport("성판악", fetchWeatherJson(baseDate, baseTime, 54, 35))); // 33.3844174 126.6166709
//        weatherReports.add(new WeatherReport("관음사", fetchWeatherJson(baseDate, baseTime, 53, 36))); // 33.423744 126.555786
//        weatherReports.add(new WeatherReport("돈내코", fetchWeatherJson(baseDate, baseTime, 53, 34))); // 33.3101519,126.5681177
//        return weatherReports;

//        ArrayList<Weather> weathers = new ArrayList<>();
//        weathers.add(fetchWeatherJson("한라산", baseDate, baseTime, 53, 35)); // 33.364235 126.545517
//        weathers.add(fetchWeatherJson("어리목", baseDate, baseTime, 52, 36)); // 33.391859 126.4933766
//        weathers.add(fetchWeatherJson("영실", baseDate, baseTime, 52, 34)); // 33.339573 126.478188
//        weathers.add(fetchWeatherJson("성판악", baseDate, baseTime, 54, 35)); // 33.3844174 126.6166709
//        weathers.add(fetchWeatherJson("관음사", baseDate, baseTime, 53, 36)); // 33.423744 126.555786
//        weathers.add(fetchWeatherJson("돈내코", baseDate, baseTime, 53, 34)); // 33.3101519,126.5681177

//        fetchWeatherJson("한라산", baseDate, baseTime, 53, 35);
//        fetchWeatherJson("어리목", baseDate, baseTime, 52, 36);
//        fetchWeatherJson("영실", baseDate, baseTime, 52, 34);
//        fetchWeatherJson("성판악", baseDate, baseTime, 54, 35);
//        fetchWeatherJson("관음사", baseDate, baseTime, 53, 36);
//        fetchWeatherJson("돈내코", baseDate, baseTime, 53, 34);
        Vector<ContentValues> cVVector = new Vector<>();

        cVVector.add(fetchWeatherJson("한라산", baseDate, baseTime, 53, 35));
        cVVector.add(fetchWeatherJson("어리목", baseDate, baseTime, 52, 36));
        cVVector.add(fetchWeatherJson("영실", baseDate, baseTime, 52, 34));
        cVVector.add(fetchWeatherJson("성판악", baseDate, baseTime, 54, 35));
        cVVector.add(fetchWeatherJson("관음사", baseDate, baseTime, 53, 36));
        cVVector.add(fetchWeatherJson("돈내코", baseDate, baseTime, 53, 34));

        if (cVVector.size() > 0) {
            //Fetch한 값을 DB에 insert!
            ContentValues[] cvArray = new ContentValues[cVVector.size()];
            cVVector.toArray(cvArray);
            int size = mContext.getContentResolver().bulkInsert(HallasanContract.WeatherEntry.CONTENT_URI, cvArray);
            Log.v(LOG_TAG, "HallasanSyncAdapter에서 " + mTimeStamp + "에 Weather DB로 집어 넣은 다음 크기 " + size);
        }
    }

    private ContentValues fetchWeatherJson(String location, String baseDate, String baseTime, int x, int y) {

        HttpURLConnection urlConnection = null;
        BufferedReader reader = null;

        try {
            // Construct the URL for the OpenWeatherMap query
            // Possible parameters are avaiable at OWM's forecast API page, at
            // http://openweathermap.org/API#forecast
            final String WEATHER_BASE_URL =
                    "http://newsky2.kma.go.kr/service/SecndSrtpdFrcstInfoService2/ForecastGrib?";

            final String SERVICE_KEY_PARAM = "ServiceKey";
            final String SERVICE_KEY_VALUE = "KUPi0SOw8UGGH1YP9Q%2BGZXxcEc8j3SvIm6TqJaFiO6nk97yfGYsA9sboywRDgS2TIRl6iookaF%2FnJU8ma50yMA%3D%3D";

            final String BASE_DATE_PARAM = "base_date";
            final String BASE_DATE_VALUE = baseDate;

            final String BASE_TIME_PARAM = "base_time";
            final String BASE_TIME_VALUE = baseTime;

            final String NX_PARAM = "nx";
            final String NX_VALUE = String.valueOf(x);

            final String NY_PARAM = "ny";
            final String NY_VALUE = String.valueOf(y);

            final String NUM_OF_ROWS_PARAM = "numOfRows";
            final String NUM_OF_ROWS_VALUE = "12";

            final String TYPE_PARAM = "_type";
            final String TYPE_VALUE = "json";

            Uri builtUri = Uri.parse(WEATHER_BASE_URL).buildUpon()
                    .appendQueryParameter(SERVICE_KEY_PARAM, SERVICE_KEY_VALUE)
                    .appendQueryParameter(BASE_DATE_PARAM, BASE_DATE_VALUE)
                    .appendQueryParameter(BASE_TIME_PARAM, BASE_TIME_VALUE)
                    .appendQueryParameter(NX_PARAM, NX_VALUE)
                    .appendQueryParameter(NY_PARAM, NY_VALUE)
                    .appendQueryParameter(NUM_OF_ROWS_PARAM, NUM_OF_ROWS_VALUE)
                    .appendQueryParameter(TYPE_PARAM, TYPE_VALUE)
                    .build();

            URL url = new URL(URLDecoder.decode(builtUri.toString(), "UTF-8"));

//            Log.v(LOG_TAG, "Built URI " + url.toString());

            // Create the request to OpenWeatherMap, and open the connection
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();

            // Read the input stream into a String
            InputStream inputStream = urlConnection.getInputStream();
            StringBuffer buffer = new StringBuffer();
            if (inputStream == null) {
                // Nothing to do.
                return null;
            }
            reader = new BufferedReader(new InputStreamReader(inputStream));

            String line;
            while ((line = reader.readLine()) != null) {
                // Since it's JSON, adding a newline isn't necessary (it won't affect parsing)
                // But it does make debugging a *lot* easier if you print out the completed
                // buffer for debugging.
                buffer.append(line + "\n");
            }

            if (buffer.length() == 0) {
                // Stream was empty.  No point in parsing.
                return null;
            }

            String weatherJsonStr = buffer.toString();

//            Log.v(LOG_TAG, "Forecast Json String: " + weatherJsonStr);
            return getWeatherDataFromJson(location, baseDate, baseTime, x, y, weatherJsonStr);


        } catch (IOException e) {
            Log.e(LOG_TAG, "Error ", e);
            // If the code didn't successfully get the weather data, there's no point in attempting
            // to parse it.
            return null;
        } catch (JSONException e) {
            Log.e(LOG_TAG, e.getMessage(), e);
            e.printStackTrace();
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
            if (reader != null) {
                try {
                    reader.close();
                } catch (final IOException e) {
                    Log.e(LOG_TAG, "Error closing stream", e);
                }
            }
        }

        return null;
    }

    private ContentValues getWeatherDataFromJson(String location, String baseDate, String baseTime, int x, int y, String weatherJsonStr)
            throws JSONException {

        try {
            JSONObject weatherJsonStrObj = new JSONObject(weatherJsonStr);
            JSONObject responseObject = weatherJsonStrObj.getJSONObject("response");
            JSONObject bodyObject = responseObject.getJSONObject("body");
            JSONObject itemsObject = bodyObject.getJSONObject("items");
            JSONArray item = itemsObject.getJSONArray("item");

            ContentValues weatherValues = new ContentValues();
            //ContentValues로 저장하는 방법을 좀 더 직관적으로 수정함

            weatherValues.put(HallasanContract.WeatherEntry.COLUMN_LOCATION, location);
            weatherValues.put(HallasanContract.WeatherEntry.COLUMN_TIMESTAMP, mTimeStamp);
            weatherValues.put(HallasanContract.WeatherEntry.COLUMN_BASE_DATE, baseDate);
            weatherValues.put(HallasanContract.WeatherEntry.COLUMN_BASE_TIME, baseTime);
            weatherValues.put(HallasanContract.WeatherEntry.COLUMN_NX, x);
            weatherValues.put(HallasanContract.WeatherEntry.COLUMN_NY, y);

            for (int i = 0; i < item.length(); i++) {
                JSONObject weatherObject = (JSONObject) item.get(i);

                String category = weatherObject.get("category").toString();
                float value = Float.parseFloat(weatherObject.get("obsrValue").toString());

                weatherValues.put(category, value);

            }

//                ContentValue 출력하는 코드 : 지우지 마!
//                Set<Map.Entry<String, Object>> s = weatherValues.valueSet();
//                Iterator itr = s.iterator();
//                Log.d("DatabaseSync", "ContentValue Length :: " + weatherValues.size());
//                while(itr.hasNext())
//                {
//                    Map.Entry me = (Map.Entry)itr.next();
//                    String key = me.getKey().toString();
//                    Object value =  me.getValue();
//                    Log.d("DatabaseSync", "Key:"+key+", values:"+(String)(value == null?null:value.toString()));
//                }

            return weatherValues;

        } catch (JSONException e) {
            Log.e(LOG_TAG, e.getMessage(), e);
            e.printStackTrace();
        }
        return null;

    }

    private void roadProcess() {
        Log.v(LOG_TAG, "RoadSyncAdatpter onPerformSync Called!");

        //이전에 insert된 값이 없다면 일단 fetch
        Vector<ContentValues> cVVector = new Vector<ContentValues>(13);

        try {
            Document doc = null;
            String url = "http://www.jjpolice.go.kr/jjpolice/police25/traffic.htm?act=rss";

            if (mDebugging) {
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
            Log.v(LOG_TAG, "HallasanSyncAdapter에서 " + mTimeStamp + "에 Road DB로 집어 넣은 다음 크기 " + size);
        }
    }

//    private void roadProcess() {
//        try {
//            Document doc = null;
//            String url = "http://www.jjpolice.go.kr/jjpolice/police25/traffic.htm?act=rss";
//
//            if (mIsDebugging) {
//                //XML 파일로 테스트하기
//                InputStream inputStream = getContext().getResources().openRawResource(R.raw.sample_data1);
//                try {
//                    doc = Jsoup.parse(inputStream, "UTF-8", url);
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
//            } else {
//                //실제 URL로 테스트하기
//                try {
//                    doc = Jsoup.connect(url).get();
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
//            }
//
//            Elements dateData = doc.select("date");
//            Elements roadTitleData = doc.select("title");
//            Elements roadDescriptionData = doc.select("description");
//
//            String base_date = dateData.get(0).text().toString().trim();
//
//            ContentValues roadValues = new ContentValues();
////        ArrayList<Road> roads = new ArrayList<>();
//            //제주지방경찰청에서 제공하는 13개의 도로 DATA
//
//            for (int i = 1; i < 14; i++) {
//
//                String n = roadTitleData.get(i).text().replace("<![CDATA[", "").replace("]]>", "").toString().trim();
//                //정말 귀찮은데 도로명에 <![CDATA[~~~]> 이거 붙는 거 없애고
//
//                String name;
//                if (n.matches(".*[0-9]\\)$")) {
//                    //"숫자)"로 끝나는 문자열이라면
//                    name = n.substring(0, n.indexOf("("));
//                    // 도로명 "1100도로(1139)"에서 도로 번호를 생략
//
//                } else {
//                    name = n;
//                    // 도로번호가 없는 도로명은 그대로 쓰기
//                }
//
//                roadValues.put(HallasanContract.RoadEntry.COLUMN_NAME, name);
//                roadValues.put(HallasanContract.RoadEntry.COLUMN_TIMESTAMP, mTimeStamp);
//                roadValues.put(HallasanContract.RoadEntry.COLUMN_BASE_DATE, base_date);
//
//                String description = roadDescriptionData.get(i).text().replaceAll("&nbsp;", "").replaceAll("&amp;nbsp;", "").toString().trim();
//                //description에 nbsp랑 amp 붙는거 너무 짜증나!
//
//                String[] v = description.split(":", -6);
//
////                    Log.v(LOG_TAG, location + " 크기는 " + v.length);
////                    Log.v(LOG_TAG, location + "," + v[0].trim() );
////                    Log.v(LOG_TAG, location + "," + v[1].trim() );
////                    Log.v(LOG_TAG, location + "," + v[2].trim() );
////                    Log.v(LOG_TAG, location + "," + v[3].trim() );
////                    Log.v(LOG_TAG, location + "," + v[4].trim() );
////                    Log.v(LOG_TAG, location + "," + v[5].trim() );
///*
//                    통제하는 경우 description 내용
//                    구간 : 제주대 입구 ~ 성판악 적설 : 1 결빙 : 대형 통재상항 :    소형 통재상항 : 체인
//
//                    통제하지 않는 경우 descrition 내용
//                    구간 : 정상 적설 : 결빙 : 대형 통재상항 :    소형 통재상항 :
//*/
//                int restrict = HallasanContract.RoadEntry.RESTRICTION_DISABLED;
//                String section = "정상";
//                if (!v[1].contains("정상")) {
//                    //통제하는 경우
//                    restrict = HallasanContract.RoadEntry.RESTRICTION_ENABLED;
//                    section = v[1].substring(0, v[1].indexOf("적설")).trim();
//                    //통제구간 : "제주대 입구 ~ 성판악"
//                }
//                roadValues.put(HallasanContract.RoadEntry.COLUMN_RESTRICTION, restrict);
//                roadValues.put(HallasanContract.RoadEntry.COLUMN_SECTION, section);
//
//                Float snowfall = 0f;
//                if (v[2].matches(".*\\d+.*")) {
//                    snowfall = Float.parseFloat(v[2].substring(0, v[2].indexOf("결빙")).trim());
//                    //적설
//                }
//                roadValues.put(HallasanContract.RoadEntry.COLUMN_SNOWFALL, snowfall);
//
//                Float freezing = 0f;
//                if (v[3].matches(".*\\d+.*")) {
//                    freezing = Float.parseFloat(v[3].substring(0, v[3].indexOf("대형")).trim());
//                    //결빙
//                }
//                roadValues.put(HallasanContract.RoadEntry.COLUMN_FREEZING, freezing);
//
//                int chain = HallasanContract.RoadEntry.CHAIN_NONE;
//                if (v[4].contains("체인")) {
//                    chain = HallasanContract.RoadEntry.CHAIN_SMALL;
//                }
//                if (v[5].contains("체인")) {
//                    chain = HallasanContract.RoadEntry.CHAIN_BIG;
//                }
//                roadValues.put(HallasanContract.RoadEntry.COLUMN_CHAIN, chain);
//
//                if (roadValues.size() > 0) {
//                    getContext().getContentResolver().insert(HallasanContract.RoadEntry.CONTENT_URI, roadValues);
//                }
//            }
//        } catch (RuntimeException e) {
//            Log.e(LOG_TAG, e.getMessage(), e);
//            e.printStackTrace();
//        }
//        return;
//    }

    public static void syncImmediately(Context context, Calendar calendar, Boolean debugging) {
        mContext = context;
        mCalendar = calendar;
        mTimeStamp = new SimpleDateFormat("yyyyMMddHHmm").format(mCalendar.getTime());
        Log.v(LOG_TAG, "HallasanSyncAdapter에서 MainActivity의 타임스탬프는 : " + mTimeStamp);
        mDebugging = debugging;

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
//
//    public static void initalizeSyncAdapter(Context context) {
//        getSyncAccount(context);
//    }
//
//    public static void configurePeriodicSync(Context context, int syncInterval, int flexTime) {
//        Account account = getSyncAccount(context);
//        String authority = context.getString(R.string.content_authority);
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
//            // we can enable inexact timers in our periodic sync
//            SyncRequest request = new SyncRequest.Builder().
//                    syncPeriodic(syncInterval, flexTime).
//                    setSyncAdapter(account, authority).
//                    setExtras(new Bundle()).build();
//            ContentResolver.requestSync(request);
//        } else {
//            ContentResolver.addPeriodicSync(account,
//                    authority, new Bundle(), syncInterval);
//        }
//    }
//
//
//    private static void onAccountCreated(Account newAccount, Context context) {
//        /*
//         * Since we've created an account
//         */
//        HallasanSyncAdapter.configurePeriodicSync(context, SYNC_INTERVAL, SYNC_FLEXTIME);
//
//        /*
//         * Without calling setSyncAutomatically, our periodic sync will not be enabled.
//         */
//        ContentResolver.setSyncAutomatically(newAccount, context.getString(R.string.content_authority), true);
//
//        /*
//         * Finally, let's do a sync to get things started
//         */
//        syncImmediately(context, mCalendar, mIsDebugging);
//    }
//
//    public static void initializeSyncAdapter(Context context) {
//        getSyncAccount(context);
//    }
}
