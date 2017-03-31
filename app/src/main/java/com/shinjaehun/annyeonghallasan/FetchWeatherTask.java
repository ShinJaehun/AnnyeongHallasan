package com.shinjaehun.annyeonghallasan;

import android.content.Context;
import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.TextView;

import com.shinjaehun.annyeonghallasan.model.Weather;

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

/**
 * Created by shinjaehun on 2017-01-06.
 */

public class FetchWeatherTask extends AsyncTask<Object, Object, ArrayList<Weather>> {

    private final String LOG_TAG = FetchWeatherTask.class.getSimpleName();

    private final Context mContext;

    public FetchWeatherTask(Context context) {
        mContext = context;
    }

    private boolean DEBUG = true;

    enum Category {
        T1H, RN1, SKY, UUU, VVV,
        REH, PTY, LGT, VEC, WSD
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
    protected ArrayList<Weather> doInBackground(Object... params) {

        Calendar calendar = Calendar.getInstance();

        String min = new SimpleDateFormat("mm").format(calendar.getTime());
//        String min = "10";
        if (Integer.parseInt(min) < 30) {
            //30분 이전이면 basetime은 한 시간 전

            Log.v(LOG_TAG, "변경 전 시간 : " + new SimpleDateFormat("HH").format(calendar.getTime()) + "00");


            calendar.set(Calendar.HOUR_OF_DAY, calendar.get(Calendar.HOUR_OF_DAY) - 1);

            if (Integer.parseInt(new SimpleDateFormat("HH").format(calendar.getTime())) < 0) {
//            if (Integer.parseInt("00") <= 0) {
                //자정 이전은 전날 값으로 계산
                calendar.set(Calendar.DATE, calendar.get(Calendar.DATE) - 1);
                calendar.set(Calendar.HOUR_OF_DAY, 23);
                calendar.set(Calendar.MINUTE, 0);
                calendar.set(Calendar.SECOND, 0);
            }
        }

        String baseDate = new SimpleDateFormat("yyyyMMdd").format(calendar.getTime());
        String baseTime = new SimpleDateFormat("HH").format(calendar.getTime()) + "00";
        Log.v(LOG_TAG, "The weather data's time: " + baseDate + " " + baseTime);

//        ArrayList<WeatherReport> weatherReports = new ArrayList<>();
//        weatherReports.add(new WeatherReport("한라산", fetchWeatherJson(baseDate, baseTime, 53, 35))); // 33.364235 126.545517
//        weatherReports.add(new WeatherReport("어리목", fetchWeatherJson(baseDate, baseTime, 52, 36))); // 33.391859 126.4933766
//        weatherReports.add(new WeatherReport("영실", fetchWeatherJson(baseDate, baseTime, 52, 34))); // 33.339573 126.478188
//        weatherReports.add(new WeatherReport("성판악", fetchWeatherJson(baseDate, baseTime, 54, 35))); // 33.3844174 126.6166709
//        weatherReports.add(new WeatherReport("관음사", fetchWeatherJson(baseDate, baseTime, 53, 36))); // 33.423744 126.555786
//        weatherReports.add(new WeatherReport("돈내코", fetchWeatherJson(baseDate, baseTime, 53, 34))); // 33.3101519,126.5681177
//        return weatherReports;

        ArrayList<Weather> weathers = new ArrayList<>();
        weathers.add(fetchWeatherJson("한라산", baseDate, baseTime, 53, 35)); // 33.364235 126.545517
        weathers.add(fetchWeatherJson("어리목", baseDate, baseTime, 52, 36)); // 33.391859 126.4933766
        weathers.add(fetchWeatherJson("영실", baseDate, baseTime, 52, 34)); // 33.339573 126.478188
        weathers.add(fetchWeatherJson("성판악", baseDate, baseTime, 54, 35)); // 33.3844174 126.6166709
        weathers.add(fetchWeatherJson("관음사", baseDate, baseTime, 53, 36)); // 33.423744 126.555786
        weathers.add(fetchWeatherJson("돈내코", baseDate, baseTime, 53, 34)); // 33.3101519,126.5681177
        return weathers;

    }

    private Weather fetchWeatherJson(String location, String baseDate, String baseTime, int x, int y) {

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

            Log.v(LOG_TAG, "Forecast Json String: " + weatherJsonStr);

            return getWeatherDataFromJson(location, weatherJsonStr);

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

    private Weather getWeatherDataFromJson(String location, String weatherJsonStr)
            throws JSONException {

        Weather weather = null;

        try {
            JSONObject weatherJsonStrObj = new JSONObject(weatherJsonStr);
            JSONObject responseObject = weatherJsonStrObj.getJSONObject("response");
            JSONObject bodyObject = responseObject.getJSONObject("body");
            JSONObject itemsObject = bodyObject.getJSONObject("items");
            JSONArray item = itemsObject.getJSONArray("item");

            weather = new Weather();
            for (int i = 0; i < item.length(); i++) {
                JSONObject weatherObject = (JSONObject) item.get(i);

                String category = weatherObject.get("category").toString();
                float value = Float.parseFloat(weatherObject.get("obsrValue").toString());

                if (i == 0) {
                    weather.setLocation(location);
                    weather.setBaseDate(weatherObject.get("baseDate").toString());
                    weather.setBaseTime(weatherObject.get("baseTime").toString());
                    weather.setNx((int) weatherObject.get("nx"));
                    weather.setNy((int) weatherObject.get("ny"));
                }

                Category c = null;

                try {
                    c = Category.valueOf(category);
                } catch (IllegalArgumentException e) {
                    Log.e(LOG_TAG, "Category Exception " + e);
                }

                switch (c) {
                    case T1H:
                        weather.setT1h(value);
                        break;
                    case RN1:
                        weather.setRn1(value);
                        break;
                    case SKY:
                        weather.setSky(value);
                        break;
                    case UUU:
                        weather.setUuu(value);
                        break;
                    case VVV:
                        weather.setVvv(value);
                        break;
                    case REH:
                        weather.setReh(value);
                        break;
                    case PTY:
                        weather.setPty(value);
                        break;
                    case LGT:
                        weather.setLgt(value);
                        break;
                    case VEC:
                        weather.setVec(value);
                        break;
                    case WSD:
                        weather.setWsd(value);
                        break;
                }
            }

        } catch (JSONException e) {
            Log.e(LOG_TAG, e.getMessage(), e);
            e.printStackTrace();
        }
        return weather;
    }

    @Override
    protected void onPostExecute(ArrayList<Weather> weathers) {
        TextView weatherTv = null;
        if (weathers.size() != 0) {

            for (Weather w : weathers) {
                Log.v(LOG_TAG, w.getLocation() + " " + w.getBaseDate() + " " + w.getBaseTime() + " " + w.getNx() + " " + w.getNy() + " " + w.getT1h() + " " + w.getRn1() + " " + w.getSky() + " " + w.getUuu() + " " + w.getVvv() + " " + w.getReh() + " " + w.getPty() + " " + w.getLgt() + " " + w.getVec() + " " + w.getWsd());
            }

            for (Weather weather : weathers) {
                switch (weather.getLocation()) {
                    case "한라산" :
                        weatherTv = (TextView)((MainActivity)mContext).findViewById(R.id.weather_hallasan);
                        break;
                    case "어리목" :
                        weatherTv = (TextView)((MainActivity)mContext).findViewById(R.id.weather_eorimok);
                        break;
                    case "영실" :
                        weatherTv = (TextView)((MainActivity)mContext).findViewById(R.id.weather_yeongsil);
                        break;
                    case "성판악" :
                        weatherTv = (TextView)((MainActivity)mContext).findViewById(R.id.weather_seongpanak);
                        break;
                    case "관음사" :
                        weatherTv = (TextView)((MainActivity)mContext).findViewById(R.id.weather_kwanumsa);
                        break;
                    case "돈내코" :
                        weatherTv = (TextView)((MainActivity)mContext).findViewById(R.id.weather_donnaeko);
                        break;
                }
                StringBuilder sb = new StringBuilder();
                sb.append(weather.getLocation() + " ");

                int pty = weather.getPty();
                if (pty > 0) {
                    switch (pty) {
                        case 1:
                            sb.append("날씨 : 비"  + "\n");
                            break;
                        case 2:
                            sb.append("날씨 : 비/눈" + "\n");
                            break;
                        case 3:
                            sb.append("날씨 : 눈" + "\n");
                            break;
                    }
                }

                int sky = weather.getSky();
                if (sky > 0) {
                    switch (sky) {
                        case 1:
                            sb.append("날씨 : 맑음" + "\n");
                            break;
                        case 2:
                            sb.append("날씨 : 구름 조금"  + "\n");
                            break;
                        case 3:
                            sb.append("날씨 : 구름 많음"  + "\n");
                            break;
                        case 4:
                            sb.append("날씨 : 흐림" + "\n");
                            break;
                    }
                }

                sb.append("기온 : " + weather.getT1h() + " ℃\n");
                sb.append("풍속 : " + weather.getWsd() + " m/s\n");
                sb.append("습도 : " + weather.getReh() + " %\n");


                weatherTv.setText(sb.toString());

            }


        }


//        TextView status1TV = (TextView) ((MainActivity) mContext).findViewById(R.id.status1);
//        status1TV.setText(sb.toString());

    }

}

//        if (weatherCondition != null) {
//            StringBuilder sb = new StringBuilder();
//            sb.append("한라산의 날씨\n");
//
//            float t1h = weatherCondition.getT1h();
//            sb.append("현재 기온은 : " + t1h + " ℃\n");
//
//            int pty = weatherCondition.getPty();
//            if (pty > 0) {
//                switch (pty) {
//                    case 1:
//                        sb.append("날씨 : 비" + "\n");
//                        break;
//                    case 2:
//                        sb.append("날씨 : 비/눈" + "\n");
//                        break;
//                    case 3:
//                        sb.append("날씨 : 눈" + "\n");
//                        break;
//                }
//            }
//
//            int sky = weatherCondition.getSky();
//            if (sky > 0) {
//                switch (sky) {
//                    case 1:
//                        sb.append("날씨 : 맑음" + "\n");
//                        break;
//                    case 2:
//                        sb.append("날씨 : 구름 조금" + "\n");
//                        break;
//                    case 3:
//                        sb.append("날씨 : 구름 많음" + "\n");
//                        break;
//                    case 4:
//                        sb.append("날씨 : 흐림" + "\n");
//                        break;
//                }
//            }
