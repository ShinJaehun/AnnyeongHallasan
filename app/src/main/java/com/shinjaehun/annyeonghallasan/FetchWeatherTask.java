package com.shinjaehun.annyeonghallasan;

import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.text.format.Time;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Vector;

/**
 * Created by shinjaehun on 2017-01-06.
 */


//
//import android.content.ContentUris;
//import android.content.ContentValues;
//import android.content.Context;
//import android.content.SharedPreferences;
//import android.database.Cursor;
//import android.database.DatabaseUtils;
//import android.net.Uri;
//import android.os.AsyncTask;
//import android.preference.PreferenceManager;
//import android.text.format.Time;
//import android.util.Log;
//import android.widget.ArrayAdapter;
//
//import com.shinjaehun.sunshine.data.WeatherContract;
//import com.shinjaehun.sunshine.data.WeatherContract.WeatherEntry;
//
//import org.json.JSONArray;
//import org.json.JSONException;
//import org.json.JSONObject;
//
//import java.io.BufferedReader;
//import java.io.IOException;
//import java.io.InputStream;
//import java.io.InputStreamReader;
//import java.net.HttpURLConnection;
//import java.net.URL;
//import java.text.SimpleDateFormat;
//import java.util.Date;
//import java.util.Vector;
//
public class FetchWeatherTask extends AsyncTask<String, Void, Void> {

    private final String LOG_TAG = FetchWeatherTask.class.getSimpleName();

    //private ArrayAdapter<String> mForecastAdapter;
    private final Context mContext;

    //public FetchWeatherTask(Context context, ForecastAdapter forecastAdapter) {
    public FetchWeatherTask(Context context) {
        mContext = context;
        //mForecastAdapter = forecastAdapter;
    }

    private boolean DEBUG = true;

    /* The date/time conversion code is going to be moved outside the asynctask later,
     * so for convenience we're breaking it out into its own method now.
     */
//    private String getReadableDateString(long time){
//        // Because the API returns a unix timestamp (measured in seconds),
//        // it must be converted to milliseconds in order to be converted to valid date.
//        Date date = new Date(time);
//        SimpleDateFormat format = new SimpleDateFormat("E, MMM d");
//        return format.format(date).toString();
//    }
//
//    /**
//     * Prepare the weather high/lows for presentation.
//     */
//    private String formatHighLows(double high, double low) {
//        // Data is fetched in Celsius by default.
//        // If user prefers to see in Fahrenheit, convert the values here.
//        // We do this rather than fetching in Fahrenheit so that the user can
//        // change this option without us having to re-fetch the data once
//        // we start storing the values in a database.
//        SharedPreferences sharedPrefs =
//                PreferenceManager.getDefaultSharedPreferences(mContext);
//        String unitType = sharedPrefs.getString(
//                mContext.getString(R.string.pref_units_key),
//                mContext.getString(R.string.units_metric));
//                //mContext.getString(R.string.pref_units_metric));
//
//        //if (unitType.equals(mContext.getString(R.string.pref_units_imperial))) {
//        if (unitType.equals(mContext.getString(R.string.units_imperial))) {
//            high = (high * 1.8) + 32;
//            low = (low * 1.8) + 32;
//        //} else if (!unitType.equals(mContext.getString(R.string.pref_units_metric))) {
//        } else if (!unitType.equals(mContext.getString(R.string.units_metric))) {
//            Log.d(LOG_TAG, "Unit type not found: " + unitType);
//        }
//
//        // For presentation, assume the user doesn't care about tenths of a degree.
//        long roundedHigh = Math.round(high);
//        long roundedLow = Math.round(low);
//
//        String highLowStr = roundedHigh + "/" + roundedLow;
//        return highLowStr;
//    }

//    /**
//     * Helper method to handle insertion of a new location in the weather database.
//     *
//     * @param locationSetting The location string used to request updates from the server.
//     * @param cityName A human-readable city name, e.g "Mountain View"
//     * @param lat the latitude of the city
//     * @param lon the longitude of the city
//     * @return the row ID of the added location.
//     */
//    long addLocation(String locationSetting, String cityName, double lat, double lon) {
//        // Students: First, check if the location with this city name exists in the db
//        // If it exists, return the current ID
//        // Otherwise, insert it using the content resolver and the base URI
//        Long locationId;
//
//        //이 query 자체가 DB에서 locationSetting에 해당하는 쿼리가 존재하는지 확인하는 과정이다.
//        Cursor locationCursor = mContext.getContentResolver().query(
//                WeatherContract.LocationEntry.CONTENT_URI,
//                new String[]{WeatherContract.LocationEntry._ID},
//                WeatherContract.LocationEntry.COLUMN_LOCATION_SETTING + " = ?",
//                new String[]{locationSetting},
//                null
//        );
//        //안드로이드 커서는 열 전체의 모음이다! 하나만을 의미하는게 아냐!
//
//        //Log.d(LOG_TAG, "locationCursor : " + locationCursor.toString());
//        //그니까 이건 걍 주소만 보여줄 뿐이지
//
//        if (locationCursor.moveToFirst()) {
//            //커서가 첫번째 열을 가리킬 수 있다면(DB에 기존 자료가 존재한다면)
//            //locationCursor에서 유일무이한 컬럼인 _ID로 index를 알아냄
//            //이렇게 해서 location 정보가 중복되지 않음
//            //_ID index를 이용해 얻은 해당 locationId를 return함.
//            int locationIdIndex = locationCursor.getColumnIndex(WeatherContract.LocationEntry._ID);
//            locationId = locationCursor.getLong(locationIdIndex);
//        } else {
//            //열이 아무것도 없어 커서가 비어있다면
//            //즉, 기존에 없던 location 값이라면!
//            //새로 location을 생성해서 insert()하고 여기서 나오는 locationId를 리턴함.
//            ContentValues locationValues = new ContentValues();
//
//            locationValues.put(WeatherContract.LocationEntry.COLUMN_CITY_NAME, cityName);
//            locationValues.put(WeatherContract.LocationEntry.COLUMN_LOCATION_SETTING, locationSetting);
//            locationValues.put(WeatherContract.LocationEntry.COLUMN_COORD_LAT, lat);
//            locationValues.put(WeatherContract.LocationEntry.COLUMN_COORD_LONG, lon);
//
//            Uri insertedUri = mContext.getContentResolver().insert(
//                    WeatherContract.LocationEntry.CONTENT_URI,
//                    locationValues
//            );
//
//            locationId = ContentUris.parseId(insertedUri);
//
//        }
//
//        locationCursor.close();
//        return locationId;
//    }
//
//    /*
//        Students: This code will allow the FetchWeatherTask to continue to return the strings that
//        the UX expects so that we can continue to test the application even once we begin using
//        the database.
//     */
////    String[] convertContentValuesToUXFormat(Vector<ContentValues> cvv) {
////        // return strings to keep UI functional for now
////        String[] resultStrs = new String[cvv.size()];
////        for ( int i = 0; i < cvv.size(); i++ ) {
////            ContentValues weatherValues = cvv.elementAt(i);
////            String highAndLow = formatHighLows(
////                    weatherValues.getAsDouble(WeatherEntry.COLUMN_MAX_TEMP),
////                    weatherValues.getAsDouble(WeatherEntry.COLUMN_MIN_TEMP));
////            resultStrs[i] = getReadableDateString(
////                    weatherValues.getAsLong(WeatherEntry.COLUMN_DATE)) +
////                    " - " + weatherValues.getAsString(WeatherEntry.COLUMN_SHORT_DESC) +
////                    " - " + highAndLow;
////        }
////        return resultStrs;
////    }
//
//    /**
//     * Take the String representing the complete forecast in JSON Format and
//     * pull out the data we need to construct the Strings needed for the wireframes.
//     *
//     * Fortunately parsing is easy:  constructor takes the JSON string and converts it
//     * into an Object hierarchy for us.
//     */
//    private void getWeatherDataFromJson(String forecastJsonStr,
//                                            String locationSetting)
//            throws JSONException {
//
//        // Now we have a String representing the complete forecast in JSON Format.
//        // Fortunately parsing is easy:  constructor takes the JSON string and converts it
//        // into an Object hierarchy for us.
//
//        // These are the names of the JSON objects that need to be extracted.
//
//        // Location information
//        final String OWM_CITY = "city";
//        final String OWM_CITY_NAME = "name";
//        final String OWM_COORD = "coord";
//
//        // Location coordinate
//        final String OWM_LATITUDE = "lat";
//        final String OWM_LONGITUDE = "lon";
//
//        // Weather information.  Each day's forecast info is an element of the "list" array.
//        final String OWM_LIST = "list";
//
//        final String OWM_PRESSURE = "pressure";
//        final String OWM_HUMIDITY = "humidity";
//        final String OWM_WINDSPEED = "speed";
//        final String OWM_WIND_DIRECTION = "deg";
//
//        // All temperatures are children of the "temp" object.
//        final String OWM_TEMPERATURE = "temp";
//        final String OWM_MAX = "max";
//        final String OWM_MIN = "min";
//
//        final String OWM_WEATHER = "weather";
//        final String OWM_DESCRIPTION = "main";
//        final String OWM_WEATHER_ID = "id";
//
//        try {
//            JSONObject forecastJson = new JSONObject(forecastJsonStr);
//            JSONArray weatherArray = forecastJson.getJSONArray(OWM_LIST);
//
//            JSONObject cityJson = forecastJson.getJSONObject(OWM_CITY);
//            String cityName = cityJson.getString(OWM_CITY_NAME);
//
//            JSONObject cityCoord = cityJson.getJSONObject(OWM_COORD);
//            double cityLatitude = cityCoord.getDouble(OWM_LATITUDE);
//            double cityLongitude = cityCoord.getDouble(OWM_LONGITUDE);
//
//            long locationId = addLocation(locationSetting, cityName, cityLatitude, cityLongitude);
//
//            // Insert the new weather information into the database
//            Vector<ContentValues> cVVector = new Vector<ContentValues>(weatherArray.length());
//
//            // OWM returns daily forecasts based upon the local time of the city that is being
//            // asked for, which means that we need to know the GMT offset to translate this data
//            // properly.
//
//            // Since this data is also sent in-order and the first day is always the
//            // current day, we're going to take advantage of that to get a nice
//            // normalized UTC date for all of our weather.
//
//            Time dayTime = new Time();
//            dayTime.setToNow();
//
//            // we start at the day returned by local time. Otherwise this is a mess.
//            int julianStartDay = Time.getJulianDay(System.currentTimeMillis(), dayTime.gmtoff);
//
//            // now we work exclusively in UTC
//            dayTime = new Time();
//
//            for(int i = 0; i < weatherArray.length(); i++) {
//                // These are the values that will be collected.
//                long dateTime;
//                double pressure;
//                int humidity;
//                double windSpeed;
//                double windDirection;
//
//                double high;
//                double low;
//
//                String description;
//                int weatherId;
//
//                // Get the JSON object representing the day
//                JSONObject dayForecast = weatherArray.getJSONObject(i);
//
//                // Cheating to convert this to UTC time, which is what we want anyhow
//                dateTime = dayTime.setJulianDay(julianStartDay+i);
//
//                pressure = dayForecast.getDouble(OWM_PRESSURE);
//                humidity = dayForecast.getInt(OWM_HUMIDITY);
//                windSpeed = dayForecast.getDouble(OWM_WINDSPEED);
//                windDirection = dayForecast.getDouble(OWM_WIND_DIRECTION);
//
//                // Description is in a child array called "weather", which is 1 element long.
//                // That element also contains a weather code.
//                JSONObject weatherObject =
//                        dayForecast.getJSONArray(OWM_WEATHER).getJSONObject(0);
//                description = weatherObject.getString(OWM_DESCRIPTION);
//                weatherId = weatherObject.getInt(OWM_WEATHER_ID);
//
//                // Temperatures are in a child object called "temp".  Try not to name variables
//                // "temp" when working with temperature.  It confuses everybody.
//                JSONObject temperatureObject = dayForecast.getJSONObject(OWM_TEMPERATURE);
//                high = temperatureObject.getDouble(OWM_MAX);
//                low = temperatureObject.getDouble(OWM_MIN);
//
//                ContentValues weatherValues = new ContentValues();
//
//                weatherValues.put(WeatherEntry.COLUMN_LOC_KEY, locationId);
//                weatherValues.put(WeatherEntry.COLUMN_DATE, dateTime);
//                weatherValues.put(WeatherEntry.COLUMN_HUMIDITY, humidity);
//                weatherValues.put(WeatherEntry.COLUMN_PRESSURE, pressure);
//                weatherValues.put(WeatherEntry.COLUMN_WIND_SPEED, windSpeed);
//                weatherValues.put(WeatherEntry.COLUMN_DEGREES, windDirection);
//                weatherValues.put(WeatherEntry.COLUMN_MAX_TEMP, high);
//                weatherValues.put(WeatherEntry.COLUMN_MIN_TEMP, low);
//                weatherValues.put(WeatherEntry.COLUMN_SHORT_DESC, description);
//                weatherValues.put(WeatherEntry.COLUMN_WEATHER_ID, weatherId);
//
//                cVVector.add(weatherValues);
//            }
//
//            int inserted = 0;
//
//            // add to database
//            if ( cVVector.size() > 0 ) {
//                // Student: call bulkInsert to add the weatherEntries to the database here
//                ContentValues[] cvArray = new ContentValues[cVVector.size()];
//                cVVector.toArray(cvArray);
//
//
//            for (int i = 0 ; i <  cvArray.length ; i++ ) {
//                Log.d(LOG_TAG, "cvArray[" + i +"] :  " + cvArray[i].toString());
//            }
//
////                mContext.getContentResolver().bulkInsert(WeatherEntry.CONTENT_URI, cvArray);
////            }
////
////            // Sort order:  Ascending, by date.
////            String sortOrder = WeatherEntry.COLUMN_DATE + " ASC";
////            Uri weatherForLocationUri = WeatherEntry.buildWeatherLocationWithStartDate(
////                    locationSetting, System.currentTimeMillis());
////
////            // Students: Uncomment the next lines to display what what you stored in the bulkInsert
////            // 처음에는 걍 log에만 결과를 출력하는 건줄 알았음;;
////            // 그니까 이 부분은 방금 DB에 저장한 내용 전체를 다시 불러서 resultStrs라는 배열을 반환한다.
////            // resultStrs은 포맷에 맞게 자료를 변환해서 저장해둔 배열이다.
////            // doInBackground()는 이걸 받아서 다시 onPostExecute()로 보내고
////            // onPostExecute()내에서 arrayAdapter인 mForecastAdapter를 호출해서 화면에 알맞게 표시한다.
////            // 음 그렇다면 doInBackground()에서 반환 값이 onPostExecute()의 매개변수가 된다는 말이지...
////            // BINGO! AsyncTask 선언할 때 세번째 parameter매개변수를 의미한다. public class FetchWeatherTask extends AsyncTask<String, Void, String[]> {
////
////            Cursor cur = mContext.getContentResolver().query(weatherForLocationUri,
////                    null, null, null, sortOrder);
////
////            cVVector = new Vector<ContentValues>(cur.getCount());
////            if ( cur.moveToFirst() ) {
////                do {
////                    ContentValues cv = new ContentValues();
////                    DatabaseUtils.cursorRowToContentValues(cur, cv);
////                    cVVector.add(cv);
////                } while (cur.moveToNext());
//
//                inserted = mContext.getContentResolver().bulkInsert(WeatherEntry.CONTENT_URI, cvArray);
//            }
//
////            Log.d(LOG_TAG, "FetchWeatherTask Complete. " + cVVector.size() + " Inserted");
////
////            String[] resultStrs = convertContentValuesToUXFormat(cVVector);
////            // 그니까 DB에 저장되는 DATA는 JSON을 파싱해서 받아온 값 그대로 저장하지만
////            // 화면에 표시할 때는 DB에서 그 값을 받아와서 가공해서 보여준다.
////
////            for (int i = 0 ; i <  resultStrs.length ; i++ ) {
////                Log.d(LOG_TAG, "resultStrs[" + i +"] :  " + resultStrs[i]);
////            }
////
////            return resultStrs;
//
//            Log.d(LOG_TAG, "FetchWeatherTask Complete. " + inserted + " Inserted");
//
//        } catch (JSONException e) {
//            Log.e(LOG_TAG, e.getMessage(), e);
//            e.printStackTrace();
//        }
//        //return null;
//    }

    @Override
    protected Void doInBackground(String... params) {

        Calendar calendar = Calendar.getInstance();

        String min = new SimpleDateFormat("mm").format(calendar.getTime());
//        String min = "10";
        if (Integer.parseInt(min) < 30) {
            //30분 이전이면 basetime은 한 시간 전
            calendar.set(Calendar.HOUR_OF_DAY, calendar.get(Calendar.HOUR_OF_DAY) - 1);

            if (Integer.parseInt(new SimpleDateFormat("HH").format(calendar.getTime())) < 0) {
//            if (Integer.parseInt("00") <= 0) {
                //자정 이전은 전날 값으로 계산
                calendar.set(Calendar.DATE, calendar.get(Calendar.DATE) -1);
                calendar.set(Calendar.HOUR_OF_DAY, 23);
                calendar.set(Calendar.MINUTE, 0);
                calendar.set(Calendar.SECOND, 0);            }
        }

        String baseDate = new SimpleDateFormat("yyyyMMdd").format(calendar.getTime());
        String baseTime = new SimpleDateFormat("HH").format(calendar.getTime()) + "00";
        Log.v(LOG_TAG, "The data's Time: " + baseDate + baseTime);

//
//        // If there's no zip code, there's nothing to look up.  Verify size of params.
//        if (params.length == 0) {
//            return null;
//        }
//        String locationQuery = params[0];
//
//        // These two need to be declared outside the try/catch
//        // so that they can be closed in the finally block.
//        HttpURLConnection urlConnection = null;
//        BufferedReader reader = null;
//
//        // Will contain the raw JSON response as a string.
//        String forecastJsonStr = null;
//
//        String format = "json";
//        String units = "metric";
//        int numDays = 14;
//
//        try {
//            // Construct the URL for the OpenWeatherMap query
//            // Possible parameters are avaiable at OWM's forecast API page, at
//            // http://openweathermap.org/API#forecast
//            final String FORECAST_BASE_URL =
//                    "http://api.openweathermap.org/data/2.5/forecast/daily?";
//            final String QUERY_PARAM = "q";
//            final String FORMAT_PARAM = "mode";
//            final String UNITS_PARAM = "units";
//            final String DAYS_PARAM = "cnt";
//
//            Uri builtUri = Uri.parse(FORECAST_BASE_URL).buildUpon()
//                    .appendQueryParameter(QUERY_PARAM, params[0])
//                    .appendQueryParameter(FORMAT_PARAM, format)
//                    .appendQueryParameter(UNITS_PARAM, units)
//                    .appendQueryParameter(DAYS_PARAM, Integer.toString(numDays))
//                    .build();
//
//            URL url = new URL(builtUri.toString());
//
//            Log.v(LOG_TAG, "Built URI " + builtUri.toString());
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
//            forecastJsonStr = buffer.toString();
//
//            Log.v(LOG_TAG, "Forecast Json String: " + forecastJsonStr);
//
//            getWeatherDataFromJson(forecastJsonStr, locationQuery);
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

//        try {
//            return getWeatherDataFromJson(forecastJsonStr, locationQuery);
//        } catch (JSONException e) {
//            Log.e(LOG_TAG, e.getMessage(), e);
//            e.printStackTrace();
//        }
        // This will only happen if there was an error getting or parsing the forecast.
        return null;
    }

//    @Override
//    protected void onPostExecute(String[] result) {
//        if (result != null && mForecastAdapter != null) {
//            mForecastAdapter.clear();
//
//            for (int i = 0 ; i <  result.length ; i++ ) {
//                Log.d(LOG_TAG, "onPostExecute - result[" + i +"] :  " + result[i]);
//
//            }
//
//            for(String dayForecastStr : result) {
//                mForecastAdapter.add(dayForecastStr);
//            }
//            // New data is back from the server.  Hooray!
//        }
//    }
}