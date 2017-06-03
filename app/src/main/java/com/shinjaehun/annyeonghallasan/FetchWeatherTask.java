package com.shinjaehun.annyeonghallasan;

import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.net.Uri;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import com.shinjaehun.annyeonghallasan.data.HallasanContract;
import com.shinjaehun.annyeonghallasan.data.HallasanProvider;
import com.shinjaehun.annyeonghallasan.model.Weather;
import com.shinjaehun.annyeonghallasan.data.HallasanContract.WeatherEntry;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLDecoder;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.Vector;

/**
 * Created by shinjaehun on 2017-01-06.
 */

public class FetchWeatherTask extends AsyncTask<Object, Void, Void> {

    private final String LOG_TAG = FetchWeatherTask.class.getSimpleName();

    private final Context mContext;
    private final Calendar mCalendar;
    private final String mTimeStamp;

    public FetchWeatherTask(Context context, Calendar calendar, String timeStamp) {
        mContext = context;
        mCalendar = calendar;
        mTimeStamp = timeStamp;

//        Log.v(LOG_TAG, "현재시간" + " " + mTimeStamp);
//        SharedPreferences timeStampPF = PreferenceManager.getDefaultSharedPreferences(mContext);
//        mTimeStamp = timeStampPF.getString(MainActivity.TIME_STAMP, null);
    }

//    제주시 아라동
//    lat : 33.428505
//    lon : 126.486778
//    final String NX_VALUE = String.valueOf(52);
//    final String NY_VALUE = String.valueOf(36);

//    한라산
//    lat : 33.364235
//    lon : 126.545517

    @Override
    protected Void doInBackground(Object... params) {
        Log.v(LOG_TAG, "FetchWeatherTask 중입니다!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");


        String sortOrder = HallasanContract.WeatherEntry._ID + " DESC";
        Uri weatherWithDateUri = HallasanContract.WeatherEntry.buildWeatherUriWithDate(mTimeStamp);
        Cursor cursor = mContext.getContentResolver().query(weatherWithDateUri, null, null, null, sortOrder);



        return null;

//        String min = new SimpleDateFormat("mm").format(mCalendar.getTime());
////        String min = "10";
//        if (Integer.parseInt(min) < 30) {
//            //30분 이전이면 basetime은 한 시간 전
//
////            Log.v(LOG_TAG, "변경 전 시간 : " + new SimpleDateFormat("HH").format(mCalendar.getTime()) + "00");
//
//            mCalendar.set(Calendar.HOUR_OF_DAY, mCalendar.get(Calendar.HOUR_OF_DAY) - 1);
//
//            if (Integer.parseInt(new SimpleDateFormat("HH").format(mCalendar.getTime())) < 0) {
////            if (Integer.parseInt("00") <= 0) {
//                //자정 이전은 전날 값으로 계산
//                mCalendar.set(Calendar.DATE, mCalendar.get(Calendar.DATE) - 1);
//                mCalendar.set(Calendar.HOUR_OF_DAY, 23);
//                mCalendar.set(Calendar.MINUTE, 0);
//                mCalendar.set(Calendar.SECOND, 0);
//            }
//        }
//
//        String baseDate = new SimpleDateFormat("yyyyMMdd").format(mCalendar.getTime());
//        String baseTime = new SimpleDateFormat("HH").format(mCalendar.getTime()) + "00";
//        Log.v(LOG_TAG, "The base weather data's time: " + baseDate + " " + baseTime);
//
////        ArrayList<WeatherReport> weatherReports = new ArrayList<>();
////        weatherReports.add(new WeatherReport("한라산", fetchWeatherJson(baseDate, baseTime, 53, 35))); // 33.364235 126.545517
////        weatherReports.add(new WeatherReport("어리목", fetchWeatherJson(baseDate, baseTime, 52, 36))); // 33.391859 126.4933766
////        weatherReports.add(new WeatherReport("영실", fetchWeatherJson(baseDate, baseTime, 52, 34))); // 33.339573 126.478188
////        weatherReports.add(new WeatherReport("성판악", fetchWeatherJson(baseDate, baseTime, 54, 35))); // 33.3844174 126.6166709
////        weatherReports.add(new WeatherReport("관음사", fetchWeatherJson(baseDate, baseTime, 53, 36))); // 33.423744 126.555786
////        weatherReports.add(new WeatherReport("돈내코", fetchWeatherJson(baseDate, baseTime, 53, 34))); // 33.3101519,126.5681177
////        return weatherReports;
//
////        ArrayList<Weather> weathers = new ArrayList<>();
////        weathers.add(fetchWeatherJson("한라산", baseDate, baseTime, 53, 35)); // 33.364235 126.545517
////        weathers.add(fetchWeatherJson("어리목", baseDate, baseTime, 52, 36)); // 33.391859 126.4933766
////        weathers.add(fetchWeatherJson("영실", baseDate, baseTime, 52, 34)); // 33.339573 126.478188
////        weathers.add(fetchWeatherJson("성판악", baseDate, baseTime, 54, 35)); // 33.3844174 126.6166709
////        weathers.add(fetchWeatherJson("관음사", baseDate, baseTime, 53, 36)); // 33.423744 126.555786
////        weathers.add(fetchWeatherJson("돈내코", baseDate, baseTime, 53, 34)); // 33.3101519,126.5681177
//
////        이거 별로 쓸모 없는 거 닮아!
////        boolean inserted = false;
////        String sortOrder = HallasanContract.WeatherEntry._ID + " DESC";
////        Uri weatherWithDateUri = HallasanContract.WeatherEntry.buildWeatherUriWithDate(mTimeStamp);
////        Cursor cursor = mContext.getContentResolver().query(weatherWithDateUri, null, null, null, sortOrder);
////        // 일단 MainActivity의 timeStamp로 query
////
////        if (cursor.moveToFirst()) {
////            do {
////                String insertedTimeStamp = cursor.getString(WeatherFragment.COL_WEATHER_TIMESTAMP);
////                if (mTimeStamp.equals(insertedTimeStamp))
////                    inserted = true;
////                //이전에 insert된 값이 존재하는가?
////            } while (cursor.moveToNext());
////        }
//
//
//            Vector<ContentValues> cVVector = new Vector<>();
//
//            cVVector.add(fetchWeatherJson("한라산", baseDate, baseTime, 53, 35));
//            cVVector.add(fetchWeatherJson("어리목", baseDate, baseTime, 52, 36));
//            cVVector.add(fetchWeatherJson("영실", baseDate, baseTime, 52, 34));
//            cVVector.add(fetchWeatherJson("성판악", baseDate, baseTime, 54, 35));
//            cVVector.add(fetchWeatherJson("관음사", baseDate, baseTime, 53, 36));
//            cVVector.add(fetchWeatherJson("돈내코", baseDate, baseTime, 53, 34));
//
//            if (cVVector.size() > 0) {
//                //Fetch한 값을 DB에 insert!
//                ContentValues[] cvArray = new ContentValues[cVVector.size()];
//                cVVector.toArray(cvArray);
//                mContext.getContentResolver().bulkInsert(HallasanContract.WeatherEntry.CONTENT_URI, cvArray);
//            }
//
//        return null;

    }

    @Override
    protected void onPostExecute(Void aVoid) {
        super.onPostExecute(aVoid);
    }

    //    private ContentValues fetchWeatherJson(String location, String baseDate, String baseTime, int x, int y) {
//
//        HttpURLConnection urlConnection = null;
//        BufferedReader reader = null;
//
//        try {
//            // Construct the URL for the OpenWeatherMap query
//            // Possible parameters are avaiable at OWM's forecast API page, at
//            // http://openweathermap.org/API#forecast
//            final String WEATHER_BASE_URL =
//                    "http://newsky2.kma.go.kr/service/SecndSrtpdFrcstInfoService2/ForecastGrib?";
//
//            final String SERVICE_KEY_PARAM = "ServiceKey";
//            final String SERVICE_KEY_VALUE = "KUPi0SOw8UGGH1YP9Q%2BGZXxcEc8j3SvIm6TqJaFiO6nk97yfGYsA9sboywRDgS2TIRl6iookaF%2FnJU8ma50yMA%3D%3D";
//
//            final String BASE_DATE_PARAM = "base_date";
//            final String BASE_DATE_VALUE = baseDate;
//
//            final String BASE_TIME_PARAM = "base_time";
//            final String BASE_TIME_VALUE = baseTime;
//
//            final String NX_PARAM = "nx";
//            final String NX_VALUE = String.valueOf(x);
//
//            final String NY_PARAM = "ny";
//            final String NY_VALUE = String.valueOf(y);
//
//            final String NUM_OF_ROWS_PARAM = "numOfRows";
//            final String NUM_OF_ROWS_VALUE = "12";
//
//            final String TYPE_PARAM = "_type";
//            final String TYPE_VALUE = "json";
//
//            Uri builtUri = Uri.parse(WEATHER_BASE_URL).buildUpon()
//                    .appendQueryParameter(SERVICE_KEY_PARAM, SERVICE_KEY_VALUE)
//                    .appendQueryParameter(BASE_DATE_PARAM, BASE_DATE_VALUE)
//                    .appendQueryParameter(BASE_TIME_PARAM, BASE_TIME_VALUE)
//                    .appendQueryParameter(NX_PARAM, NX_VALUE)
//                    .appendQueryParameter(NY_PARAM, NY_VALUE)
//                    .appendQueryParameter(NUM_OF_ROWS_PARAM, NUM_OF_ROWS_VALUE)
//                    .appendQueryParameter(TYPE_PARAM, TYPE_VALUE)
//                    .build();
//
//            URL url = new URL(URLDecoder.decode(builtUri.toString(), "UTF-8"));
//
//            Log.v(LOG_TAG, "Built URI " + url.toString());
//
//            // Create the request to OpenWeatherMap, and open the connection
//            urlConnection = (HttpURLConnection) url.openConnection();
//            urlConnection.setRequestMethod("GET");
//            urlConnection.connect();
//
//            // Read the input stream into a String
//            InputStream inputStream = urlConnection.getInputStream();
//            StringBuffer buffer = new StringBuffer();
//            if (inputStream == null) {
//                // Nothing to do.
//                return null;
//            }
//            reader = new BufferedReader(new InputStreamReader(inputStream));
//
//            String line;
//            while ((line = reader.readLine()) != null) {
//                // Since it's JSON, adding a newline isn't necessary (it won't affect parsing)
//                // But it does make debugging a *lot* easier if you print out the completed
//                // buffer for debugging.
//                buffer.append(line + "\n");
//            }
//
//            if (buffer.length() == 0) {
//                // Stream was empty.  No point in parsing.
//                return null;
//            }
//
//            String weatherJsonStr = buffer.toString();
//
//            Log.v(LOG_TAG, "Forecast Json String: " + weatherJsonStr);
//            return getWeatherDataFromJson(location, baseDate, baseTime, x, y, weatherJsonStr);
//
//        } catch (IOException e) {
//            Log.e(LOG_TAG, "Error ", e);
//            // If the code didn't successfully get the weather data, there's no point in attempting
//            // to parse it.
//            return null;
//        } catch (JSONException e) {
//            Log.e(LOG_TAG, e.getMessage(), e);
//            e.printStackTrace();
//        } finally {
//            if (urlConnection != null) {
//                urlConnection.disconnect();
//            }
//            if (reader != null) {
//                try {
//                    reader.close();
//                } catch (final IOException e) {
//                    Log.e(LOG_TAG, "Error closing stream", e);
//                }
//            }
//        }
//
//        return null;
//    }
//
//    private ContentValues getWeatherDataFromJson(String location, String baseDate, String baseTime, int x, int y, String weatherJsonStr)
//            throws JSONException {
//
//        try {
//            JSONObject weatherJsonStrObj = new JSONObject(weatherJsonStr);
//            JSONObject responseObject = weatherJsonStrObj.getJSONObject("response");
//            JSONObject bodyObject = responseObject.getJSONObject("body");
//            JSONObject itemsObject = bodyObject.getJSONObject("items");
//            JSONArray item = itemsObject.getJSONArray("item");
//
//            ContentValues weatherValues = new ContentValues();
//            //ContentValues로 저장하는 방법을 좀 더 직관적으로 수정함
//
//            weatherValues.put(WeatherEntry.COLUMN_LOCATION, location);
//            weatherValues.put(WeatherEntry.COLUMN_TIMESTAMP, mTimeStamp);
//            weatherValues.put(WeatherEntry.COLUMN_BASE_DATE, baseDate);
//            weatherValues.put(WeatherEntry.COLUMN_BASE_TIME, baseTime);
//            weatherValues.put(WeatherEntry.COLUMN_NX, x);
//            weatherValues.put(WeatherEntry.COLUMN_NY, y);
//
//            for (int i = 0; i < item.length(); i++) {
//                JSONObject weatherObject = (JSONObject) item.get(i);
//
//                String category = weatherObject.get("category").toString();
//                float value = Float.parseFloat(weatherObject.get("obsrValue").toString());
//
//                weatherValues.put(category, value);
//
//            }
//
////                ContentValue 출력하는 코드 : 지우지 마!
////                Set<Map.Entry<String, Object>> s = weatherValues.valueSet();
////                Iterator itr = s.iterator();
////                Log.d("DatabaseSync", "ContentValue Length :: " + weatherValues.size());
////                while(itr.hasNext())
////                {
////                    Map.Entry me = (Map.Entry)itr.next();
////                    String key = me.getKey().toString();
////                    Object value =  me.getValue();
////                    Log.d("DatabaseSync", "Key:"+key+", values:"+(String)(value == null?null:value.toString()));
////                }
//
//            return weatherValues;
//
//        } catch (JSONException e) {
//            Log.e(LOG_TAG, e.getMessage(), e);
//            e.printStackTrace();
//        }
//        return null;
//    }

}
