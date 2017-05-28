package com.shinjaehun.annyeonghallasan;

import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.net.Uri;
import android.os.AsyncTask;
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
import java.util.Vector;

/**
 * Created by shinjaehun on 2017-01-06.
 */

public class FetchWeatherTask extends AsyncTask<Object, Void, Void> {

    private final String LOG_TAG = FetchWeatherTask.class.getSimpleName();

    private final Context mContext;
    private final Calendar mCalendar;
    private String mTimeStamp;

    public FetchWeatherTask(Context context, Calendar calendar) {
        mContext = context;
        mCalendar = calendar;
        mTimeStamp = new SimpleDateFormat("yyyyMMddHHmm").format(calendar.getTime());
        Log.v(LOG_TAG, "현재시간" + " " + mTimeStamp);
    }

    private boolean DEBUG = true;

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

        fetchWeatherJson("한라산", baseDate, baseTime, 53, 35);
        fetchWeatherJson("어리목", baseDate, baseTime, 52, 36);
        fetchWeatherJson("영실", baseDate, baseTime, 52, 34);
        fetchWeatherJson("성판악", baseDate, baseTime, 54, 35);
        fetchWeatherJson("관음사", baseDate, baseTime, 53, 36);
        fetchWeatherJson("돈내코", baseDate, baseTime, 53, 34);

        return null;

    }

    private void fetchWeatherJson(String location, String baseDate, String baseTime, int x, int y) {

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

            Log.v(LOG_TAG, "Built URI " + url.toString());

            // Create the request to OpenWeatherMap, and open the connection
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();

            // Read the input stream into a String
            InputStream inputStream = urlConnection.getInputStream();
            StringBuffer buffer = new StringBuffer();
            if (inputStream == null) {
                // Nothing to do.
                return ;
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
                return ;
            }

            String weatherJsonStr = buffer.toString();

            Log.v(LOG_TAG, "Forecast Json String: " + weatherJsonStr);
            getWeatherDataFromJson(location, weatherJsonStr);
            return ;

        } catch (IOException e) {
            Log.e(LOG_TAG, "Error ", e);
            // If the code didn't successfully get the weather data, there's no point in attempting
            // to parse it.
            return ;
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

        return ;
    }

    private void getWeatherDataFromJson(String location, String weatherJsonStr)
            throws JSONException {

        try {
            JSONObject weatherJsonStrObj = new JSONObject(weatherJsonStr);
            JSONObject responseObject = weatherJsonStrObj.getJSONObject("response");
            JSONObject bodyObject = responseObject.getJSONObject("body");
            JSONObject itemsObject = bodyObject.getJSONObject("items");
            JSONArray item = itemsObject.getJSONArray("item");

            ContentValues weaterValues = new ContentValues();

            for (int i = 0; i < item.length(); i++) {
                JSONObject weatherObject = (JSONObject) item.get(i);

                String category = weatherObject.get("category").toString();
                float value = Float.parseFloat(weatherObject.get("obsrValue").toString());

                if (i == 0) {

                    weaterValues.put(WeatherEntry.COLUMN_LOCATION, location);
                    weaterValues.put(WeatherEntry.COLUMN_TIMESTAMP, mTimeStamp);
                    weaterValues.put(WeatherEntry.COLUMN_BASE_DATE, weatherObject.get("baseDate").toString());
                    weaterValues.put(WeatherEntry.COLUMN_BASE_TIME, weatherObject.get("baseTime").toString());
                    weaterValues.put(WeatherEntry.COLUMN_NX, (int) weatherObject.get("nx"));
                    weaterValues.put(WeatherEntry.COLUMN_NY, (int) weatherObject.get("ny"));
                }

                WeatherEntry.Category c = null;

                try {
                    c = WeatherEntry.Category.valueOf(category);
                } catch (IllegalArgumentException e) {
                    Log.e(LOG_TAG, "Category Exception " + e);
                }

                switch (c) {
                    case T1H:
                        weaterValues.put(WeatherEntry.COLUMN_T1H, value);
                        break;
                    case RN1:
                        weaterValues.put(WeatherEntry.COLUMN_RN1, value);
                        break;
                    case SKY:
                        weaterValues.put(WeatherEntry.COLUMN_SKY, value);
                        break;
                    case UUU:
                        weaterValues.put(WeatherEntry.COLUMN_UUU, value);
                        break;
                    case VVV:
                        weaterValues.put(WeatherEntry.COLUMN_VVV, value);
                        break;
                    case REH:
                        weaterValues.put(WeatherEntry.COLUMN_REH, value);
                        break;
                    case PTY:
                        weaterValues.put(WeatherEntry.COLUMN_PTY, value);
                        break;
                    case LGT:
                        weaterValues.put(WeatherEntry.COLUMN_LGT, value);
                        break;
                    case VEC:
                        weaterValues.put(WeatherEntry.COLUMN_VEC, value);
                        break;
                    case WSD:
                        weaterValues.put(WeatherEntry.COLUMN_WSD, value);
                        break;
                }


            }

            if (weaterValues.size() > 0) {
                //DB에 insert
//                Uri weatherWithDateUri = WeatherEntry.buildWeatherUriWithDate(mTimeStamp);
//                Cursor c = mContext.getContentResolver().query(
//                        weatherWithDateUri,
//                        new String[] { WeatherEntry.COLUMN_LOCATION, WeatherEntry.COLUMN_TIMESTAMP },
//                        null,
//                        null,
//                        null
//                );
//
//                while (c.moveToNext()) {
//                    Log.v(LOG_TAG, "장소 : " + c.getString(0) + " 타임스탬프 : " + c.getString(1));
//
//                }
//                이렇게 DB에서 타임스탬프를 가져와서 비교하는 건 아닌 거 같다!
//                query에 많은 자원이 소모되기 때문 아닐까...

                mContext.getContentResolver().insert(WeatherEntry.CONTENT_URI, weaterValues);

            }

//            long id = addWeather(weather.getLocation(), mTimeStamp, weather.getBaseDate(), weather.getBaseTime(), weather.getNx(), weather.getNy(), weather.getT1h(), weather.getRn1(), weather.getSky(), weather.getUuu(), weather.getVvv(), weather.getReh(), weather.getPty(), weather.getLgt(), weather.getVec(), weather.getWsd());

        } catch (JSONException e) {
            Log.e(LOG_TAG, e.getMessage(), e);
            e.printStackTrace();
        }
        return ;
    }

//    long addWeather(String location, String timeStamp, String baseDate, String baseTime, int nx, int ny, float t1h, float rn1, float sky, float uuu, float vvv, float reh, float pty, float lgt, float vec, float wsd) {
//        long weatherId;
//
////        Cursor weatherCursor = mContext.getContentResolver().query(
////                WeatherEntry.CONTENT_URI,
////                new String[] { WeatherEntry._ID },
////                WeatherEntry.COLUMN_TIMESTAMP + " = ? ",
////                new String[] { timeStamp },
////                null);
//
////        if (weatherCursor.moveToFirst()) {
////            int weatherIdIndex = weatherCursor.getColumnIndex(WeatherEntry._ID);
////            weatherId = weatherCursor.getLong(weatherIdIndex);
////        } else {
//            ContentValues weatherValues = new ContentValues();
//
//            weatherValues.put(WeatherEntry.COLUMN_LOCATION, location);
//            weatherValues.put(WeatherEntry.COLUMN_TIMESTAMP, timeStamp);
//            weatherValues.put(WeatherEntry.COLUMN_BASE_DATE, baseDate);
//            weatherValues.put(WeatherEntry.COLUMN_BASE_TIME, baseTime);
//            weatherValues.put(WeatherEntry.COLUMN_NX, nx);
//            weatherValues.put(WeatherEntry.COLUMN_NY, ny);
//            weatherValues.put(WeatherEntry.COLUMN_T1H, t1h);
//            weatherValues.put(WeatherEntry.COLUMN_RN1, rn1);
//            weatherValues.put(WeatherEntry.COLUMN_SKY, sky);
//            weatherValues.put(WeatherEntry.COLUMN_UUU, uuu);
//            weatherValues.put(WeatherEntry.COLUMN_VVV, vvv);
//            weatherValues.put(WeatherEntry.COLUMN_REH, reh);
//            weatherValues.put(WeatherEntry.COLUMN_PTY, pty);
//            weatherValues.put(WeatherEntry.COLUMN_LGT, lgt);
//            weatherValues.put(WeatherEntry.COLUMN_VEC, vec);
//            weatherValues.put(WeatherEntry.COLUMN_WSD, wsd);
//
//            Uri insertedUri = mContext.getContentResolver().insert(
//                    WeatherEntry.CONTENT_URI, weatherValues);
//            weatherId = ContentUris.parseId(insertedUri);
////        }
//
////        weatherCursor.close();
//        return weatherId;
//    }
//
//    void query() {
//        long weaterId;
//
////        long t = 201705202251L;
//        String timeStamp = "201705202251";
////        Cursor cursor = mContext.getContentResolver().query(
////                WeatherEntry.CONTENT_URI,
////                null,
////                null,
////                null,
////                null
////        );
//
////        Cursor cursor = mContext.getContentResolver().query(
////                WeatherEntry.CONTENT_URI,
////                null,
////                WeatherEntry.COLUMN_TIMESTAMP + " = ? ",
////                new String[] { timeStamp },
////                null
////        );
//
//        Uri weatherWithDateUri = WeatherEntry.buildWeatherUriWithDate(timeStamp);
//        Cursor cursor = mContext.getContentResolver().query(
//                weatherWithDateUri,
//                null,
//                null,
//                null,
//                null
//        );
//
////        Cursor cursor = mContext.getContentResolver().query(
////                WeatherEntry.CONTENT_URI.withAppendedPath(WeatherEntry.CONTENT_URI, String.valueOf(t)),
////                null,
////                null,
////                null,
////                null
////        );
//
//        while (cursor.moveToNext()) {
//
//
//            int weatherIdIndex = cursor.getColumnIndex(WeatherEntry._ID);
//            weaterId = cursor.getLong(weatherIdIndex);
//
//            int weatherLocationIndex = cursor.getColumnIndex(WeatherEntry.COLUMN_LOCATION);
//            int weatherTimeStampIndex = cursor.getColumnIndex(WeatherEntry.COLUMN_TIMESTAMP);
//            Log.v(LOG_TAG,  "ID : " + weaterId + " 장소 : " + cursor.getString(weatherLocationIndex) + " TimeStamp " + cursor.getString(weatherTimeStampIndex));
//        }
//
//        cursor.close();
//    }

}
