package com.shinjaehun.annyeonghallasan;

import android.content.Context;
import android.database.Cursor;
import android.support.v4.widget.CursorAdapter;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.shinjaehun.annyeonghallasan.data.HallasanContract;
/**
 * Created by shinjaehun on 2017-05-23.
 */

public class WeatherAdapter extends CursorAdapter {

    private final String LOG_TAG = WeatherAdapter.class.getSimpleName();


    public WeatherAdapter(Context context, Cursor c, int flags) {
        super(context, c, flags);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {

        View v = LayoutInflater.from(context).inflate(R.layout.grid_item_weather, parent, false);
        return v;
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {

        ImageView weatherImageIV = (ImageView) view.findViewById(R.id.weather_img);
        TextView weatherLocationTV = (TextView) view.findViewById(R.id.weather_location);
        TextView weatherTemperatureTV = (TextView) view.findViewById(R.id.weather_temperature);
        TextView weatherInfoTV = (TextView) view.findViewById(R.id.weather_info);

        if (cursor != null) {
            Log.v(LOG_TAG, "ID : " + cursor.getLong(WeatherFragment.COL_WEATHER_ID) + " 장소 : " + cursor.getString(WeatherFragment.COL_WEATHER_LOCATION) + " TimeStamp " + cursor.getString(WeatherFragment.COL_WEATHER_TIMESTAMP)
                    + " SKY " + cursor.getInt(WeatherFragment.COL_WEATHER_SKY));
        }

        if (cursor != null) {
            String location = cursor.getString(WeatherFragment.COL_WEATHER_LOCATION);
            weatherLocationTV.setText(location);

            int sky = cursor.getInt(WeatherFragment.COL_WEATHER_SKY);

            if (sky > 0) {
                switch (sky) {
                    case 1:
                        weatherImageIV.setImageResource(R.drawable.weather_sunny);
                        weatherInfoTV.setText("맑음");
                        break;
                    case 2:
                        weatherImageIV.setImageResource(R.drawable.weather_cloud_sun);
                        weatherInfoTV.setText("구름 조금");
                        break;
                    case 3:
                        weatherImageIV.setImageResource(R.drawable.weather_cloud);
                        weatherInfoTV.setText("구름 많음");
                        break;
                    case 4:
                        weatherImageIV.setImageResource(R.drawable.weather_clouds);
                        weatherInfoTV.setText("흐림");
                        break;
                }
            }

            int pty = cursor.getInt(WeatherFragment.COL_WEATHER_PTY);
            if (pty > 0) {
                switch (pty) {
                    case 1:
                        weatherImageIV.setImageResource(R.drawable.weather_cloud_rain);
                        weatherInfoTV.setText("비");
                        break;
                    case 2:
                        weatherImageIV.setImageResource(R.drawable.weather_cloud_snow_rain);
                        weatherInfoTV.setText("비/눈");
                        break;
                    case 3:
                        weatherImageIV.setImageResource(R.drawable.weather_cloud_snow2);
                        weatherInfoTV.setText("눈");
                        break;
                }
            }

            weatherTemperatureTV.setText(String.valueOf(cursor.getFloat(WeatherFragment.COL_WEATHER_T1H)));

        }



//        cursor.close(); 닝기미 씨발 죽을려고 cursor를 닫아!!

    }
}
