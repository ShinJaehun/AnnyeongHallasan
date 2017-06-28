package com.shinjaehun.annyeonghallasan;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

/**
 * Created by shinjaehun on 2017-06-24.
 */

public class WeatherDialog extends Dialog {
    String LOG_TAG = WeatherDialog.class.getSimpleName();

    private Context mContext;

    private String location;
    private String baseDate;
    private float sky;
    private float pty;
    private float t1h;
    private float reh;
    private float rn1;
    private float vec;
    private float wsd;

    private TextView locationTV;
    private ImageView weatherIV;
    private TextView infoTV;
    private TextView temperatureTV;
    private TextView humidityTV;
    private LinearLayout rainfallL;
    private TextView railfallTV;
    private LinearLayout windSpeedL;
    private TextView windDirectionTV;
    private TextView windSpeedTV;
    private TextView baseDateTV;
    private Button confirmBTN;

    private View.OnClickListener listener;

    private static String[] direction = new String[]{
            "N", "NNE", "NE", "ENE",
            "E", "ESE", "SE", "SSE",
            "S", "SSW", "SW", "WSW",
            "W", "WNW", "NW", "NNW",
            "N"};

    public WeatherDialog(Context context, View.OnClickListener clickListener,
                         String location, String baseDate,
                         int sky, int pty, float t1h, float reh, float rn1, float vec, float wsd) {
        super(context);
        requestWindowFeature(Window.FEATURE_NO_TITLE);

        mContext = context;
        listener = clickListener;

        this.location = location;
        this.baseDate = baseDate;
        this.sky = sky;
        this.pty = pty;
        this.t1h = t1h;
        this.reh = reh;
        this.rn1 = rn1;
        this.vec = vec;
        this.wsd = wsd;

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // dialog 외부 흐림 효과
        WindowManager.LayoutParams lpWindow = new WindowManager.LayoutParams();
        lpWindow.flags = WindowManager.LayoutParams.FLAG_DIM_BEHIND;
        lpWindow.dimAmount = 0.8f;
        getWindow().setAttributes(lpWindow);

        setContentView(R.layout.dialog_weather_info);

        locationTV = (TextView)findViewById(R.id.text_location_weather_adapter);
        locationTV.setText(location);

        int s = Math.round(sky);

        weatherIV = (ImageView)findViewById(R.id.img_weather_adapter);
        infoTV = (TextView)findViewById(R.id.text_info_weather_adapter);

        if (s > 0) {
            switch (s) {
                case 1:
                    weatherIV.setImageResource(R.drawable.weather_sunny);
                    infoTV.setText("맑음");
                    break;
                case 2:
                    weatherIV.setImageResource(R.drawable.weather_cloud_sun);
                    infoTV.setText("구름 조금");
                    break;
                case 3:
                    weatherIV.setImageResource(R.drawable.weather_cloud);
                    infoTV.setText("구름 많음");
                    break;
                case 4:
                    weatherIV.setImageResource(R.drawable.weather_clouds);
                    infoTV.setText("흐림");
                    break;
            }
        }

        int p = Math.round(pty);

        if (p > 0) {
            switch (p) {
                case 1:
                    weatherIV.setImageResource(R.drawable.weather_cloud_rain);
                    infoTV.setText("비");
                    break;
                case 2:
                    weatherIV.setImageResource(R.drawable.weather_cloud_snow_rain);
                    infoTV.setText("비/눈");
                    break;
                case 3:
                    weatherIV.setImageResource(R.drawable.weather_cloud_snow2);
                    infoTV.setText("눈");
                    break;
            }
        }

        temperatureTV = (TextView)findViewById(R.id.text_temperature_weather_adapter);
        temperatureTV.setText(String.valueOf(t1h));

        humidityTV = (TextView)findViewById(R.id.text_weather_humidity);
        humidityTV.setText(String.valueOf(reh));

        rainfallL = (LinearLayout)findViewById(R.id.layout_weather_rainfall);
        railfallTV = (TextView)findViewById(R.id.text_weather_rainfall);
        railfallTV.setText(String.valueOf(rn1));

        windSpeedL = (LinearLayout)findViewById(R.id.layout_weather_wind_speed);
        windDirectionTV = (TextView)findViewById(R.id.text_weather_wind_direction);
        windSpeedTV = (TextView)findViewById(R.id.text_weather_wind_speed);

        int wv = (int)((vec + 22.5 * 0.5) / 22.5);
        String windDirection = " ";
        if (wv >= 0 && wv < direction.length) {
            windDirection = direction[wv];
        }
        windDirectionTV.setText(windDirection);

        int ws = Math.round(wsd);
        String windSpeed = " ";
        if (ws > 4 && ws < 9) {
            windSpeed = "약간 강";
        } else if (ws >= 9  && ws < 14) {
            windSpeed = "강";
        } else if (ws >= 14) {
            windSpeed = "매우 강";
        }
        windSpeedTV.setText(windSpeed);

        baseDateTV = (TextView)findViewById(R.id.text_base_date);
        baseDateTV.setText(baseDate);
//        baseDateTV.setText(baseDate.substring(0, 4) + "년"
//            + baseDate.substring(4, 6) + "월"
//            + baseDate.substring(6) + "일 "
//            + baseTime.substring(0, 2) + "시"
//            + baseTime.substring(2) + "분");
//      이렇게 해 놓으면 00시, 01시 이럴 때 문제가 발생한다. 근데 왜 06월은 정상적으로 출력되지?
        confirmBTN = (Button)findViewById(R.id.button_confirm);

        confirmBTN.setOnClickListener(listener);

    }

    @Override
    protected void onStop() {
        super.onStop();
        dismiss();
    }
}
