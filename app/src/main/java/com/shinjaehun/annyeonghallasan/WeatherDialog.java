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
    private long timeStamp;
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
    private TextView railfallTV;
    private TextView windDirectionTV;
    private TextView windSpeedTV;
    private TextView baseDateTV;
    private Button confirmBTN;

    private View.OnClickListener listener;

    private static String[] direction = new String[]{
            "북", "북북동", "북동", "동북동",
            "동", "동남동", "남동", "남남동",
            "남", "남남서", "남서", "서남서",
            "서", "서북서", "북서", "북북서",
            "북"};

    public WeatherDialog(Context context, View.OnClickListener clickListener,
                         String location, String baseDate, long timeStamp,
                         int sky, int pty, float t1h, float reh, float rn1, float vec, float wsd) {
        super(context);
        requestWindowFeature(Window.FEATURE_NO_TITLE);

        mContext = context;
        listener = clickListener;

        this.location = location;
        this.baseDate = baseDate;
        this.timeStamp = timeStamp;
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
                    weatherIV.setImageResource(R.drawable.weather_white_big_sunny);
                    infoTV.setText("맑음");
                    break;
                case 2:
                    weatherIV.setImageResource(R.drawable.weather_white_big_cloud_sun);
                    infoTV.setText("구름 조금");
                    break;
                case 3:
                    weatherIV.setImageResource(R.drawable.weather_white_big_cloud);
                    infoTV.setText("구름 많음");
                    break;
                case 4:
                    weatherIV.setImageResource(R.drawable.weather_white_big_clouds);
                    infoTV.setText("흐림");
                    break;
            }
        }

        int p = Math.round(pty);

        if (p > 0) {
            switch (p) {
                case 1:
                    weatherIV.setImageResource(R.drawable.weather_white_big_cloud_rain);
                    infoTV.setText("비");
                    break;
                case 2:
                    weatherIV.setImageResource(R.drawable.weather_white_big_cloud_snow_rain);
                    infoTV.setText("비/눈");
                    break;
                case 3:
                    weatherIV.setImageResource(R.drawable.weather_white_big_cloud_snow2);
                    infoTV.setText("눈");
                    break;
            }
        }

        temperatureTV = (TextView)findViewById(R.id.text_temperature_weather_adapter);
        temperatureTV.setText(String.valueOf(t1h));

        humidityTV = (TextView)findViewById(R.id.text_weather_humidity);
        humidityTV.setText(String.valueOf(reh));

        railfallTV = (TextView)findViewById(R.id.text_weather_rainfall);
        railfallTV.setText(String.valueOf(rn1));

        windDirectionTV = (TextView)findViewById(R.id.text_weather_wind_direction);
        windSpeedTV = (TextView)findViewById(R.id.text_weather_wind_speed);

        int wv = (int)((vec + 22.5 * 0.5) / 22.5);
        // (풍향값 + 22.5 * 0.5) / 22.5) = 16방위 변환값(소수점 이하 버림) 기상청 api 매뉴얼

        String windDirection = " ";
        if (wv >= 0 && wv < direction.length) {
            windDirection = direction[wv];
        }
        windDirectionTV.setText(windDirection);

        int ws = Math.round(wsd);
        String windSpeed = " ";
        if (ws > 4 && ws < 9) {
            windSpeed = "약간 강한 바람";
        } else if (ws >= 9  && ws < 14) {
            windSpeed = "강한 바람";
        } else if (ws >= 14) {
            windSpeed = "매우 강한 바람";
        }
        windSpeedTV.setText(windSpeed);

        baseDateTV = (TextView)findViewById(R.id.text_base_date);
//        baseDateTV.setText(baseDate);
        baseDateTV.setText(baseDate.substring(0, 4) + "년"
                 + baseDate.substring(4, 6) + "월"
                 + baseDate.substring(6, 8) + "일 "
                 + baseDate.substring(8, 10) + "시"
                 + baseDate.substring(10) + "분\n" + "타임스탬프" + String.valueOf(timeStamp));
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
